package lwt.container;

import java.util.ArrayList;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;

import lwt.LColor;

public class LCanvas extends LView {
	
	public interface LPainter {
		public void paint();
	}

	protected PaintEvent currentEvent;
	protected ArrayList<LPainter> painters;
	
	protected GC bufferGC;
	protected Image buffer;

	/**
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter parent new lwt.dialog.LShell(800, 600)
	 */
	public LCanvas(LContainer parent) {
		super(parent, false);
		painters = new ArrayList<LCanvas.LPainter>();
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				currentEvent = e;
				for (LPainter p : painters) {
					p.paint();
				}
			}
		});
	}
	
	public void addPainter(LPainter painter) {
		painters.add(painter);
	}
	
	public void removePainter(LPainter painter) {
		painters.remove(painter);
	}

	//////////////////////////////////////////////////
	// {{ Draw
	
	private GC getGC() {
		if (bufferGC != null)
			return bufferGC;
		else 
			return currentEvent.gc;
	}
	
	public void drawRect(int x, int y, int w, int h) {
		getGC().drawRectangle(x, y, w, h);
	}

	public void drawPolygon(int[] p, boolean close) {
		if (close)
			getGC().drawPolygon(p);
		else
			getGC().drawPolyline(p);
	}
	
	public void drawImage(Image img, int x0, int y0, int w0, int h0, int x, int y, float sx, float sy) {
		getGC().drawImage(img, x0, y0, 
				Math.min(w0, img.getBounds().width), 
				Math.min(h0, img.getBounds().height),
				x, y, 
				Math.round(w0 * sx), 
				Math.round(h0 * sy));
	}
	
	public void drawImage(String path, int x0, int y0, int w0, int h0, int x, int y, float sx, float sy) {
		Image img = SWTResourceManager.getImage(path);
		drawImage(img, x0, y0, w0, h0, x, y, sx, sy);
	}
	
	public void drawImage(Image img, int x, int y, float sx, float sy) {
		Rectangle r = img.getBounds();
		getGC().drawImage(img, 0, 0, r.width, r.height,
				x, y, Math.round(r.width * sx), Math.round(r.height * sy));
	}
	
	public void drawImage(Image img, int x, int y) {
		getGC().drawImage(img, x, y);
	}
	
	public void drawImageCenter(String path, int x, int y, float sx, float sy) {
		Image img = SWTResourceManager.getImage(path);
		drawImage(img,
				(int) (x - img.getBounds().width * sx / 2),
				(int) (y - img.getBounds().width * sy / 2),
				sx, sy);
	}
	
	public void fillPolygon(int[] p) {
		getGC().fillPolygon(p);
	}
	
	public void fillRect(int x, int y, int w, int h) {
		getGC().fillRectangle(x, y, w, h);
	}
	
	public void fillRect() {
		if (bufferGC == null) {
			Rectangle r = getBounds();
			fillRect(r.x, r.y, r.width, r.height);
		} else {
			Rectangle r = buffer.getBounds();
			fillRect(r.x, r.y, r.width, r.height);
		}
	}
	
	public void setTransparency(int alpha) {
		getGC().setAlpha(alpha);
	}
	
	public void setPaintColor(LColor color) {
		getGC().setForeground(color.convert());
	}
	
	public void setFillColor(LColor color) {
		getGC().setBackground(color.convert());
	}

	// }}
	
	//////////////////////////////////////////////////
	// {{ Buffer
	
	public void setBuffer(Image image) {
		buffer = image;
	}
	
	public void drawBuffer(int x, int y, float sx, float sy) {
		drawImage(buffer, x, y, sx, sy);
	}
	
	public void drawBuffer(int x, int y) {
		drawImage(buffer, x, y);
	}
	
	public void pushBuffer() {
		buffer = new Image(Display.getCurrent(), getBounds());
		bufferGC = new GC(buffer);
	}
	
	public void pushBuffer(int w, int h) {
		buffer = new Image(Display.getCurrent(), w, h);
		bufferGC = new GC(buffer);
	}
	
	public void popBuffer() {
		if (bufferGC != null)
			bufferGC.dispose();
		bufferGC = null;
	}
	
	public void disposeBuffer() {
		if (buffer != null)
			buffer.dispose();
	}
	
	// }}
	
}
