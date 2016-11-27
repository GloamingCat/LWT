package lwt.widget;

import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

public abstract class LList<T, ST> extends LTree<T, ST> {
	
	protected boolean includeID = false;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LList(Composite parent, int style) {
		super(parent, style);
	}
	
	public void setIncludeID(boolean value) {
		includeID = value;
	}

	@Override
	protected int indexByBounds(Point pt, Rectangle bounds) {
		if (pt.y < bounds.y + bounds.height / 2) {
			return 0;
		} else {
			return 1;
		}
	}
	
	public void setItems(LDataTree<T> root) {
		super.setItems(root);
		refreshAll();
	}
	
	public void refreshObject(LPath path) {
		TreeItem item = toTreeItem(path);
		String id = "";
		if (includeID) {
			id = stringID(path.index);
		}
		if (item != null) {
			item.setText(id + toObject(path).toString());
		}
	}
	
	public void refreshAll() {
		if (includeID) {
			int i = 0;
			for(TreeItem item : tree.getItems()) {
				String name = item.getData().toString();
				String id = stringID(i++);
				item.setText(id + name);
			}
		}
	}
	
	protected String stringID(int i) {
		return String.format("[%03d]", i);
	}
	
}
