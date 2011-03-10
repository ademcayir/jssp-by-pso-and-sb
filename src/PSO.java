import java.io.InputStream;



public class PSO {
	
	private static final int BENCHMARK_DENEME_SAYISI = 5;
	private static final int BENCHMARK_DENEME_SURESI = 10*60*1000;
	
	
	
	private static PSO instance;
	public static final int PSO = 0;
	public static final int PSO_SB = 1;
	public static final int YBS = 2;
	public static PSO instance(){
		if (instance == null){
			instance = new PSO();
		}
		return instance;
	}
	private boolean dosyaya_yaz;
	private int cozum_yontemi;
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
	public void setCozumYontemi(int yontem){
		this.cozum_yontemi = yontem;
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
	public int getCozumYontemi(){
		return cozum_yontemi;
	}
	public void benchmark(){
		if (!running){
			running = true;
			Thread t = new Thread(){
				public void run() {
					Problem son = null;
					for (int j = 1; j < Test.names.length; j++) {
						for (int i = 0; running && i < BENCHMARK_DENEME_SAYISI; i++) {
							try {
								System.out.println("benchmark:"+j+","+i);
								InputStream input = PSOScreen.instance().getClass().getResourceAsStream("/problems/"+Test.names[j][0]);
								Problem p = new Problem();
								p.parse(input);
								try {
									input.close();
								} catch (Exception e) {
								}
								Logger.setName(p.name);
								Logger.addLine("Problem: "+Test.names[j][0]);
								Logger.addLine("Cozum Zamani: "+System.currentTimeMillis());
								Logger.addLine("Cozum Yontemi: "+cozum_yontemi);
								Logger.addLine("Parcacik Sayisi: "+parcacik_sayisi);
								Cozum c = new Cozum();
								Suru suru = new Suru(c);
								suru.init(p, parcacik_sayisi, -4, 4, 0, 4);
								current_suru = suru;
								suru.solve(-1, BENCHMARK_DENEME_SURESI);
								c.yayilma_zaman = p.yayilma_zamani;
								c.en_iyi_cozum_zamani = p.en_iyi_cozum_zamani;
								c.p = p;
								PSOScreen.instance().pso_cozum_ekle(c);
								son = p;
								suru.benchmark_log();
								Logger.addLine();
								Logger.addLine("Sonuc");
								Logger.addLine("Cozum: "+Algorithms.getArrayString(suru.getBest()));
								Logger.addLine("Degerler: "+Algorithms.getArrayString(suru.getBestVals()));
								Logger.addLine("Yayilma Zamani: "+suru.getBestTft());
								Logger.addLine("Iterasyon Sayisi: "+c.iterasyon_sayisi);
								Logger.addLine("En Iyi Cozum Zamani: "+c.en_iyi_cozum_zamani);
								Logger.addLine("En Iyi Cozum Iterasyon Sayisi: "+p.en_iyi_cozum_iterasyon_sayisi);
								Logger.addLine("Toplam Sure: "+c.toplam_zaman);
								Logger.addLine();
								Logger.addLine("Bilinen En Iyi Zaman: "+Test.names[j][1]);
								if (Test.names[j].length > 2){
									Logger.addLine("AIS Ile Iyi Zaman: "+Test.names[j][2]);
									Logger.addLine("GRASP Ile Iyi Zaman: "+Test.names[j][3]);
								}
								int tft = suru.getBestTft();
								int target_tft = 0;
								DisplayManager.update(son);
								try {
									target_tft = Integer.parseInt(Test.names[j][1]);
								} catch (Exception e) {
								}
								Logger.addLine();
								if (target_tft > 0){
									Logger.addLine("Hata Payi: "+((double)(tft - target_tft)/(double)target_tft));
									if (Test.names[j].length > 2){
										int tft_ais = 0;
										try {
											tft_ais = Integer.parseInt(Test.names[j][2]);
										} catch (Exception e) {
										}
										
										int tft_grasp = 0;
										try {
											tft_grasp = Integer.parseInt(Test.names[j][3]);
										} catch (Exception e) {
										}
										Logger.addLine("Hata Payi(AIS): "+((double)(tft_ais - target_tft)/(double)target_tft));
										Logger.addLine("Hata Payi(GRASP): "+((double)(tft_grasp - target_tft)/(double)target_tft));
									}
								}
								Logger.renameTo(p.name+"."+suru.getBestTft());
								if (target_tft != 0 && tft <= target_tft){
									break;
								}
							} catch (Exception e) {
								Logger.addLine();
								Logger.addLine("HATA: "+e);
								Logger.close();
							}
						}
						PSOScreen.instance().pso_stopped();
					}
					running = false;	
				}
			};
			t.start();
		}
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
						suru.solve(max_iterasyon, zaman_kisiti * 60 * 1000);
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
