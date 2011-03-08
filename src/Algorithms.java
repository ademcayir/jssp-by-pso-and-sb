import java.util.Random;





public class Algorithms {
	
	public static Random random = new Random();
	private static boolean temp_full_list[];
	private static boolean temp_makine_list[];
	
	public static void localSearch(Problem problem,int is_sirasi[],int makine_sirasi[]){
		for (int i = 0; i < problem.isler.length; i++) {
			AltIs baslangic = problem.isler[i].baslangic;
			while (baslangic.is_icin_sonrasi != null){
				baslangic = baslangic.is_icin_sonrasi;
				baslangic.makine_icin_oncesi = null;
				baslangic.makine_icin_sonrasi = null;
				baslangic.baslangic_zamani = -1;
			}
		}
		AltIs makinedeki_son_is[] = new AltIs[problem.makine_sayisi];
		int isler_son_zaman[] = new int[problem.is_sayisi];
		for (int i = 0; i < is_sirasi.length; i++) {
//			System.out.println("suanda["+i+"]"+cozum[i]);
//			if (i == 14){
//				System.out.println("--");
//			}
			int cozum_is = is_sirasi[i] / problem.makine_sayisi;
			AltIs siradaki_is = problem.isler[cozum_is].siralanmamis_ilk_eleman();
			if (siradaki_is == null){
				System.out.println(">>>>:siradi is null::"+i+">>"+Algorithms.getArrayString(is_sirasi));
				System.out.println();
			}
			if (makine_sirasi != null ){
				makine_sirasi[i] = siradaki_is.makine;
			}
			int siradaki_isin_bir_onceki_is_bitis_zamani = 0;
			if (siradaki_is.is_icin_oncesi == null){
				System.out.println("is icin oncesi nulll");
			}
			if (siradaki_is.is_icin_oncesi.makine != -1){
				siradaki_isin_bir_onceki_is_bitis_zamani = siradaki_is.is_icin_oncesi.baslangic_zamani+siradaki_is.is_icin_oncesi.sure;
			}
			if (makinedeki_son_is[siradaki_is.makine] == null){
				makinedeki_son_is[siradaki_is.makine] = siradaki_is;
				siradaki_is.baslangic_zamani = isler_son_zaman[siradaki_is.parent.is_no];
				isler_son_zaman[siradaki_is.parent.is_no] = siradaki_is.baslangic_zamani + siradaki_is.sure;
				
			} else {
				AltIs tarama_yapilacak_is = makinedeki_son_is[siradaki_is.makine];
 				AltIs uygun_onceki_is = makinedeki_son_is[siradaki_is.makine];
				int uygun_baslangic_zamani = uygun_onceki_is.baslangic_zamani+uygun_onceki_is.sure;
				if (siradaki_isin_bir_onceki_is_bitis_zamani > uygun_baslangic_zamani){
					uygun_baslangic_zamani = siradaki_isin_bir_onceki_is_bitis_zamani;
				}
				while (tarama_yapilacak_is.makine_icin_oncesi != null){
					AltIs tarama_yapilacak_bir_onceki_is = tarama_yapilacak_is.makine_icin_oncesi;
					int bir_onceki_sinir = (tarama_yapilacak_bir_onceki_is.baslangic_zamani + tarama_yapilacak_bir_onceki_is.sure);
					if (siradaki_isin_bir_onceki_is_bitis_zamani > bir_onceki_sinir ){
						bir_onceki_sinir = siradaki_isin_bir_onceki_is_bitis_zamani;
					}
					int gap = tarama_yapilacak_is.baslangic_zamani - bir_onceki_sinir;
					if (gap >= siradaki_is.sure ){
						uygun_onceki_is = tarama_yapilacak_bir_onceki_is;
						uygun_baslangic_zamani = bir_onceki_sinir;
						if (uygun_baslangic_zamani < siradaki_isin_bir_onceki_is_bitis_zamani){
							uygun_baslangic_zamani = siradaki_isin_bir_onceki_is_bitis_zamani;
						}
					}
					tarama_yapilacak_is = tarama_yapilacak_bir_onceki_is;
					if (tarama_yapilacak_is.baslangic_zamani < siradaki_isin_bir_onceki_is_bitis_zamani){
						break;
					}
				}
				if (tarama_yapilacak_is.makine_icin_oncesi == null && tarama_yapilacak_is.baslangic_zamani - siradaki_isin_bir_onceki_is_bitis_zamani >= siradaki_is.sure ){
					uygun_onceki_is = null;
					uygun_baslangic_zamani = siradaki_isin_bir_onceki_is_bitis_zamani;
				}
				
				if (uygun_onceki_is != null ){
					if (uygun_onceki_is.makine_icin_sonrasi != null ){
						uygun_onceki_is.makine_icin_sonrasi.makine_icin_oncesi = siradaki_is;
						siradaki_is.makine_icin_sonrasi = uygun_onceki_is.makine_icin_sonrasi;
					} else {
						makinedeki_son_is[siradaki_is.makine] = siradaki_is;
					}
					uygun_onceki_is.makine_icin_sonrasi = siradaki_is;
					siradaki_is.makine_icin_oncesi = uygun_onceki_is;
					siradaki_is.baslangic_zamani = uygun_baslangic_zamani;
					isler_son_zaman[siradaki_is.parent.is_no] = siradaki_is.baslangic_zamani + siradaki_is.sure;
				} else {
					AltIs tmp = makinedeki_son_is[siradaki_is.makine];
					while (tmp.makine_icin_oncesi != null){
						tmp = tmp.makine_icin_oncesi;
					}
					tmp.makine_icin_oncesi = siradaki_is;
					siradaki_is.makine_icin_sonrasi = tmp;
					siradaki_is.baslangic_zamani = uygun_baslangic_zamani;
					isler_son_zaman[siradaki_is.parent.is_no] = siradaki_is.baslangic_zamani + siradaki_is.sure;
				}
			}
//			problem.sirayi_goster();
		}
		problem.yayilma_zamani = 0;
		for (int i = 0; i < makinedeki_son_is.length; i++) {
			if (makinedeki_son_is[i] != null){
				if (makinedeki_son_is[i].baslangic_zamani + makinedeki_son_is[i].sure > problem.yayilma_zamani){
					problem.yayilma_zamani = makinedeki_son_is[i].baslangic_zamani + makinedeki_son_is[i].sure;
				}
			}
		}
	}
	
