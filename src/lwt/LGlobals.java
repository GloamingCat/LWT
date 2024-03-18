package lwt;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.widgets.Display;

public class LGlobals {
	
	public static final Clipboard clipboard = initClipboard();

	private static final HashMap<String, Integer> accelerators = initAccelerators();
	private static HashMap<String, Integer> initAccelerators() {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("f1", SWT.F1);
		map.put("f2", SWT.F2);
		map.put("f3", SWT.F3);
		map.put("f4", SWT.F4);
		map.put("f5", SWT.F5);
		map.put("ctrl", SWT.MOD1);
		map.put("alt", SWT.ALT);
		map.put("del", 0 | SWT.DEL);
		map.put("space", 0 | SWT.SPACE);
		map.put("enter", 0 | SWT.CR);
		for (char c = 'a'; c <= 'z'; c++)
			map.put("" + c, 0 | c);
		for (char c = '0'; c <= '9'; c++)
			map.put("" + c, 0 | c);
		return map;
	}
	
	public static int getAccelerator(String[] keys) {
		int k = 0;
		for (String mod : keys) {
			mod = mod.trim().toLowerCase();
			if (mod.charAt(0) == '&')
				mod = "" + mod.charAt(1);
			k = k | accelerators.get(mod);
		}
		return k;
	}
	
	private static Clipboard initClipboard() {
		return new Clipboard(Display.getDefault());
	}
	
}
