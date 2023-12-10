package lwt.editor;

import lwt.container.LContainer;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LEditEvent;
import lwt.widget.LTree;

public abstract class LTreeEditor<T, ST> extends LAbstractTreeEditor<T, ST> {
	
	protected LTree<T, ST> tree;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LTreeEditor(LContainer parent) {
		super(parent);
		tree = createTree();
		setListeners();
		tree.setMenuInterface(getMenuInterface());
	}
	
	protected LTree<T, ST> createTree() { 
		LTreeEditor<T, ST> self = this;
		return new LTree<T, ST>(this) {
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