	public static void hata_ara(Problem problem){
		
		for (int i = 0; i < problem.isler.length; i++) {
			AltIs tmp = problem.isler[i].baslangic;
			int mevcut = 0;
			while (tmp.is_icin_sonrasi != null){
				tmp= tmp.is_icin_sonrasi;
				if (tmp.baslangic_zamani < mevcut ){
					System.out.println("HATALI");
					tmp = problem.isler[i].baslangic;
					int counter = 0;
					while (tmp.is_icin_sonrasi != null){
						tmp = tmp.is_icin_sonrasi;
						System.out.println((counter++)+"-("+tmp.makine+")>"+tmp.baslangic_zamani+","+(tmp.baslangic_zamani+tmp.sure));
					}
					i = problem.isler.length;
					break;
				}
				mevcut = tmp.baslangic_zamani+tmp.sure;
			}
		}
	}
	
	public static void cozum_sira_goster(int cozum[]){
		System.out.print("sira>");
		for (int i = 0; i < cozum.length; i++) {
			System.out.print(cozum[i]+",");
		}
		System.out.println();
	}
	
	public static void apply_SPV(String from,double x[],int order[]){
//		System.out.println("SPV: from > "+from);
//		System.out.println("SPV: order > "+getArrayString(order));
//		System.out.println("SPV: x     > "+getArrayString(x));

		if (temp_full_list == null || temp_full_list.length != x.length){
			temp_full_list = new boolean[x.length];
		}
		for (int i = 0; i < temp_full_list.length; i++) {
			temp_full_list[i] = false;
		}
		
		boolean min_set;
		for (int i = 0; i < order.length; i++) {
			double min = 1000;
			int target=0;
			min_set = false;
			for (int j = 0; j < x.length; j++) {
				if (!temp_full_list[j] && (!min_set || x[j] < min)){
					min_set = true;
					target = j;
					min = x[j];
				}
			}
			order[i] = target;
			temp_full_list[target] = true;
		}
	}
	public static void apply_SB(Problem problem, int is_sirasi[],int makine_sirasi[], double x[],double v[]){
		if (temp_makine_list == null || temp_makine_list.length != problem.makine_sayisi){
			temp_makine_list = new boolean[problem.makine_sayisi];
		}
		for (int i = 0; i < temp_makine_list.length; i++) {
			temp_makine_list[i] = false;
		}
		int is_sirasi_temp[] = clone(is_sirasi);
		int makine_sirasi_temp[] = clone(makine_sirasi);
		double x_temp[] = clone(x);
		double v_temp[] = clone(v);
		System.out.println("--");
		for (int k = 0; k < problem.makine_sayisi;k++){
			int bottleneck = findBotteneck(problem, is_sirasi,temp_makine_list);
			temp_makine_list[bottleneck] = true;
			System.out.println("bottleneck:"+bottleneck);
			int start = 0;
			int end = 0;
			int yayilma_zamani = problem.yayilma_zamani;
			for (int i =0; i < makine_sirasi.length;i++){
				if (makine_sirasi[i] == bottleneck){
					end = i;
					if (end - start > 1){
						for (int j = start; j < end; j++) {
							int t = Math.abs(Algorithms.random.nextInt() % (end - start)) + start;
							swap(is_sirasi_temp, j, t);
							swap(x_temp, j, t);
							swap(v_temp, j, t);
						}
					}
					start = end+1;
				}
			}
			Algorithms.localSearch(problem, is_sirasi_temp, makine_sirasi);
			if (problem.yayilma_zamani < yayilma_zamani){
				System.arraycopy(is_sirasi_temp, 0, is_sirasi, 0, is_sirasi.length);
				System.arraycopy(x_temp, 0, x, 0, x.length);
				System.arraycopy(v_temp, 0, v, 0, v.length);
			}
		}
	}
	
