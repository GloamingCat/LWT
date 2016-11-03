package lwt.dataserialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class LFileManager {
	
	public static String appDataPath(String applicationName) {
		return System.getenv("APPDATA") + "\\" + applicationName + "\\";
	}
	
	public static String applicationPath() {
		try {
			String path = new File(".").getCanonicalPath();
			return path + "/";
		} catch(Exception e) {
			return "";
		}
	}

	public static byte[] load(String path) {
		path = path.replace("\\", File.separator);
		path = path.replace("/", File.separator);
		try {
			File file = new File(path);
			FileInputStream reader = new FileInputStream(file);
			byte[] array = new byte[(int) file.length()];
			reader.read(array);
			reader.close();
			return array;
		} catch(Exception e) {
			System.out.println("couldn't load: " + path);
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean save(String path, byte[] content) {
		path = path.replace("\\", File.separator);
		path = path.replace("/", File.separator);
		try {
			File file = new File(path);
			file.getParentFile().mkdirs();
			FileOutputStream writer = new FileOutputStream(file);
		    writer.write(content);
		    writer.close();
		    return true;
		} catch(Exception e) {
			System.out.println("couldn't save: " + path);
			return false;
		}
	}
	
}
