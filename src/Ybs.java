import java.util.Random;


public class Ybs {
	private static Random random = new Random();
	public static void ybs(Problem problem){
		final int NUM_OF_LIBRARY = 4;
		final int ITERATION_NUM = 1000;
		
		int library[][] = new int[NUM_OF_LIBRARY][problem.makine_sayisi*problem.is_sayisi];
		int antijen[] = new int[problem.makine_sayisi*problem.is_sayisi];
		randomize(antijen, problem.makine_sayisi);
		int copy_antijen[] = new int[antijen.length];
		System.arraycopy(antijen, 0, copy_antijen, 0, antijen.length);
		Algorithms.localSearch(problem, antijen);
		int makespan = problem.yayilma_zamani;
		for (int i = 0; i < ITERATION_NUM; i++) {
			randomize(copy_antijen, problem.makine_sayisi);
			Algorithms.localSearch(problem, antijen);
			if (makespan < problem.yayilma_zamani){
				makespan = problem.yayilma_zamani;
				System.out.println("iyi cozum bulundu:"+problem.yayilma_zamani);
				Algorithms.cozum_sira_goster(antijen);
				problem.sirayi_goster();
				System.arraycopy(copy_antijen, 0, antijen, 0, antijen.length);
			}
		}
		System.out.println("");
		System.out.println("");
		System.out.println("makespan="+makespan);
		Algorithms.cozum_sira_goster(antijen);
		Algorithms.localSearch(problem, antijen);
		problem.sirayi_goster();
	}
	
	private static void randomize(int cozum[],int makine_sayisi){
		for (int i = 0; i < cozum.length; i++) {
			cozum[i] = i/makine_sayisi;
		}
		for (int i = 0; i < cozum.length/2; i++) {
			int from = Math.abs(random.nextInt() % cozum.length);
			int to = Math.abs(random.nextInt() % cozum.length);
			int tmp = cozum[from];
			cozum[from] = cozum[to];
			cozum[to] = tmp;
		}
	}
}
