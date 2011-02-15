import java.io.InputStream;
import java.util.Vector;


public class Test {
	public static void main(String[] args) {
		InputStream input = "".getClass().getResourceAsStream("/problem_abz5");
		Problem p = new Problem();
		p.parse(input);
		try {
			input.close();
		} catch (Exception e) {
		}
		p.goster();
		//Ybs.ybs(p);
		
		
		int cozum[] = new int[p.makine_sayisi*p.is_sayisi];
		
//		cozum = new int[]{
//				2,2,4,1,
//				4,5,0,3,
//				2,0,3,5,
//				5,5,0,4,
//				2,4,3,0,
//				3,1,1,1
//		};
		
//		cozum = new int[]{
//			0, 4, 3, 5, 1 ,4 ,1, 5, 2, 2, 5, 1, 0, 3, 0, 3, 5, 4, 3, 0, 4, 2, 1, 2,
//		};
		
		Suru suru = new Suru();
		suru.init(p, 10, -4, 4, 0, 4);
		suru.solve(2000, 1000);
		
		suru.degerleri_goster();
		
		
		System.out.println("best:"+p.yayilma_zamani);
		DisplayManager.show(p);
		
//		Suru suru = new Suru();
//		suru.init(p, 10, -4, 4, 0, 4);
//		suru.solve(10000, 10000);
//		cozum = suru.getBest();
//		int tmp[] = new int[cozum.length];
//		Algorithms.localSearch2(p, cozum,tmp);
//		p.sirayi_goster();
	}
}
