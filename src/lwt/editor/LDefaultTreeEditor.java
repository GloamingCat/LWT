package lwt.editor;

import lbase.data.LDataTree;
import lbase.data.LPath;
import lwt.container.LContainer;

public abstract class LDefaultTreeEditor<T> extends LTreeEditor<T, T> {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LDefaultTreeEditor(LContainer parent) {
		super(parent);
	}

	@Override
	public T getEditableData(LPath path) {
		LDataTree<T> node = getDataCollection().getNode(path);
		return node.data;
	}
	
	@Override
	public void setEditableData(LPath path, T data) {
		LDataTree<T> node = getDataCollection().getNode(path);
		node.data = data;
	}
	
}
