package lwt.widget;

import lwt.LHelper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

public class LImage extends Composite {

	private Image image = null;
	private Rectangle rectangle;
	private int alignv = SWT.TOP;
	private int alignh = SWT.LEFT;
	
	private float r = 1, g = 1, b = 1, a = 1;
	private float h = 0, s = 1, v = 1;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LImage(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				if (image == null)
					return;
				int x = 0;
				int y = 0;
				Rectangle bounds = getBounds();
				Rectangle rect = rectangle == null ? image.getBounds() : rectangle;
				if (alignh == SWT.RIGHT) {
					x = bounds.width - rect.width;
				} else if (alignh == SWT.CENTER) {
					x = (bounds.width - rect.width) / 2;
				}
				if (alignv == SWT.BOTTOM) {
					y = bounds.height - rect.height;
				} else if (alignv == SWT.CENTER) {
					y = (bounds.height - rect.height) / 2;
				}
				try {
					e.gc.drawImage(image, rect.x, rect.y, rect.width, rect.height, 
							x, y, rect.width, rect.height);
				} catch (IllegalArgumentException ex) { System.out.println("Problem printing quad."); }
				e.x = x;
				e.y = y;
			}
		});
	}
	
	public void setImage(String path) {
		Image img = SWTResourceManager.getImage(path);
		setImage(img);
	}
	
	public void setImage(String path, Rectangle r) {
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
		if (image != null)
			image.dispose();
		rectangle = rect;
		if (img == null) {
			image = null;
		} else {
			ImageData imgData = img.getImageData();
			imgData = LHelper.colorTransform(imgData, r, g, b, a, h, s, v);
			image = new Image(getDisplay(), imgData);
		}
		rectangle = rect;
		redraw();
	}
	
	public void setHorizontalAlign(int i) {
		alignh = i;
		redraw();
	}
	
	public void setVerticalAlign(int i) {
		alignv = i;
		redraw();
	}
	
	public int getHorizontalAlign() {
		return alignh;
	}
	
	public int getVerticalAlign() {
		return alignv;
	}
	
	public void setRGBA(float _r, float _g, float _b, float _a) {
		r = _r; g = _g; b = _b; a = _a;
	}
	
	public void setHSV(float _h, float _s, float _v) {
		h = _h; s = _s; v = _v;
	}
	
	@Override
	protected void checkSubclass() {}

}
