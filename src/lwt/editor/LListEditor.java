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
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LListEditor(Composite parent, int style) {
		super(parent, style);
		
		LList<T, ST> list = new LList<T, ST>(this, SWT.NONE) {
			@Override
			public LEditEvent<ST> editTreeItem(LPath path) {
				return onEditItem(path);
			}
			@Override
			public Object toObject(LPath path) {
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
		setEditListeners();
		
	}
	
	public void onVisible() {
		collection.setItems(getList().toTree());
		super.onVisible();
	}

	public void setObject(Object obj) {
		@SuppressWarnings("unchecked")
		LDataList<T> db = (LDataList<T>) obj;
		collection.setItems(db.toTree());
	}
	
	public LDataTree<T> duplicateNode(LDataTree<T> node) {
		LDataTree<T> copy = new LDataTree<T>(duplicateData(node.data));
		for(LDataTree<T> child : node.children) {
			LDataTree<T> childCopy = duplicateNode(child);
			childCopy.setParent(copy);
		}
		return copy;
	}
	
	public abstract LDataList<T> getList();
	
	public abstract T createNewData();
	
	public abstract T duplicateData(T original);
	
	protected LDataCollection<T> getDataCollection() {
		return getList();
	}
	
}
