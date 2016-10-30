package lwt.widget;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public abstract class LTree<T, ST> extends LMenuCollection<T, ST> {
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LTree(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
	}
	
	protected int indexByBounds(Point pt, Rectangle bounds) {
		if (pt.y < bounds.y + bounds.height / 3) {
			return 0;
		} else if (pt.y > bounds.y + 2 * bounds.height / 3) {
			return 1;
		} else {
			return -1;
		}
	}
	
}
