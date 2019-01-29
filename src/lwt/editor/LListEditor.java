package lwt.editor;

import lwt.dataestructure.LDataList;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LEditEvent;
import lwt.widget.LList;

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
		list = new LList<T, ST>(this, style) {
			@Override
			public LEditEvent<ST> edit(LPath path) {
				return onEditItem(path);
			}
			@Override
			public T toObject(LPath path) {
				if (path == null || path.index == -1)
					return null;
				return getDataCollection().get(path.index);
			}
			@Override
			public LDataTree<T> emptyNode() {
				return new LDataTree<T>(createNewData());
			}
			@Override
			public LDataTree<T> duplicateNode(LPath path) {
				T data = duplicateData(getDataCollection().get(path.index));
				return new LDataTree<T> (data);
			}
			@Override
			public LDataTree<T> toNode(LPath path) {
				T data = getDataCollection().get(path.index);
				return new LDataTree<T> (data);
			}
		};
		LList<T, ST> customList = createList();
		if (customList != null) {
			list.dispose();
			list = customList;
		}
		setListeners();
		list.setActionStack(getActionStack());
	}
	
	protected LList<T, ST> createList() { return null; }
	
	public LList<T, ST> getCollectionWidget() {
		return list;
	}
	
	public void setIncludeID(boolean value) {
		list.setIncludeID(value);
	}
	
	protected abstract LDataList<T> getDataCollection();
	
}
