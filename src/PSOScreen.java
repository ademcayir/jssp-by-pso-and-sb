import java.awt.Color; 
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class PSOScreen extends JApplet {
	
	
	
	private JComboBox genel_problemler;
	private JLabel makine_sayisi_;
	private JLabel is_sayisi_;
	private JComboBox parcacik_sayisi;
	private JComboBox cozum_yontemi; 
	private JComboBox zaman_kisiti ;
	private JComboBox iterasyon_sayisi ;
	private JComboBox deneme_sayisi;
	private JButton coz;
	private JButton benchmark;
	private JLabel makespan;
	private JLabel current_iterasyon_sayisi;
	private JLabel current_time;
	private JLabel en_iyi_cozume_ulasma_zamani;
	private JLabel bilinen_en_iyi_cozum_yayilma_zamani;
	private JLabel problem_ismi;
	private JButton durdur;
	private JComboBox cozum_kumesi;
	private JCheckBox dosyaya_yaz;
	private Gauge bar;
	private static PSOScreen instance;
	public static PSOScreen instance(){
		return instance;
	}
	public PSOScreen() {
		
		instance = this;
		Container con1 = getContentPane();
		con1.setLayout(new GridBagLayout());
		GridBagConstraints g;
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 0;
		g.gridheight = 2;
		g.fill = GridBagConstraints.BOTH;
		con1.add(ayarlar(),g);
		
		g = new GridBagConstraints();
		g.gridx = 1;
		g.gridy = 0;
		g.weightx = 1;
		g.fill = GridBagConstraints.HORIZONTAL;
		con1.add(yonetim(),g);
		
		g = new GridBagConstraints();
		g.gridx = 1;
		g.gridy = 1;
		g.weightx = 1;
		g.weighty = 1;
		g.fill = GridBagConstraints.BOTH;
		
		con1.add(DisplayManager.getScheduleScreen(),g);
		
		setVisible(true);
		setSize(1000,800);
	}
	public void pso_started(){
		coz.setEnabled(false);
		durdur.setEnabled(true);
	}
	public void pso_stopped(){
		coz.setEnabled(true);
		durdur.setEnabled(false);
	}
	private JPanel yonetim(){
		GridBagConstraints g;
		JPanel yonetim = new JPanel();
		yonetim.setLayout(new GridBagLayout());
		coz = new JButton("Çözümü Başlat");
		durdur = new JButton("Çözümü Durdur");
		bar = new Gauge();
		durdur.setEnabled(false);
		coz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				set_pso();
				PSO.instance().baslat();
				coz.setEnabled(false);
				durdur.setEnabled(true);
			}
		});
		benchmark = new JButton("Benchmark");
		benchmark.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				set_pso();
				PSO.instance().benchmark();
			}
		});
		
		
		durdur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PSO.instance().durdur();
				coz.setEnabled(true);
				durdur.setEnabled(false);
			}
		});
		cozum_kumesi = new JComboBox();
		cozum_kumesi.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				cozum_goster();
			}
		});
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 0;
		g.weightx = 1;
		yonetim.add(coz,g);
		
		g = new GridBagConstraints();
		g.gridx = 1;
		g.gridy = 0;
		g.weightx = 1;
		yonetim.add(durdur,g);
		
		g = new GridBagConstraints();
		g.gridx = 2;
		g.gridy = 0;
		g.weightx = 1;
		yonetim.add(benchmark,g);
		
		g = new GridBagConstraints();
		g.gridx = 3;
		g.gridy = 0;
		g.weightx = 1;
		yonetim.add(cozum_kumesi,g);
		
		
		
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 1;
		g.weightx = 1;
		g.gridwidth = 3;
		g.fill = GridBagConstraints.HORIZONTAL;
		problem_ismi = new JLabel("Problem: ");
		yonetim.add(problem_ismi,g);
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 2;
		g.weightx = 1;
		g.gridwidth = 3;
		g.fill = GridBagConstraints.HORIZONTAL;
		makespan = new JLabel("Yayılma Zamanı: ");
		yonetim.add(makespan,g);
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 3;
		g.weightx = 1;
		g.fill = GridBagConstraints.HORIZONTAL;
		g.gridwidth = 3;
		current_iterasyon_sayisi = new JLabel("İterasyon Sayısı: ");
		yonetim.add(current_iterasyon_sayisi,g);
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 4;
		g.weightx = 1;
		g.fill = GridBagConstraints.HORIZONTAL;
		g.gridwidth = 3;
		current_time = new JLabel("Zaman: ");
		yonetim.add(current_time,g);
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 5;
		g.weightx = 1;
		g.fill = GridBagConstraints.HORIZONTAL;
		g.gridwidth = 3;
		en_iyi_cozume_ulasma_zamani = new JLabel("En iyi çözüme ulaşma zamanı: ");
		yonetim.add(en_iyi_cozume_ulasma_zamani,g);
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 6;
		g.weightx = 1;
		g.fill = GridBagConstraints.HORIZONTAL;
		g.gridwidth = 3;
		bilinen_en_iyi_cozum_yayilma_zamani = new JLabel("Bilinen En İyi Yayılma Zamanı: ");
		yonetim.add(bilinen_en_iyi_cozum_yayilma_zamani,g);
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 7;
		g.gridwidth = 2;
		g.weightx = 1;
		g.gridwidth = 3;
		g.fill = GridBagConstraints.HORIZONTAL;
		
		yonetim.add(bar,g);
		return yonetim;
	}
	private void cozum_goster(){
		if (cozum_kumesi.getItemCount() > 0){
			Cozum i = (Cozum)cozum_kumesi.getSelectedItem();
			pso_set_current_iterasyon(i.iterasyon_sayisi);
			pso_set_current_time(i.toplam_zaman);
			pso_set_en_iyi_ulasim_zamani(i.en_iyi_cozum_zamani);
			pso_set_makespan(i.yayilma_zaman);
			DisplayManager.update(i.p);
		}
	}
	private JPanel ayarlar(){
		
		JPanel ayarlar= new JPanel();
		ayarlar.setLayout(new GridBagLayout());
		GridBagConstraints g;
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 0;
		g.fill = GridBagConstraints.HORIZONTAL;
		ayarlar.add(new JLabel("Problem İsmi"),g);
		
		genel_problemler = new JComboBox();
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 1;
		g.fill = GridBagConstraints.HORIZONTAL;
		ayarlar.add(genel_problemler,g);
		for (int i = 0; i < Test.names.length; i++) {
			String name = Test.names[i][0];
			name = name.substring("instance_".length());
			if (i == 0){
				name = "Örnek Problem";
			} else {
				name = "Problem "+name.substring(0,name.length()-4);
			}
			genel_problemler.addItem(new ComboItem(name,i));
		}
		genel_problemler.setSelectedIndex(1);
		genel_problemler.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				problem_isimlerini_tekrar_belirle();
			}
		});
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 2;
		g.fill = GridBagConstraints.HORIZONTAL;
		ayarlar.add(new JLabel("Makine Sayısı"),g);
		
		makine_sayisi_ = new JLabel(" ");
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 3;
		g.fill = GridBagConstraints.HORIZONTAL;
		ayarlar.add(makine_sayisi_,g);
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 4;
		g.fill = GridBagConstraints.HORIZONTAL;
		ayarlar.add(new JLabel("İş Sayısı"),g);
		
		is_sayisi_= new JLabel(" ");
		g.gridx = 0;
		g.gridy = 5;
		g.fill = GridBagConstraints.HORIZONTAL;
		ayarlar.add(is_sayisi_,g);
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 6;
		g.fill = GridBagConstraints.HORIZONTAL;
		ayarlar.add(new JLabel("Parçacık Sayısı"),g);
		
		parcacik_sayisi = new JComboBox();
		parcacik_sayisi.addItem(new ComboItem("10 Parçacık",10));
		parcacik_sayisi.addItem(new ComboItem("20 Parçacık",20));
		parcacik_sayisi.addItem(new ComboItem("30 Parçacık",30));
		parcacik_sayisi.addItem(new ComboItem("50 Parçacık",50));

		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 7;
		g.fill = GridBagConstraints.HORIZONTAL;
		ayarlar.add(parcacik_sayisi,g);
	
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 8;
		g.fill = GridBagConstraints.HORIZONTAL;
		ayarlar.add(new JLabel("Çözüm Yöntemi"),g);
		
		cozum_yontemi = new JComboBox();
		cozum_yontemi.addItem(new ComboItem("PSO",0));
		cozum_yontemi.addItem(new ComboItem("PSO + SB",1));
		cozum_yontemi.addItem(new ComboItem("YBS",2));

		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 9;
		g.fill = GridBagConstraints.HORIZONTAL;
		ayarlar.add(cozum_yontemi,g);
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 10;
		g.fill = GridBagConstraints.HORIZONTAL;
		ayarlar.add(new JLabel("Zaman Kısıtı"),g);
		
		zaman_kisiti = new JComboBox();
		zaman_kisiti.addItem(new ComboItem("Kısıt Yok",-1));
		zaman_kisiti.addItem(new ComboItem("1 Dakika",1));
		zaman_kisiti.addItem(new ComboItem("2 Dakika",2));
		zaman_kisiti.addItem(new ComboItem("5 Dakika",5));
		zaman_kisiti.addItem(new ComboItem("10 Dakika",10));
		zaman_kisiti.setSelectedIndex(1);
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 11;
		g.fill = GridBagConstraints.HORIZONTAL;
		ayarlar.add(zaman_kisiti,g);
		
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 12;
		g.fill = GridBagConstraints.HORIZONTAL;
		ayarlar.add(new JLabel("İterasyon Kısıtı"),g);
		
		iterasyon_sayisi = new JComboBox();
		iterasyon_sayisi.addItem(new ComboItem("Kısıt Yok",-1));
		iterasyon_sayisi.addItem(new ComboItem("2 İterasyon",2));
		iterasyon_sayisi.addItem(new ComboItem("10 İterasyon",10));
		iterasyon_sayisi.addItem(new ComboItem("50 İterasyon",50));
		iterasyon_sayisi.addItem(new ComboItem("100 İterasyon",100));
		iterasyon_sayisi.addItem(new ComboItem("1000 İterasyon",1000));
		iterasyon_sayisi.addItem(new ComboItem("10000 İterasyon",10000));
		iterasyon_sayisi.addItem(new ComboItem("50000 İterasyon",50000));
		iterasyon_sayisi.setSelectedIndex(5);
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 13;
		g.fill = GridBagConstraints.HORIZONTAL;
		ayarlar.add(iterasyon_sayisi,g);
		
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 14;
		g.fill = GridBagConstraints.HORIZONTAL;
		ayarlar.add(new JLabel("Deneme Sayısı"),g);
		
		deneme_sayisi = new JComboBox();
		deneme_sayisi.addItem(new ComboItem("1 Deneme",1));
		deneme_sayisi.addItem(new ComboItem("5 Deneme",5));
		deneme_sayisi.addItem(new ComboItem("10 Deneme",10));
		deneme_sayisi.addItem(new ComboItem("20 Deneme",20));
		deneme_sayisi.setSelectedIndex(0);
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 15;
		g.fill = GridBagConstraints.HORIZONTAL;
		ayarlar.add(deneme_sayisi,g);
		
		
		dosyaya_yaz = new JCheckBox("Dosyaya Yaz");
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 16;
		g.fill = GridBagConstraints.HORIZONTAL;
		ayarlar.add(dosyaya_yaz,g);
		problem_isimlerini_tekrar_belirle();
		return ayarlar;
	}
	private void problem_isimlerini_tekrar_belirle(){
		String name = "/problems/"+Test.names[((ComboItem)genel_problemler.getSelectedItem()).option][0];
		InputStream input = instance.getClass().getResourceAsStream(name);
		Problem p = new Problem();
		p.parse(input);
		try {
			input.close();
		} catch (Exception e) {
		}
		makine_sayisi_.setText(p.makine_sayisi+" Makine");
		is_sayisi_.setText(p.is_sayisi+" İş");
	}
	
	public JLabel getLabel(Color c,String str){
		JLabel l = new JLabel(str);
		l.setOpaque(true);
		l.setBackground(c);
		return l;
	}
	public void init() {
	
	}
	public void pso_set_current_iterasyon(int m){
		current_iterasyon_sayisi.setText("İterasyon Sayısı: "+m);
	}
	public void pso_set_name(String name){
		problem_ismi.setText("Problem: "+name);
	}
	public void pso_set_en_iyi_ulasim_zamani(long m){
		en_iyi_cozume_ulasma_zamani.setText("En iyi çözüme ulaşma zamanı: "+m+" ms");
	}
	public void pso_cozum_ekle(Cozum cozum){
		int count = cozum_kumesi.getItemCount();
		cozum.label = ""+(count+1)+". Çözüm";
		cozum_kumesi.addItem(cozum);
		int min;
		int min_index;
		Cozum c = (Cozum)cozum_kumesi.getItemAt(0);
		min = c.yayilma_zaman;
		min_index = 0;
		for (int i = 1; i < cozum_kumesi.getItemCount(); i++) {
			c = (Cozum)cozum_kumesi.getItemAt(i);
			if (c.yayilma_zaman < min){
				min = c.yayilma_zaman ;
				min_index = i;
			}
		}
		
		for (int i = 0; i < cozum_kumesi.getItemCount(); i++) {
			c = (Cozum)cozum_kumesi.getItemAt(i);
			if (c.yayilma_zaman <= min ){
				c.label = ""+(i+1)+". Çözüm(En İyi)";
			} else  {
				c.label = ""+(i+1)+". Çözüm";
			}
		}
	}
	public void pso_set_current_time(long m){
		current_time.setText("Zaman: "+m+" ms");
	}
	public void pso_set_makespan(int m){
		makespan.setText("Yayılma Zamanı: "+m);
	}
	public void pso_set_current_percent(int percent){
		bar.setCurrentAmount(percent);
	}
	private void set_pso(){
		bar.setCurrentAmount(0);
		makespan.setText("Üretim Zamanı: ");
		current_iterasyon_sayisi.setText("İterasyon Sayısı: ");
		problem_ismi.setText("Problem: ");
		current_time.setText("Zaman: ");
		cozum_kumesi.removeAllItems();
		
		
		int index = ((ComboItem)genel_problemler.getSelectedItem()).option;
		if (Test.names[index][1].equals("")){
			bilinen_en_iyi_cozum_yayilma_zamani.setText("Bilinen En İyi Yayılma Zamanı: (Henüz Bilinmiyor)");
		} else {
			if (Test.names[index].length == 4){
				bilinen_en_iyi_cozum_yayilma_zamani.setText("Bilinen En İyi Yayılma Zamanı: "+Test.names[index][1]+", YBS İle: "+Test.names[index][2]+", GRASP İle: "+Test.names[index][3]);
			} else {
				bilinen_en_iyi_cozum_yayilma_zamani.setText("Bilinen En İyi Yayılma Zamanı: "+Test.names[index][1]);
			}
		}
		PSOScreen.instance().pso_set_name(((ComboItem)genel_problemler.getSelectedItem()).lbl);
		PSO.instance().setDosyayaYaz(dosyaya_yaz.isSelected());
		PSO.instance().setProblemIndex(index);
		PSO.instance().setMaksimum_iterasyon(((ComboItem)iterasyon_sayisi.getSelectedItem()).option);
		PSO.instance().setParcacik_sayisi(((ComboItem)parcacik_sayisi.getSelectedItem()).option);
		PSO.instance().setZaman_kisiti(((ComboItem)zaman_kisiti.getSelectedItem()).option);
		PSO.instance().setDenemeSayisi(((ComboItem)deneme_sayisi.getSelectedItem()).option);
		PSO.instance().setCozumYontemi(((ComboItem)cozum_yontemi.getSelectedItem()).option);
	}
	class ComboItem {
		String lbl;
		int option;
		public ComboItem(String lbl,int option) {
			this.lbl = lbl;
			this.option = option;
		}
		public String toString() {
			return lbl;
		}
	}
}

