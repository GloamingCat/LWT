package lwt.editor;

import lwt.dataestructure.LDataCollection;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LEditEvent;
import lwt.widget.LTree;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public abstract class LTreeEditor<T, ST> extends LCollectionEditor<T, ST> {
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LTreeEditor(Composite parent, int style) {
		super(parent, style);
		
		LTreeEditor<T, ST> self = this;
		LTree<T, ST> tree = new LTree<T, ST>(this, SWT.NONE) {
			@Override
			public LEditEvent<ST> edit(LPath path) {
				return onEditItem(path);
			}
			@Override
			public Object toObject(LPath path) {
				LDataTree<T> node = getTree().getNode(path);
				if (node == null)
					return null;
				return node.data;
			}
			@Override
			public LDataTree<T> emptyNode() {
				return new LDataTree<T>(createNewData());
			}
			@Override
			public LDataTree<T> duplicateNode(LPath nodePath) {
				return self.duplicateNode(getTree().getNode(nodePath));
			}
			@Override
			public LDataTree<T> toNode(LPath path) {
				return getTree().getNode(path);
			}
		};
		setCollection(tree);
		setListeners();
	}
	
	public void onVisible() {
		onChildVisible();
		forceFirstSelection();
	}
	
	public void forceFirstSelection() {
		if (getTree() != null) {
			collection.setItems(getTree());
			if (getTree().children.size() > 0) {
				collection.forceSelection(new LPath(0));
			} else {
				collection.forceSelection(null);
			}
		} else {
			collection.setItems(new LDataTree<>());
			collection.forceSelection(null);
		}
	}

	public void setObject(Object obj) {
		@SuppressWarnings("unchecked")
		LDataTree<T> db = (LDataTree<T>) obj;
		collection.setItems(db);
	}
	
	public LDataTree<T> duplicateNode(LDataTree<T> node) {
		LDataTree<T> copy = new LDataTree<T>(duplicateData(node.data));
		for(LDataTree<T> child : node.children) {
			LDataTree<T> childCopy = duplicateNode(child);
			childCopy.setParent(copy);
		}
		return copy;
	}
	
	public abstract LDataTree<T> getTree();
	
	public abstract T createNewData();
	
	public abstract T duplicateData(T original);
	
	protected LDataCollection<T> getDataCollection() {
		return getTree();
	}
	
	
}
