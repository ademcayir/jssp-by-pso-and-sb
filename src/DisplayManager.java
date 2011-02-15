import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.JFrame;


public class DisplayManager {
	private static JFrame frame;
	private static ScheduleComponent scheduleComponent;
	public static void show(Problem problem){
		if (frame == null){
			frame = new JFrame();
			scheduleComponent = new ScheduleComponent();
			frame.add(scheduleComponent,BorderLayout.CENTER);
			frame.setSize(600, 400);
		}
		scheduleComponent.update(problem);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
class ScheduleComponent extends Component {
	
	private AltIsFrame alt_isler[];
	private double x_carpan;
	private double y_carpan;
	private int yayilma_zamani;
	private int makine_sayisi;
	private static Color colors[]={
			Color.black,	
			Color.red,	
			Color.yellow,	
			Color.blue,	
			Color.pink,	
			Color.CYAN,	
			Color.green,
		};
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		int w = getWidth();
		int h = getHeight();
		g.fillRect(0, 0, w, h);
		if (yayilma_zamani != 0){
			x_carpan = getWidth()/((double)yayilma_zamani);
			y_carpan = getHeight() / ((double)makine_sayisi);
			for (int i = 0; alt_isler != null && i < alt_isler.length; i++) {
				if (alt_isler[i] != null){
					alt_isler[i].draw(g);
				}
			}
		}
	}
	public void update(Problem problem){
		alt_isler = new AltIsFrame[problem.makine_sayisi * problem.is_sayisi];
		int counter = 0;
		yayilma_zamani = problem.yayilma_zamani;
		makine_sayisi = problem.makine_sayisi;
		for (int i = 0; i < problem.isler.length; i++) {
			Is is = problem.isler[i];
			AltIs tmp = is.baslangic;
			while (tmp.is_icin_sonrasi != null){
				tmp = tmp.is_icin_sonrasi;
				alt_isler[counter] = new AltIsFrame();
				alt_isler[counter].baslangic = tmp.baslangic_zamani;
				alt_isler[counter].sure = tmp.sure;
				alt_isler[counter].makine = tmp.makine;
				alt_isler[counter].is = tmp.parent.is_no;
				counter++;
			}
		}
		invalidate();
		repaint();
	}
	class AltIsFrame {
		int is;
		int baslangic;
		int sure;
		int makine;
		private Polygon p1 = new Polygon(new int[]{0,0,0,0},new int[]{0,0,0,0},4);
		private Polygon p2 = new Polygon(new int[]{0,0,0,0},new int[]{0,0,0,0},4);
		void draw(Graphics g){
			int x = (int)(baslangic * x_carpan);
			int y = (int)(makine * y_carpan);
			int w = (int)(sure * x_carpan);
			int h = (int)y_carpan;
			
			g.setClip(x, y, w, h);	
			if (is < colors.length){
				g.setColor(colors[is]);
				g.fillRect(x, y, w, h);
			} else {
				Color c1 = colors[is%colors.length];
				Color c2 = colors[(is-1)%colors.length];
				int step_size = h/6;
				if (step_size <= 0){
					step_size = 1;
				}
				int target = w;
				if (h > w){
					target = h;
				}
				int cur = 0;
				for (int i = 0; cur < target ; i++) {
					if (i%2==0){
						g.setColor(c1);
					} else {
						g.setColor(c2);
					}
					if (is % 2 == 0){
						p1.xpoints[0] = x + cur;
						p1.ypoints[0] = y;
						
						p1.xpoints[1] = x + cur + step_size;
						p1.ypoints[1] = y;
						
						p1.xpoints[2] = x;
						p1.ypoints[2] = y+cur+step_size;
						
						p1.xpoints[3] = x;
						p1.ypoints[3] = y+cur;
						
						p2.xpoints[0] = x+target;
						p2.ypoints[0] = y+cur;
						
						p2.xpoints[1] = x+target;
						p2.ypoints[1] = y+cur+step_size;
						
						p2.xpoints[2] = x+cur+step_size;
						p2.ypoints[2] = y+target;
						
						p2.xpoints[3] = x+cur;
						p2.ypoints[3] = y+target;
						
						
					} else {
						
						p1.xpoints[0] = x + cur;
						p1.ypoints[0] = y;
						
						p1.xpoints[1] = x + cur + step_size;
						p1.ypoints[1] = y;
						
						p1.xpoints[2] = x+target;
						p1.ypoints[2] = y+target - cur - step_size;
						
						p1.xpoints[3] = x+target;
						p1.ypoints[3] = y+target - cur;
						
						
						p2.xpoints[0] = x;
						p2.ypoints[0] = y+cur;
						
						p2.xpoints[1] = x;
						p2.ypoints[1] = y+cur+step_size;
						
						p2.xpoints[2] = x + target - cur - step_size;
						p2.ypoints[2] = y+target;
						
						p2.xpoints[3] = x + target - cur;
						p2.ypoints[3] = y+target;
						
					}
					g.fillPolygon(p1);
					g.fillPolygon(p2);
					cur += step_size;
				}
			}
		}
	}
}