class Gauge extends JComponent {
    
	  // the current and total amounts that the gauge reperesents
	  int current = 0;
	  int total = 100;

	  // The preferred size of the gauge
	  int Height = 18;   // looks good
	  int Width  = 250;  // arbitrary 

	  /**
	   * Constructs a Gauge
	   */
	  public Gauge() {
	      this(Color.lightGray);
	  }

	  /**
	   * Constructs a that will be drawn uses the
	   * specified color.
	   *
	   * @gaugeColor the color of this Gauge
	   */
	  public Gauge(Color gaugeColor) {
	      setBackground(gaugeColor);
	  }

	  public void paint(Graphics g) {
	      int barWidth = (int) (((float)current/(float)total) * getSize().width);
	      g.setColor(getBackground());
	      g.fill3DRect(0, 0, barWidth, getSize().height-2, true);
	  }

	  public void setCurrentAmount(int Amount) {
	      current = Amount; 

	      // make sure we don't go over total
	      if(current > 100)
	       current = 100;

	      repaint();
	  }

	  public int getCurrentAmount() {
	      return current;
	  }

	  public int getTotalAmount() {
	      return total;
	  }

	  public Dimension getPreferredSize() {
	      return new Dimension(Width, Height);
	  }

	  public Dimension getMinimumSize() {
	      return new Dimension(Width, Height);
	  }
	}
