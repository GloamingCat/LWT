package lwt.dataserialization;

public interface LSerializer {

	boolean save();
	boolean load();
	boolean isDataFolder(String path);
	
}
