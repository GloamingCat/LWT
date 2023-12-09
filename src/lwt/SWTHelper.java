package lwt;

import java.util.HashMap;

import org.eclipse.swt.SWT;

public class SWTHelper {
	
	public static final HashMap<String, Integer> accelerators = initAccelerators();
	
	private static HashMap<String, Integer> initAccelerators() {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("F1", SWT.F1);
		map.put("F2", SWT.F2);
		map.put("F3", SWT.F3);
		map.put("F4", SWT.F4);
		map.put("F5", SWT.F5);
		return map;
	}
	
}
