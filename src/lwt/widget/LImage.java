package lwt.widget;

import lwt.LColor;
import lwt.LFlags;
import lwt.LImageHelper;
import lwt.container.LCanvas;
import lwt.container.LContainer;
import lwt.event.listener.LPainter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.SWTResourceManager;

public class LImage extends LCanvas {

	private Image image = null;
	private Image original = null;
	private Rectangle rectangle;
	private int align = LFlags.MIDDLE | LFlags.CENTER;
	
	private float r = 1, g = 1, b = 1;
	private float h = 0, s = 1, v = 1;
	private int a = 255;
	
	private float sx = 1, sy = 1;

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
				if (image == null)
					return;
				Rectangle bounds = getBounds();
				Rectangle rect = rectangle == null ? image.getBounds() : rectangle;
				int x = 0;
				int y = 0;
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
					e.gc.drawImage(image, rect.x, rect.y, rect.width, rect.height, 
							x, y, w, h);
				} catch (IllegalArgumentException ex) { System.out.println("Problem printing quad."); }
				e.x = x;
				e.y = y;
				for (LPainter p : painters) {
					p.paint();
				}
			}
		});
		addListener(SWT.Paint, oldListeter);
	}
	
	public void setBackground(LColor color) {
		setBackground(color.convert());
	}
	
	public void setImage(String path) {
		if (path == null) {
			setImage((Image) null, null);
			return;
		}
		Image img = SWTResourceManager.getImage(path);
		setImage(img);
	}
	
	public void setImage(String path, Rectangle r) {
		if (path == null) {
			setImage((Image) null, null);
			return;
		}
		Image img = SWTResourceManager.getImage(path);
		setImage(img, r);
	}
	
	public void setImage(Image img) {
		if (img == null) {
			setImage((Image) null, null);
		} else {
			setImage(img, img.getBounds());
		}
	}
	
	public void setImage(Image img, Rectangle rect) {
		rectangle = rect;
		original = img;
		if (image != null)
			image.dispose();
		if (img == null) {
			image = null;
		} else {
			ImageData imgData = img.getImageData();
			LImageHelper.correctTransparency(imgData);
			LImageHelper.colorTransform(imgData, r, g, b, h, s, v);
			image = new Image(getDisplay(), imgData);
		}
		rectangle = rect;
		redraw();
	}
	
	public Image getImage() {
		return image;
	}
	
	public Image getOriginalImage() {
		return original;
	}
	
	public Rectangle getRectangle() {
		return rectangle;
	}
	
	public void setAlignment(int a) {
		align = a;
		redraw();
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
	
	public void dispose() {
		super.dispose();
		if (image != null)
			image.dispose();
	};

}
