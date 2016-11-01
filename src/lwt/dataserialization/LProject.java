package lwt.dataserialization;

public interface LProject extends LSerializer {

	boolean hasChanges();
	boolean isDataFile(String path);
	
}
