import java.util.Vector;


public class Is {
	public int is_no;
	public Is(int is_no){
		this.is_no = is_no;
	}
	public AltIs baslangic = new AltIs(this,-1,0);
	public void alt_is_ekle(int makine_no, int sure){
		AltIs son_is = baslangic;
		while (son_is.is_icin_sonrasi != null){
			son_is = son_is.is_icin_sonrasi;
		}
		son_is.is_icin_sonrasi = new AltIs(this,makine_no, sure);
		son_is.is_icin_sonrasi.is_icin_oncesi = son_is;
	}
	public AltIs siralanmamis_ilk_eleman(){
		AltIs alt_is = baslangic;
		while (alt_is.is_icin_sonrasi != null){
			alt_is = alt_is.is_icin_sonrasi;
			if (alt_is.baslangic_zamani == -1){
				return alt_is;
			}
		}
		return null;
	}
}
