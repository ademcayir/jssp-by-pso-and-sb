import java.io.InputStream;
import java.util.Scanner;
import java.util.Vector;


public class Problem {
	public Is isler[];
	public String name;
	public String desc;
	public int makine_sayisi;
	public int is_sayisi;
	public int yayilma_zamani;
	public Problem(){
	}
	public int getBoyutSayisi(){
		return makine_sayisi*is_sayisi;
	}
	public boolean parse(InputStream input) {
		try {
			Scanner scanner = new Scanner(input);
			int satir_no=0;
			int makine_sayisi =0;
			int is_sayisi=0;
			
			int current_is =0;
			StringBuffer buf = new StringBuffer();
			while (scanner.hasNextLine()){
				String line =scanner.nextLine().trim();
				if (line.equals("")){
					continue;
				} else if (satir_no == 0){
					if (line.startsWith("instance")){
						line = line.substring(9);
					}
					name = line;
					satir_no = 1;
					continue;
				} else if (satir_no == 1){
					desc = line;
					satir_no = 2;
					continue;
				} else if (satir_no == 2){
					int index = line.indexOf(' ');
					is_sayisi = Integer.parseInt(line.substring(0,index).trim());
					makine_sayisi = Integer.parseInt(line.substring(index).trim());
					isler = new Is[is_sayisi];
					this.is_sayisi = is_sayisi;
					this.makine_sayisi = makine_sayisi;
					satir_no = 3;
					continue;
				}
				isler[current_is] = new Is(current_is);
				buf.delete(0, buf.length());
				int makine=-1;
				int sure=0;
				for (int i = 0; i < line.length(); i++) {
					char c = line.charAt(i);
					if (c == ' ' && buf.length() > 0){
						if (makine == -1){
							makine = Integer.parseInt(buf.toString());
						} else {
							sure = Integer.parseInt(buf.toString());
							isler[current_is].alt_is_ekle(makine, sure);
							makine = -1;
						}
						buf.delete(0, buf.length());
						continue;
					}
					if (c != ' '){
						buf.append(c);
					}
				}
				if (makine != -1 && buf.length() > 0){
					sure = Integer.parseInt(buf.toString());
					isler[current_is].alt_is_ekle(makine, sure);
				}
				current_is++;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void goster(){
		System.out.println("İsim: "+name);
		System.out.println("Açıklama: "+desc);
		for (int i = 0; i < isler.length; i++) {
			System.out.print("iş["+(i+1)+"] > ");
			AltIs alt_is = isler[i].baslangic;
			while (alt_is.is_icin_sonrasi != null){
				alt_is = alt_is.is_icin_sonrasi;
				System.out.print((alt_is.makine+1)+"("+alt_is.sure+") ");
			}
			System.out.println();
		}
	}
	
	public void sirayi_goster(){
		AltIs makine_baslangiclari[] = new AltIs[makine_sayisi];
		AltIs tmp = null;
		System.out.println("yayilma zamanı: "+yayilma_zamani);
		for (int j = 0; j < isler.length; j++) {
			tmp = isler[j].baslangic;
			while (tmp.is_icin_sonrasi != null){
				if (tmp.makine != -1 && tmp.baslangic_zamani != -1){
					if (makine_baslangiclari[tmp.makine] == null){
						makine_baslangiclari[tmp.makine] = tmp;
					} else if (makine_baslangiclari[tmp.makine].baslangic_zamani > tmp.baslangic_zamani){
						makine_baslangiclari[tmp.makine] = tmp;
					}
				}
				tmp = tmp.is_icin_sonrasi;
			}
		}
		
		
//		tmp = null;
//		for (int i = 0; i < makine_baslangiclari.length; i++) {
//			tmp = makine_baslangiclari[i];
//			System.out.print("iş["+i+"]>");
//			while (tmp != null){
//				System.out.print(tmp.parent.is_no+"("+tmp.baslangic_zamani+","+tmp.sure+") ");
//				tmp = tmp.makine_icin_sonrasi;
//			}
//			System.out.println();
//		}
		
		
		tmp = null;
		for (int i = 0; i < makine_baslangiclari.length; i++) {
			tmp = makine_baslangiclari[i];
			System.out.print("makine="+(i+1)+"> ");
			if (tmp == null){
				System.out.println("");
				continue;
			}
			
			int mevcut = 0;
			while (tmp != null){
				for (int j = mevcut; j < tmp.baslangic_zamani; j++) {
					System.out.print(' ');
				}
				for (int j = 0; j < tmp.sure; j++) {
					System.out.print(Integer.toHexString((tmp.parent.is_no+1)));
				}
				mevcut = tmp.baslangic_zamani+tmp.sure;
				tmp = tmp.makine_icin_sonrasi;
			}
			System.out.println();
		}
		System.out.println();
	}
}