	private static void swap(double a[],int i,int j){
		double tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}
	private static void swap(int a[],int i,int j){
		int tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}
	private static int[] clone(int a[]){
		int t[] = new int[a.length];
		System.arraycopy(a, 0, t, 0, t.length);
		return t;
	}
	private static double[] clone(double a[]){
		double t[] = new double[a.length];
		System.arraycopy(a, 0, t, 0, t.length);
		return t;
	}
	
	
	private static int findBotteneck(Problem problem, int current_order[],boolean ignore_makine_list[]){
		AltIs makine_isler_baslangiclar[] = new AltIs[problem.makine_sayisi];
		AltIs temp_alt_is = problem.isler[0].baslangic;
		while (temp_alt_is.is_icin_sonrasi != null){
			temp_alt_is = temp_alt_is.is_icin_sonrasi;
			makine_isler_baslangiclar[temp_alt_is.makine] = temp_alt_is;
			while (makine_isler_baslangiclar[temp_alt_is.makine].makine_icin_oncesi != null){
				makine_isler_baslangiclar[temp_alt_is.makine] = makine_isler_baslangiclar[temp_alt_is.makine].makine_icin_oncesi;
			}
		}
		int baslangiclar[] = new int[problem.makine_sayisi];
		int bitisler[] = new int[problem.makine_sayisi];
		int sureler[] = new int[problem.makine_sayisi];
		for (int i = 0; i < makine_isler_baslangiclar.length; i++) {
			temp_alt_is = makine_isler_baslangiclar[i];
			boolean baslangic = true;
			while (temp_alt_is != null){
				if (baslangic){
					baslangiclar[i] = temp_alt_is.baslangic_zamani;
					baslangic = false;
				}
				bitisler[i] = temp_alt_is.baslangic_zamani+temp_alt_is.sure;
				sureler[i] += temp_alt_is.sure;
				temp_alt_is = temp_alt_is.makine_icin_sonrasi; 
			}
		}
		int target_bosluk=0;
		int bosluk = 0;
		int target_yayilma_zamani=0;
		int yayilma_zamani = 0;
		for (int i = 0; i < sureler.length; i++) {
			if (ignore_makine_list[i]){
				continue;
			}
			int tmp = bitisler[i] - baslangiclar[i] - sureler[i];
			if (tmp > bosluk){
				bosluk = tmp;
				target_bosluk = i;
			}
			if (bitisler[i] > yayilma_zamani){
				yayilma_zamani = bitisler[i];
				target_yayilma_zamani = i;
			}
		}
		return target_yayilma_zamani;
	}
	
	public static String getArrayString(double array[]){
		StringBuffer buf = new StringBuffer();
		buf.append('[');
		for (int i = 0; i < array.length; i++) {
			buf.append(doubleToString(array[i]));
			if(array.length != i + 1){
				buf.append(',');
			}
		}
		buf.append(']');
		return buf.toString();
	}
	public static String doubleToString(double d){
		String s = ""+d;
		if (d >= 0){
			s = "+"+s;
		}
		while (s.length() < 5){
			s += "0";
		}
		return s.substring(0,5);
	}
	public static String getArrayString(int array[]){
		StringBuffer buf = new StringBuffer();
		buf.append('[');
		for (int i = 0; i < array.length; i++) {
			buf.append(array[i]);
			if(array.length != i + 1){
				buf.append(',');
			}
		}
		buf.append(']');
		return buf.toString();
	}
}
