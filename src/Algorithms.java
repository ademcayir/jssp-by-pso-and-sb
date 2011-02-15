import javax.jws.Oneway;
import javax.swing.text.GapContent;


public class Algorithms {
	public static void localSearch2(Problem problem,int cozum[],int tmp[]){
		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = cozum[i]/problem.makine_sayisi;
		}
		localSearch(problem, tmp);
	}
	public static void localSearch(Problem problem,int cozum[]){
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
		for (int i = 0; i < cozum.length; i++) {
//			System.out.println("suanda["+i+"]"+cozum[i]);
//			if (i == 14){
//				System.out.println("--");
//			}
			AltIs siradaki_is = problem.isler[cozum[i]].siralanmamis_ilk_eleman();
			int siradaki_isin_bir_onceki_is_bitis_zamani = 0;
			if (siradaki_is == null){
				System.out.println(">>>>:siradi is null");
			}
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
		
		for (int i = 0; i < order.length; i++) {
			double min = 1000;
			int target=0;
			for (int j = 0; j < x.length; j++) {
				if (j == 0 || x[j] < min){
					target = j;
					min = x[j];
				}
			}
			x[target] += 1000000;
			order[i] = target;
		}
		for (int i = 0; i < x.length; i++) {
			x[i] -= 1000000;
		}
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
