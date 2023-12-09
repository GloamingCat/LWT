package lwt.graphics;

import org.eclipse.swt.graphics.Rectangle;

public class LRect {
	
	public int x;
	public int y;
	public int width;
	public int height;
	
	public LRect() {}
	
	public LRect(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		width = w;
		height = h;
	}
	
	public LRect(Rectangle rect) {
		this(rect.x, rect.y, rect.width, rect.height);
	}
	
	public LRect clone() {
		return new LRect(x, y, width, height);
	}

}
