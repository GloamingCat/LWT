package lwt.editor;

import lwt.dataestructure.LDataCollection;
import lwt.dataestructure.LDataList;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LEditEvent;
import lwt.widget.LList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public abstract class LListEditor<T, ST> extends LCollectionEditor<T, ST> {
	
	protected LList<T, ST> list;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LListEditor(Composite parent, int style) {
		super(parent, style);
		
		list = new LList<T, ST>(this, SWT.NONE) {
			@Override
			public LEditEvent<ST> edit(LPath path) {
				return onEditItem(path);
			}
			@Override
			public Object toObject(LPath path) {
				if (path == null)
					return null;
				return getList().get(path.index);
			}
			@Override
			public LDataTree<T> emptyNode() {
				return new LDataTree<T>(createNewData());
			}
			@Override
			public LDataTree<T> duplicateNode(LPath path) {
				return new LDataTree<T> (getList().get(path.index));
			}
			@Override
			public LDataTree<T> toNode(LPath path) {
				return new LDataTree<T> (getList().get(path.index));
			}
		};
		setCollection(list);
		setListeners();
		
	}
	
	public void setIncludeID(boolean value) {
		list.setIncludeID(value);
	}
	
	public void onVisible() {
		onChildVisible();
		forceFirstSelection();
	}
	
	public void forceFirstSelection() {
		if (getList() != null) {
			collection.setItems(getList().toTree());
			if (getList().size() > 0) {
				collection.forceSelection(new LPath(0));
			} else {
				collection.forceSelection(null);
			}
		} else {
			collection.setItems(null);
			collection.forceSelection(null);
		}
	}

	public void setObject(Object obj) {
		if (obj == null) {
			collection.setItems(null);
			setList(null);
		} else {
			@SuppressWarnings("unchecked")
			LDataList<T> db = (LDataList<T>) obj;
			collection.setItems(db.toTree());
			setList(db);
		}
	}
	
	public LDataTree<T> duplicateNode(LDataTree<T> node) {
		LDataTree<T> copy = new LDataTree<T>(duplicateData(node.data));
		for(LDataTree<T> child : node.children) {
			LDataTree<T> childCopy = duplicateNode(child);
			childCopy.setParent(copy);
		}
		return copy;
	}
	
	protected void setList(LDataList<T> list) {}
	
	public abstract LDataList<T> getList();
	
	public abstract T createNewData();
	
	public abstract T duplicateData(T original);
	
	protected LDataCollection<T> getDataCollection() {
		return getList();
	}
	
}
