package lwt.dataserialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public interface LSerializer {

	public static Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	boolean save();
	boolean load();
	
}
