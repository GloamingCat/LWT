package lwt.container;

import java.util.ArrayList;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import lwt.graphics.LPainter;
import lwt.graphics.LTexture;

public class LCanvas extends LView {
	
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
		painters = new ArrayList<LPainter>();
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				currentEvent = e;
				for (LPainter p : painters) {
					p.setGC(e.gc);
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
	
	public void fillRect() {
		if (bufferGC == null) {
			Rectangle r = getBounds();
			currentEvent.gc.fillRectangle(r.x, r.y, r.width, r.height);
		} else {
			Rectangle r = buffer.getBounds();
			bufferGC.fillRectangle(r.x, r.y, r.width, r.height);
		}
	}
	
	// }}
	
	//////////////////////////////////////////////////
	// {{ Buffer
	
	public void setBuffer(LTexture image) {
		buffer = image.convert();
	}
	
	public void drawBuffer(int x, int y, float sx, float sy) {
		Rectangle r = buffer.getBounds();
		currentEvent.gc.drawImage(buffer, 0, 0, r.width, r.height,
				x, y, Math.round(r.width * sx), Math.round(r.height * sy));
	}
	
	public void drawBuffer(int x, int y) {
		currentEvent.gc.drawImage(buffer, x, y);
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
	
	public LPainter getBufferPainter() {
		return new LPainter(bufferGC) {
			@Override
			public void paint() {}
		};
	}
	
	public void disposeBuffer() {
		if (buffer != null)
			buffer.dispose();
		buffer = null;
	}
	
	public void dispose() {
		super.dispose();
		disposeBuffer();
	};
	
	// }}
	
	public void redraw() {
		super.redraw();
	}
	
}
