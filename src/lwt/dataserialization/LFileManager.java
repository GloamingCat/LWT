package lwt.dataserialization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

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

	public static String load(String path) {
		path = path.replace("\\", File.separator);
		path = path.replace("/", File.separator);
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String allLines = "";
			for(String line = br.readLine(); line != null; line = br.readLine()) {
				allLines += line;
			}
			br.close();
			return allLines;
		} catch(Exception e) {
			System.out.println("couldn't load: " + path);
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean save(String path, String content) {
		path = path.replace("\\", File.separator);
		path = path.replace("/", File.separator);
		try {
			File file = new File(path);
			file.getParentFile().mkdirs();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		    writer.write(content);
		    writer.newLine();
		    writer.close();
		    return true;
		} catch(Exception e) {
			System.out.println("couldn't save: " + path);
			return false;
		}
	}
	
}
