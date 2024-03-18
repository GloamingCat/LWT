package lwt.container;

import lwt.graphics.LColor;
import lwt.graphics.LPainter;
import lwt.graphics.LPoint;
import lwt.graphics.LRect;
import lwt.graphics.LTexture;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.SWTResourceManager;

import lbase.LFlags;

public class LImage extends LCanvas {

	private LTexture original = null;
	private LRect rectangle;
	private int align = LFlags.MIDDLE | LFlags.CENTER;
	
	private float r = 1, g = 1, b = 1;
	private float h = 0, s = 1, v = 1;
	private int a = 255;
	
	private float ox = 0, oy = 0;
	private float sx = 1, sy = 1;
	private float rz = 0;
	
	private float dx = 0, dy = 0;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LImage(LContainer parent) {
		super(parent);
		setBackground(SWTResourceManager.getColor(224, 224, 224));
		setLayout(new FillLayout());
		Listener oldListeter = getListeners(SWT.Paint)[0];
		removeListener(SWT.Paint, oldListeter);
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				currentEvent = e;
				int x = 0;
				int y = 0;
				if (buffer != null) {
					Rectangle bounds = getBounds();
					LRect rect = rectangle == null ? new LRect(buffer.getBounds()) : rectangle;
					int w = Math.round(rect.width * sx);
					int h = Math.round(rect.height * sy);
					if ((align & LFlags.RIGHT) > 0) {
						x = bounds.width - w;
					} else if ((align & LFlags.MIDDLE) > 0) {
						x = (bounds.width - w) / 2;
					}
					if ((align & LFlags.BOTTOM) > 0) {
						y = bounds.height - h;
					} else if ((align & LFlags.CENTER) > 0) {
						y = (bounds.height - h) / 2;
					}
					try {
						e.gc.setAlpha(a);
						if (rz != 0) {
							Transform t = new Transform(Display.getCurrent());
							t.translate(ox * sx, oy * sy);
							t.rotate(rz);
							t.translate(-ox * sx, -oy * sy);
							e.gc.setTransform(t);
						}
						e.gc.drawImage(buffer, rect.x, rect.y, rect.width, rect.height, 
								x, y, w, h);
						if (rz != 0)
							e.gc.setTransform(null);
					} catch (IllegalArgumentException ex) { System.out.println("Problem printing quad."); }
				}
				dx = x;
				dy = y;
				for (LPainter p : painters) {
					p.setGC(e.gc);
					p.paint();
				}
			}
		});
		addListener(SWT.Paint, oldListeter);
	}
	
	public float getImageX() {
		return dx;
	}
	
	public float getImageY() {
		return dy;
	}
	
	public void setBackground(LColor color) {
		setBackground(color.convert());
	}
	
	public void setImage(String path) {
		if (path == null) {
			setImage((LTexture) null, null);
			return;
		}
		LTexture img = new LTexture(path);
		setImage(img);
	}
	
	public void setImage(String path, LRect r) {
		if (path == null) {
			setImage((LTexture) null, null);
			return;
		}
		LTexture img = new LTexture(path);
		setImage(img, r);
	}
	
	public void setImage(LTexture img) {
		if (img == null) {
			setImage((LTexture) null, null);
		} else {
			setImage(img, img.getBounds());
		}
	}
	
	public void setImage(LTexture img, LRect rect) {
		rectangle = rect;
		original = img;
		disposeBuffer();
		refreshImage();
		rectangle = rect;
		redraw();
	}
	
	public void refreshImage() {
		if (original == null || original.convert() == null)
			return;
		disposeBuffer();
		ImageData imgData = original.convert().getImageData();
		LTexture.correctTransparency(imgData);
		LTexture.colorTransform(imgData, r, g, b, h, s, v);
		buffer = new Image(getDisplay(), imgData);
	}

	public boolean hasImage() {
		return buffer != null;
	}
	
	public LTexture getOriginalImage() {
		return original;
	}
	
	public LRect getRect() {
		return rectangle;
	}	
	
	public void setRect(LRect rect) {
		rectangle = rect;
		redraw();
	}
	
	public LPoint getImageSize() {
		Rectangle r = buffer.getBounds();
		return new LPoint(r.width, r.height);
	}
	
	public void setAlignment(int a) {
		align = a;
		redraw();
	}
	
	public void setOffset(float _ox, float _oy) {
		ox = _ox; oy = _oy;
	}
	
	public void setRGBA(float _r, float _g, float _b, float _a) {
		r = _r; g = _g; b = _b; a = Math.round(_a * 255);
	}
	
	public void setHSV(float _h, float _s, float _v) {
		h = _h; s = _s; v = _v;
	}
	
	public void setScale(float _sx, float _sy) {
		sx = _sx; sy = _sy;
	}
	
	public void setRotation(float _r) {
		rz = _r;
	}
	
}
