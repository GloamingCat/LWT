package lwt.editor;

import lbase.data.LDataList;
import lbase.data.LDataTree;
import lbase.data.LPath;
import lbase.event.LEditEvent;
import lwt.container.LContainer;
import lwt.widget.LList;

public abstract class LListEditor<T, ST> extends LAbstractTreeEditor<T, ST> {
	
	protected LList<T, ST> list;
	
	public LListEditor(LContainer parent) {
		this(parent, false);
	}
	
	public LListEditor(LContainer parent, boolean check) {
		super(parent);	
		list = createList(check);
		setListeners();
		list.setMenuInterface(getMenuInterface());
	}
	
	protected LList<T, ST> createList(boolean check) { 
		LListEditor<T, ST> self = this;
		return new LList<T, ST>(this, check) {
			@Override
			public LEditEvent<ST> edit(LPath path) {
				return onEditItem(path);
			}
			@Override
			public T toObject(LPath path) {
				if (path == null || path.index == -1)
					return null;
				return self.getDataCollection().get(path.index);
			}
			@Override
			public LDataTree<T> emptyNode() {
				return new LDataTree<T>(createNewElement());
			}
			@Override
			public LDataTree<T> duplicateNode(LDataTree<T> node) {
				T data = duplicateElement(node.data);
				return new LDataTree<T> (data);
			}
			@Override
			public LDataTree<T> toNode(LPath path) {
				return self.getDataCollection().toTree().getNode(path);
			}
			@Override
			protected String encodeNode(LDataTree<T> node) {
				return self.encodeData(node);
			}
			@Override
			protected LDataTree<T> decodeNode(String str) {
				return self.decodeData(str);
			}
			@Override
			public boolean canDecode(String str) {
				return true;
			}
		};
	}
	
	public LList<T, ST> getCollectionWidget() {
		return list;
	}
	
	public void setIncludeID(boolean value) {
		list.setIncludeID(value);
	}
	
	protected abstract LDataList<T> getDataCollection();
	
}
