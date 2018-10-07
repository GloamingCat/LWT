package lwt.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

public class LImage extends Composite {

	private Image image;
	private Rectangle rect;
	private int alignv = SWT.TOP;
	private int alignh = SWT.LEFT;
	
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
				if (image == null || rect == null) {
					return;
				}
				int x = 0;
				int y = 0;
				Rectangle bounds = getBounds();
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
					e.gc.drawImage(image, rect.x, rect.y, rect.width, rect.height, x, y, rect.width, rect.height);
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
	
	public void setImage(Image img, Rectangle r) {
		image = img;
		rect = r;
		redraw();
	}
	
	public void setImage(Image img) {
		if (img == null) {
			setImage((Image) null, null);
		} else {
			setImage(img, img.getBounds());
		}
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
	
	@Override
	protected void checkSubclass() {}

}
