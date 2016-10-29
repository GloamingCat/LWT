package lwt.widget;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

public abstract class LList extends LMenuCollection {
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LList(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected int indexByBounds(Point pt, Rectangle bounds) {
		if (pt.y < bounds.y + bounds.height / 2) {
			return 0;
		} else {
			return 1;
		}
	}
	
}
