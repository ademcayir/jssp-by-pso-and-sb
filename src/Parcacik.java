

class Particle {
	private double x[];
	private int is_sirasi[];
	private int makine_sirasi[];
	private double v[];
	
	private double best_x[];
	private int best_order[];
	private int best_x_tft;
	private int temp[];
	private Problem problem;
	public void init(Problem problem,int x_min,int x_max,int v_min,int v_max,int temp[]){
		this.temp = temp;
		this.problem = problem;
		x = new double[problem.getBoyutSayisi()];
		v = new double[problem.getBoyutSayisi()];
		best_x = new double[problem.getBoyutSayisi()];
		best_order = new int[problem.getBoyutSayisi()];
		is_sirasi = new int[problem.getBoyutSayisi()];
		makine_sirasi = new int[problem.getBoyutSayisi()];
		for (int i = 0; i < x.length; i++) {
			x[i] = Algorithms.random.nextDouble()%(x_max - x_min) + x_min;			
			v[i] = Algorithms.random.nextDouble()%(v_min - v_max ) + v_min;			
		}
		System.arraycopy(x, 0, best_x, 0, best_x.length);
		Algorithms.apply_SPV("init",best_x, best_order);
		Algorithms.localSearch(problem, best_order,makine_sirasi);
		best_x_tft = problem.yayilma_zamani;
	}
	public double[] getBestX(){
		return best_x;
	}
	public int[] getBestOrder(){
		return best_order;
	}
	public int getBestTFT(){
		return best_x_tft;
	}
	public double[] getCurrentX(){
		return x;
	}
	public double[] getCurrentV(){
		return v;
	}
	public void nextMove(double global_best[], double inertia,double c1, double c2){
		for (int i = 0; i < global_best.length; i++) {
			v[i] = v[i] * inertia + c1 * Math.abs(Algorithms.random.nextDouble()) * (best_x[i] - x[i]) + c2 * Math.abs(Algorithms.random.nextDouble())*(global_best[i] - x[i]);
			x[i] = x[i] + v[i];
		}
		
		Algorithms.apply_SPV("nextMove",x,is_sirasi);
		Algorithms.localSearch(problem, is_sirasi,makine_sirasi);
		int current_tft = problem.yayilma_zamani;
		if (current_tft < best_x_tft){
			best_x_tft = current_tft;
			System.arraycopy(x, 0, best_x, 0, x.length);
			System.arraycopy(is_sirasi, 0, best_order, 0, is_sirasi.length);
			
			if (PSO.instance().getCozumYontemi() == PSO.PSO_SB){
				Algorithms.apply_SB(problem, is_sirasi,makine_sirasi, x,v);
			}
		}
	}
	public void degerleri_goster(String label){
		System.out.println("Particle: "+label);
		System.out.println("best tft >"+best_x_tft);
		System.out.println("last x >"+Algorithms.getArrayString(x));
		System.out.println("last v >"+Algorithms.getArrayString(v));
		System.out.println("best x >"+Algorithms.getArrayString(best_x));
		System.out.println("best order >"+Algorithms.getArrayString(best_order));
	}
}
