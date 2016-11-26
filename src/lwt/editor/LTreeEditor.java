package lwt.editor;

import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LEditEvent;
import lwt.widget.LTree;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public abstract class LTreeEditor<T, ST> extends LAbstractTreeEditor<T, ST> {
	
	protected LTree<T, ST> tree;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LTreeEditor(Composite parent, int style) {
		super(parent, style);
		LTreeEditor<T, ST> self = this;
		tree = new LTree<T, ST>(this, SWT.NONE) {
			@Override
			public LEditEvent<ST> edit(LPath path) {
				return onEditItem(path);
			}
			@Override
			public T toObject(LPath path) {
				LDataTree<T> node = getDataCollection().getNode(path);
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
				return self.duplicateNode(getDataCollection().getNode(nodePath));
			}
			@Override
			public LDataTree<T> toNode(LPath path) {
				return getDataCollection().getNode(path);
			}
		};
		setListeners();
		tree.setActionStack(getActionStack());
	}

	public LTree<T, ST> getCollectionWidget() {
		return tree;
	}
	
	protected abstract LDataTree<T> getDataCollection();

}
