import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;


public class DisplayManager {
	private static ScheduleComponent scheduleComponent;
	public static void update(Problem problem){
		scheduleComponent.update(problem);
	}
	public static Container getScheduleScreen(){
		if (scheduleComponent== null){
			scheduleComponent = new ScheduleComponent();
		}
		return scheduleComponent;
	}
}
class ScheduleComponent extends Container {
	
	private AltIsFrame alt_isler[];
	private double x_carpan;
	private double y_carpan;
	private int yayilma_zamani;
	private int makine_sayisi;
	private int draw_route=2;
	private Point draw_points[]; 
	public ScheduleComponent() {
		setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 0;
		g.weightx = 1;
		g.weighty = 5;
		g.fill = GridBagConstraints.BOTH;
		add(table,g);
		
		g = new GridBagConstraints();
		g.gridx = 0;
		g.gridy = 1;
		g.weightx = 1;
		g.weighty = 1;
		g.fill = GridBagConstraints.BOTH;
		add(info,g);
	}
	private static Color colors[]={
			Color.black,	
			Color.red,	
			Color.yellow,	
			Color.blue,	
			Color.pink,	
			Color.CYAN,	
			Color.green,
		};
	private Component info = new Component(){
		int current_x=-1,current_y=-1;
		{
			
			addMouseMotionListener(new MouseMotionListener(){
				public void mouseMoved(MouseEvent e) {
					current_x = e.getX();
					current_y = e.getY();
					ScheduleComponent.this.repaint();
				};
				public void mouseDragged(MouseEvent e) {
					current_x = e.getX();
					current_y = e.getY();
					ScheduleComponent.this.repaint();
				}
			});
			addMouseListener(new MouseAdapter(){
				public void mouseExited(MouseEvent e) {
					current_x = -1;
					current_y = -1;
					ScheduleComponent.this.repaint();
				}
			});
			
		}
		public void paint(Graphics g) {
			g.setColor(Color.white);
			g.fillRect(0, 0, getWidth(), getHeight());
			if (alt_isler == null){
				return;
			}
			Graphics2D g2 = (Graphics2D)g;
			Font f = g.getFont();
			int is_sayisi = alt_isler.length/makine_sayisi;
			int satir = ((is_sayisi-1)/10)+1;
			String txt = (is_sayisi+1)+". İş";
			Rectangle2D r2 = f.getStringBounds(txt,0 , txt.length(), g2.getFontRenderContext());
			int t_w = (int)r2.getWidth()+10;
			int cell_w = getWidth()/10 - t_w;
			int cell_h = getHeight()/(satir) - 4;
			int cur_x = 2;
			int cur_y = 2;
			g.setColor(Color.black);
			g.drawLine(0, 1, getWidth(), 1);
			draw_route = -1;
			for (int i = 0; i < satir; i++) {
				for (int j = 0; j < 10; j++) {
					int index = i*10+j;
					if (index >= is_sayisi){
						break;
					}
					AltIsFrame a = new AltIsFrame();
					a.is = index;
					int c_h = cell_h;
					if (c_h > (r2.getHeight()*5)/3){
						c_h = (int)(r2.getHeight()*5)/3;
					}
					a.draw(g, cur_x, cur_y, cell_w, c_h);
					if (current_x > cur_x && current_x < cur_x+cell_w && 
							current_y > cur_y && current_y < cur_y + c_h
					){
						draw_route = index;
					}
					g.setColor(Color.black);
					g.setClip(0, 0, getWidth(), getHeight());
					g.drawString((index+1)+". İş", cur_x+cell_w+2, cur_y+(int)r2.getHeight());
					cur_x += t_w+cell_w;
				}
				cur_y += cell_h;
				cur_x = 2;
			}
			
			if(draw_route != -1){
				
				int yayilma_zamani = -1;
				int baslama_zamani = -1;
				int total = 0;
				for (int i = 0; i < alt_isler.length; i++) {
					if (alt_isler[i].is == draw_route){
						if (yayilma_zamani == -1){
							yayilma_zamani = alt_isler[i].baslangic+alt_isler[i].sure;
						}
						if (baslama_zamani == -1){
							baslama_zamani = alt_isler[i].baslangic;
						}
						
						if (alt_isler[i].baslangic < baslama_zamani){
							baslama_zamani = alt_isler[i].baslangic; 
						}
						if (alt_isler[i].baslangic + alt_isler[i].sure > yayilma_zamani){
							yayilma_zamani = alt_isler[i].baslangic + alt_isler[i].sure;	
						}
						total += alt_isler[i].sure;
					}
				}
				int h = (int)r2.getHeight()*4 + 10;
				int row_h = (int)r2.getHeight();
				int stary_y = current_y;
				String row1 = "Yayilma Zamanı: "+yayilma_zamani;
				String row2 = "Başlama Zamanı: "+baslama_zamani;
				String row3 = "Gecikme: "+(yayilma_zamani-baslama_zamani-total);
				
				int w = (int)f.getStringBounds(row2, 0,row2.length(),g2.getFontRenderContext()).getWidth() + 30;
				int start_x = current_x+10;
				if (stary_y + h > getHeight()){
					stary_y -= h;
				}
				if (start_x + w > getWidth()){
					start_x -= w + 20;
				}
				if (stary_y < 0){
					stary_y = 0;
				}
				g.setColor(Color.white);
				g.fillRect(start_x, stary_y, w, h);
				g.setColor(Color.black);
				g.drawRect(start_x, stary_y, w, h);
				g.drawString((draw_route+1)+". İş ", start_x+3, stary_y+3+row_h);
				g.drawString(row1, start_x+3, stary_y+3+row_h*2);
				g.drawString(row2, start_x+3, stary_y+3+row_h*3);
				g.drawString(row3, start_x+3, stary_y+3+row_h*4);
				
				
			}
		};
	};
	private Component table = new Component(){
		private boolean rows[];
		private Polygon tmp = new Polygon(new int[4],new int[4],4);
		public void paint(Graphics g) {
			int w = getWidth();
			int h = getHeight();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, w, h);
			if (yayilma_zamani == 0){
				return;
			}
			Font font = g.getFont();
			Graphics2D g2 = (Graphics2D)g;
			String txt = (makine_sayisi+1)+". makine";
			Rectangle2D r = font.getStringBounds(txt, 0, txt.length(), g2.getFontRenderContext());
			
			if (rows == null || rows.length != makine_sayisi){
				rows = new boolean[makine_sayisi];
			} else {
				for (int i = 0; i < rows.length; i++) {
					rows[i] = false;
				}
			}
			for (int i = 0; i < makine_sayisi; i++) {
				g.setColor(Color.black);
				g.drawString((i+1)+". makine", 5, (h/makine_sayisi)*(i)+(h/makine_sayisi)/2 );
			}
			int translate = (int)r.getWidth()+10;
			w -= translate;
			
			if (yayilma_zamani != 0){
				x_carpan = w/((double)yayilma_zamani);
				y_carpan = h / ((double)makine_sayisi);
				for (int i = 0; alt_isler != null && i < alt_isler.length; i++) {
					if (alt_isler[i] != null){
						g.translate(translate, 0);
						alt_isler[i].draw(g);
						g.translate(-translate, 0);
						g.setColor(Color.black);
						g.setClip(0,0,getWidth(),getHeight());
						int y = (int)(alt_isler[i].makine * y_carpan);
						g.drawLine(0, y, getWidth(), y);
					}
				}
				if (draw_route != -1){
					for (int j = 0; j < draw_points.length-1; j++) {
						for (int i = 0; i < draw_points.length-1-j; i++) {
							if (draw_points[i].x > draw_points[i+1].x){
								Point tmp = draw_points[i];
								draw_points[i] = draw_points[i+1];
								draw_points[i+1]=tmp;
							}
						}
					}
					
					for (int j = 0; j < draw_points.length-1; j++) {
						int x_add;
						if (draw_points[j].y > draw_points[j+1].y){
							x_add = -1;
						} else {
							x_add = +1;
						}
						
						
						tmp.xpoints[0] = draw_points[j].x+translate-x_add;
						tmp.ypoints[0] = draw_points[j].y+1;
						
						tmp.xpoints[1] = draw_points[j].x+translate+x_add;
						tmp.ypoints[1] = draw_points[j].y-1;
						
						tmp.xpoints[2] = draw_points[j+1].x+translate+x_add;
						tmp.ypoints[2] = draw_points[j+1].y-1;
						
						tmp.xpoints[3] = draw_points[j+1].x+translate-x_add;
						tmp.ypoints[3] = draw_points[j+1].y+1;
						g.setColor(Color.black);
						g.fillPolygon(tmp);
						g.setColor(Color.white);
						g.drawLine(draw_points[j].x+translate, draw_points[j].y, draw_points[j+1].x+translate, draw_points[j+1].y);
						
					}
				}
			}
			
		}
	};
	
	
	public void update(Problem problem){
		alt_isler = new AltIsFrame[problem.makine_sayisi * problem.is_sayisi];
		int counter = 0;
		yayilma_zamani = problem.yayilma_zamani;
		makine_sayisi = problem.makine_sayisi;
		draw_points = new Point[makine_sayisi];
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
		for (int i = 0; i < draw_points.length; i++) {
			draw_points[i] = new Point(0,0);
		}
		table.invalidate();
		table.repaint();
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
			draw(g,x,y,w,h);
			if (draw_route == is){
				draw_points[makine].setLocation(x+w/2,y+h/2);
			}
		}
		
		void draw(Graphics g,int x,int y,int w,int h){
			
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