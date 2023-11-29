package lwt;

import java.util.HashMap;

import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

public class LColor {
	
	public int red;
	public int green;
	public int blue;
	public int alpha;
	
	public LColor(int r, int b, int g, int a) {
		red = r;
		green = g;
		blue = b;
		alpha = a;
	}
	
	public LColor(int r, int b, int g) {
		this(r, g, b, 255);
	}
	
	public LColor() {
		red = 255;
		green = 255;
		blue = 255;
		alpha = 255;
	}
	
	public Color convert() {
		return SWTResourceManager.getColor(red, green, blue);
	}
	
	//////////////////////////////////////////////////
	// {{ Map
	
	private static HashMap<String, LColor> colorMap = new HashMap<String, LColor>();
	
	public static void setColor(String name, int r, int g, int b, int a) {
		colorMap.put(name, new LColor(r, g, b, a));
	}
	
	public static LColor getColor(String name) {
		return colorMap.get(name);
	}
	
	// }}
	
}
