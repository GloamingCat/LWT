package lwt.editor;

import lbase.data.LDataTree;
import lbase.data.LPath;
import lbase.event.LEditEvent;
import lwt.container.LContainer;
import lwt.widget.LTree;

public abstract class LTreeEditor<T, ST> extends LAbstractTreeEditor<T, ST> {
	
	protected LTree<T, ST> tree;
	
	public LTreeEditor(LContainer parent) {
		this(parent, false);
	}
	
	public LTreeEditor(LContainer parent, boolean check) {
		super(parent);
		tree = createTree(check);
		setListeners();
		tree.setMenuInterface(getMenuInterface());
	}
	
	protected LTree<T, ST> createTree(boolean check) { 
		LTreeEditor<T, ST> self = this;
		return new LTree<T, ST>(this, check) {
			@Override
			public LEditEvent<ST> edit(LPath path) {
				return onEditItem(path);
			}
			@Override
			public T toObject(LPath path) {
				LDataTree<T> node = self.getDataCollection().getNode(path);
				if (node == null)
					return null;
				return node.data;
			}
			@Override
			public LDataTree<T> toNode(LPath path) {
				return self.getDataCollection().getNode(path);
			}
			@Override
			protected LDataTree<T> emptyNode() {
				return new LDataTree<T>(createNewElement());
			}
			@Override
			protected LDataTree<T> duplicateNode(LDataTree<T> node) {
				return self.duplicateData(node);
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

	public LTree<T, ST> getCollectionWidget() {
		return tree;
	}
	
	public abstract LDataTree<T> getDataCollection();

}
