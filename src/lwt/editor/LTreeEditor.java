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
		LTreeEditor<T, ST> self = this;
		tree = new LTree<T, ST>(this) {
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
				return new LDataTree<T>(createNewData());
			}
			@Override
			protected LDataTree<T> duplicateNode(LDataTree<T> node) {
				return self.duplicateNode(node);
			}
			@Override
			protected String encodeNode(LDataTree<T> node) {
				return self.encodeNode(node);
			}
			@Override
			protected LDataTree<T> decodeNode(String str) {
				return self.decodeNode(str);
			}
		};
		LTree<T, ST> customTree = createTree();
		if (customTree != null) {
			tree.dispose();
			tree = customTree;
		}
		setListeners();
		tree.setActionStack(getActionStack());
	}
	
	protected LTree<T, ST> createTree() { return null; }

	public LTree<T, ST> getCollectionWidget() {
		return tree;
	}
	
	public abstract LDataTree<T> getDataCollection();

}
