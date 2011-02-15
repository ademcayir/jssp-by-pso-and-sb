import java.util.Random;


class Particle {
	private double x[];
	private int current_order[];
	private double v[];
	
	private double best_x[];
	private int best_order[];
	private int best_x_tft;
	private int temp[];
	private static Random random = new Random();
	private Problem problem;
	public void init(Problem problem,int x_min,int x_max,int v_min,int v_max,int temp[]){
		this.temp = temp;
		this.problem = problem;
		x = new double[problem.getBoyutSayisi()];
		v = new double[problem.getBoyutSayisi()];
		best_x = new double[problem.getBoyutSayisi()];
		best_order = new int[problem.getBoyutSayisi()];
		current_order = new int[problem.getBoyutSayisi()];
		for (int i = 0; i < x.length; i++) {
			x[i] = random.nextDouble()%(x_max - x_min) + x_min;			
			v[i] = random.nextDouble()%(v_min - v_max ) + v_min;			
		}
		System.arraycopy(x, 0, best_x, 0, best_x.length);
		Algorithms.apply_SPV("init",best_x, best_order);
		Algorithms.localSearch2(problem, best_order,temp);
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
			v[i] = v[i] * inertia + c1 * Math.abs(random.nextDouble()) * (best_x[i] - x[i]) + c2 * Math.abs(random.nextDouble())*(global_best[i] - x[i]);
			x[i] = x[i] + v[i];
		}
		Algorithms.apply_SPV("nextMove",x,current_order);
		Algorithms.localSearch2(problem, current_order, temp);
		int current_tft = problem.yayilma_zamani;
		if (current_tft < best_x_tft){
			best_x_tft = current_tft;
			System.arraycopy(x, 0, best_x, 0, x.length);
			System.arraycopy(current_order, 0, best_order, 0, current_order.length);
		}
	}
	public void degerleri_goster(String label){
		System.out.print(label+">");
		for (int i = 0; i < x.length; i++) {
			System.out.print(" x["+i+"]="+x[i]);
		}
		System.out.println(label+">");
		for (int i = 0; i < x.length; i++) {
			System.out.print(" v["+i+"]="+v[i]);
		}
		System.out.println();
	}
}
