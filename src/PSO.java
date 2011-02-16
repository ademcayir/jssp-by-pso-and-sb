import java.io.InputStream;



public class PSO {
	private static PSO instance;
	public static PSO instance(){
		if (instance == null){
			instance = new PSO();
		}
		return instance;
	}
	private boolean dosyaya_yaz;
	private int problem_index;
	private int max_iterasyon;
	private int parcacik_sayisi;
	private int zaman_kisiti;
	private int deneme_sayisi;
	private boolean running;
	private Suru current_suru;
	public void setDosyayaYaz(boolean b){
		dosyaya_yaz = b;
	}
	public void setProblemIndex(int index){
		problem_index = index;
	}
	public void setMaksimum_iterasyon(int index){
		max_iterasyon = index;
	}
	public void setParcacik_sayisi(int index){
		parcacik_sayisi = index;
	}
	public void setZaman_kisiti(int index){
		zaman_kisiti = index;
	}
	public void setDenemeSayisi(int deneme){
		deneme_sayisi = deneme;
	}
	public void baslat(){
		if (!running){
			running = true;
			Thread t = new Thread(){
				public void run() {
					Problem son = null;
					for (int i = 0; running && i < deneme_sayisi; i++) {
						InputStream input = PSOScreen.instance().getClass().getResourceAsStream("/problems/"+Test.names[problem_index][0]);
						Problem p = new Problem();
						p.parse(input);
						try {
							input.close();
						} catch (Exception e) {
						}
						Cozum c = new Cozum();
//						p.goster();
						Suru suru = new Suru(c);
						suru.init(p, parcacik_sayisi, -4, 4, 0, 4);
						current_suru = suru;
						suru.solve(max_iterasyon, zaman_kisiti);
						c.yayilma_zaman = p.yayilma_zamani;
						c.en_iyi_cozum_zamani = p.en_iyi_cozum_zamani;
						c.p = p;
						PSOScreen.instance().pso_cozum_ekle(c);
						son = p;
					}
					DisplayManager.update(son);
					PSOScreen.instance().pso_stopped();
					running = false;	
				}
			};
			t.start();
		}
	}
	public void durdur(){
		running = false;
		if (current_suru!= null){
			current_suru.stop();
			current_suru = null;
		}
	}
}
