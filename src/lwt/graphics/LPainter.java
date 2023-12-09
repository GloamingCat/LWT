package lwt.graphics;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.wb.swt.SWTResourceManager;

public abstract class LPainter {
	
	private GC gc;

	public LPainter() {
		gc = null;
	}
	
	public LPainter(GC gc) {
		this.gc = gc;
	}
	
	public LPainter(LTexture target) {
		this.gc = new GC(target.convert());
	}
	
	public void setGC(GC gc) {
		this.gc = gc;
	}

	//////////////////////////////////////////////////
	// {{ Draw
	
	public void drawRect(int x, int y, int w, int h) {
		gc.drawRectangle(x, y, w, h);
	}
	
	public void drawLine(int x1, int y1, int x2, int y2) {
		gc.drawLine(x1, y1, x2, y2);
	}

	public void drawPolygon(int[] p, boolean close) {
		if (close)
			gc.drawPolygon(p);
		else
			gc.drawPolyline(p);
	}
	
	private void drawImage(Image img, int x0, int y0, int w0, int h0, int x, int y, float sx, float sy) {
		Rectangle size = img.getBounds();
		gc.drawImage(img, x0, y0, 
				Math.min(w0, size.width), 
				Math.min(h0, size.height),
				x, y, 
				Math.round(w0 * sx), 
				Math.round(h0 * sy));
	}
	
	private void drawImage(Image img, int x, int y, float sx, float sy) {
		Rectangle r = img.getBounds();
		gc.drawImage(img, 0, 0, r.width, r.height,
				x, y, Math.round(r.width * sx), Math.round(r.height * sy));
	}
	
	private void drawImage(Image img, int x, int y) {
		gc.drawImage(img, x, y);
	}
	
	public void drawImage(LTexture img, int x, int y) {
		drawImage(img.convert(), x, y);
	}
	
	public void drawImage(LTexture img, int x, int y, float sx, float sy) {
		drawImage(img.convert(), x, y, sx, sy);
	}
	
	public void drawImage(LTexture img, int x0, int y0, int w0, int h0, int x, int y, float sx, float sy) {
		drawImage(img.convert(), x0, y0, w0, h0, x, y, sx, sy);
	}
	
	public void drawImage(String path, int x0, int y0, int w0, int h0, int x, int y, float sx, float sy) {
		Image img = SWTResourceManager.getImage(path);
		drawImage(img, x0, y0, w0, h0, x, y, sx, sy);
	}

	public void drawImageCenter(String path, int x, int y, float sx, float sy) {
		Image img = SWTResourceManager.getImage(path);
		drawImage(img,
				(int) (x - img.getBounds().width * sx / 2),
				(int) (y - img.getBounds().width * sy / 2),
				sx, sy);
	}
	
	public void fillPolygon(int[] p) {
		gc.fillPolygon(p);
	}
	
	public void fillRect(int x, int y, int w, int h) {
		gc.fillRectangle(x, y, w, h);
	}
	
	public void setTransparency(int alpha) {
		gc.setAlpha(alpha);
	}
	
	public void setPaintColor(LColor color) {
		gc.setForeground(color.convert());
	}
	
	public void setFillColor(LColor color) {
		gc.setBackground(color.convert());
	}
	
	public void dispose() {
		gc.dispose();
	}
	
	// }}
	
	public abstract void paint();

}