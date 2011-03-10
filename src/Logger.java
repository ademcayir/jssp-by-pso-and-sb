import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


public class Logger {
	
	private static OutputStream output;
	private static File file;
	private static final String ROOT = "c:/log/";
	public static void setName(String str){
		System.out.println("Log.name:" +str);
		try {
			if (output != null){
				output.close();
			}
			file = new File(ROOT+str+"-"+System.currentTimeMillis()+".txt");
			file.createNewFile();
			output = new FileOutputStream(file);
		} catch (Exception e) {
		
		}
	}
	public static void renameTo(String str){
		try {
			File f2 = new File(ROOT+str+".txt");
			int c = 1;
			while (f2.exists()){
				f2 = new File(ROOT+str+"."+(c++)+".txt");
			}
			System.out.println("f2::"+f2);
			if (output != null){
				output.close();
				output = null;
			}
			file.renameTo(f2);
		} catch (Exception e) {
		}
	}
	public static void close(){
		try {
			output.close();
		} catch (Exception e) {
		}
		System.out.println("file closed:"+file);
	}
	public static void addLine(){
		addLine("------------------------------------");
	}
	public static void addLine(String str){
		if (output != null){
			try {
				output.write(str.getBytes());
				output.write('\r');
				output.write('\n');
			} catch (Exception e) {
			}
		}
	}
	
	
}
