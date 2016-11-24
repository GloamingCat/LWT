package lwt.editor;

import lwt.dataestructure.LDataList;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LEditEvent;
import lwt.widget.LList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public abstract class LListEditor<T, ST> extends LAbstractTreeEditor<T, ST> {
	
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
			public T toObject(LPath path) {
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
		setListeners();
		list.setActionStack(getActionStack());
	}
	
	public LList<T, ST> getCollection() {
		return list;
	}
	
	public void setIncludeID(boolean value) {
		list.setIncludeID(value);
	}
	
	@Override
	public void forceFirstSelection() {
		if (getList() != null) {
			list.setItems(getList().toTree());
			if (getList().size() > 0) {
				list.forceSelection(new LPath(0));
			} else {
				list.forceSelection(null);
			}
		} else {
			list.setItems(null);
			list.forceSelection(null);
		}
	}
	
	protected LDataList<T> getDataCollection() {
		return getList();
	}
	
	public void setList(LDataList<T> list) {}
	public abstract LDataList<T> getList();
	
}
