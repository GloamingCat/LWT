package lwt.widget;

import lwt.container.LContainer;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.TreeItem;

import lbase.data.LDataTree;
import lbase.data.LPath;

public abstract class LList<T, ST> extends LTree<T, ST> {
	
	protected boolean includeID = false;
	
	public LList(LContainer parent) {
		this(parent, false);
	}
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LList(LContainer parent, boolean check) {
		super(parent, check);
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
	
	public void setItemNode(TreeItem item, LDataTree<T> node) {
		String id = "";
		if (includeID) {
			id = stringID(indexOf(item));
		}
		String name = dataToString(node.data);
		item.setData(DATA, node.data);
		item.setData(ID, node.id);
		item.setText(id + name);
	}
	
	public void refreshObject(LPath path) {
		TreeItem item = toTreeItem(path);
		if (item != null) {
			String id = "";
			if (includeID) {
				id = stringID(path.index);
			}
			String name = dataToString(toObject(path));
			item.setText(id + name);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void refreshAll() {
		if (includeID) {
			int i = 0;
			for(TreeItem item : tree.getItems()) {
				String name = dataToString((T) item.getData(DATA));
				String id = stringID(i++);
				item.setText(id + name);
			}
		} else {
			for(TreeItem item : tree.getItems()) {
				String name = dataToString((T) item.getData(DATA));
				item.setText(name);
			}
		}
	}
	
}
