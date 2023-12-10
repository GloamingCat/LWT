package lwt;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.widgets.Display;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public class LGlobals {
	
	public static final HashMap<String, Integer> accelerators = initAccelerators();
	public static final Clipboard clipboard = initClipboard();
	public static final Gson gson = new GsonBuilder().create();
	public static final Gson prettyGson = new GsonBuilder().
			setPrettyPrinting().
			disableHtmlEscaping().
			create();
	public static final JsonParser json = new JsonParser();
	
	private static HashMap<String, Integer> initAccelerators() {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("F1", SWT.F1);
		map.put("F2", SWT.F2);
		map.put("F3", SWT.F3);
		map.put("F4", SWT.F4);
		map.put("F5", SWT.F5);
		return map;
	}
	
	private static Clipboard initClipboard() {
		return new Clipboard(Display.getDefault());
	}
	
}
