
class Suru {
	private Problem problem;
	private Particle particles[];
	private double global_best[];
	private int global_best_order[];
	private int global_best_tft;
	private int temp[];
	private boolean init;
	private boolean stop;
	private long baslangic_zamani;
	private Cozum cozum;
	private int current_iterasyon_sayisi;
	public Suru(Cozum cozum){
		this.cozum = cozum;
		init = false;
	}
	public void stop(){
		stop = true;
	}
	public double[] getBestVals(){
		return global_best;
	}
	public int[] getBest(){
		return global_best_order;
	}
	public int getBestTft(){
		return global_best_tft;
	}
	public void benchmark_log(){
		for (int i = 0; i < particles.length; i++) {
			Logger.addLine();
			Logger.addLine("Parca: "+i);
			Logger.addLine("En Iyi Zaman: "+particles[i].getBestTFT());
			Logger.addLine("En Iyi Konum Vectoru: "+Algorithms.getArrayString(particles[i].getBestOrder()));
			Logger.addLine("Konum Vectoru: "+Algorithms.getArrayString(particles[i].getCurrentX()));
			Logger.addLine("Hiz Vectoru: "+Algorithms.getArrayString(particles[i].getCurrentV()));
		}
	}
	
	
	
	public void init(Problem problem,int particle_num,int x_min,int x_max,int v_min,int v_max){
		this.problem = problem;
		stop = false;
		particles = new Particle[particle_num];
		global_best = new double[problem.getBoyutSayisi()];
		global_best_order = new int[problem.getBoyutSayisi()];
		temp = new int[problem.getBoyutSayisi()];
		for (int i = 0; i < particles.length; i++) {
			particles[i] = new Particle();
			particles[i].init(problem,x_min, x_max, v_min, v_max,temp);
			if (i == 0){
				System.arraycopy(particles[i].getBestX(), 0, global_best, 0, global_best.length);
				System.arraycopy(particles[i].getBestOrder(), 0, global_best_order, 0, global_best_order.length);
				global_best_tft = particles[i].getBestTFT();
			}
		}
		calculate_global_best(0);
		init = true;
	}
	
	public void solve(int maks_itesyon, int maks_millis){
		double w = 0.9;
		double b = 0.975;
		int percent=0;
		baslangic_zamani = System.currentTimeMillis();
		int j=0;
		if (maks_millis <= 0){
			maks_millis = -1;
		}
		for (j = 0;; j++) {
			
			for (int i = 0; i < particles.length; i++) {
				particles[i].nextMove(global_best, w, 2, 2);
			}
			w = w * b;
			if (w < 0.4){
				w = 0.4;
			}
			calculate_global_best(j);
			
			
			if (maks_itesyon != -1 ){
				percent = (j*100)/maks_itesyon;
				if (j >= maks_itesyon){
					break;
				}
			}
			long ti2 = System.currentTimeMillis();
			if (maks_millis != -1 ) {
				if ( ti2 - baslangic_zamani > maks_millis){
					break;
				}
				int percent2 = (int)(((ti2-baslangic_zamani)*100)/maks_millis);
				if (percent2 > percent){
					percent = percent2;
				}
			}
			PSOScreen.instance().pso_set_current_time(System.currentTimeMillis() - baslangic_zamani);
			if (stop){
				break;
			}
			PSOScreen.instance().pso_set_current_percent(percent);
			PSOScreen.instance().pso_set_current_iterasyon(j+1);
			current_iterasyon_sayisi = j;
		}
		cozum.iterasyon_sayisi = j;
		cozum.toplam_zaman = System.currentTimeMillis() - baslangic_zamani;
		PSOScreen.instance().pso_set_current_percent(100);
		Algorithms.localSearch(problem, global_best_order,new int[problem.getBoyutSayisi()]);
		
		System.out.println("sonuçlar");
		for (int i = 0; i < particles.length; i++) {
			particles[i].degerleri_goster(""+i+">");
		}
		
		stop = true;
	}
	private void calculate_global_best(int step){
		for (int i = 0; i < particles.length; i++) {
			double p_x[] = particles[i].getBestX();
			int p_order[] = particles[i].getBestOrder();
			int pbest = particles[i].getBestTFT();
			if (pbest < global_best_tft ){
				global_best_tft = pbest;
				PSOScreen.instance().pso_set_makespan(global_best_tft);
				problem.yayilma_zamani = global_best_tft;
				if (baslangic_zamani == 0){
					problem.en_iyi_cozum_zamani = 0; 
					problem.en_iyi_cozum_iterasyon_sayisi = 1; 
				} else {
					problem.en_iyi_cozum_zamani = System.currentTimeMillis() - baslangic_zamani; 
					problem.en_iyi_cozum_iterasyon_sayisi = current_iterasyon_sayisi; 
				}
				System.arraycopy(p_x, 0, global_best, 0, p_x.length);
				System.arraycopy(p_order, 0, global_best_order, 0, p_x.length);
			}
		}
	}
	public void degerleri_goster(){
		System.out.println("sürü değerleri:");
		for (int i = 0; i < particles.length; i++) {
			particles[i].degerleri_goster(i+"");
		}
	}
}
