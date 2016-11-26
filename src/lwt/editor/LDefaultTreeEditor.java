package lwt.editor;

import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;

import org.eclipse.swt.widgets.Composite;

public abstract class LDefaultTreeEditor<T> extends LTreeEditor<T, T> {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LDefaultTreeEditor(Composite parent, int style) {
		super(parent, style);
	}

	public T getEditableData(LPath path) {
		LDataTree<T> node = getDataCollection().getNode(path);
		return node.data;
	}
	
	public void setEditableData(LPath path, T data) {
		LDataTree<T> node = getDataCollection().getNode(path);
		node.data = data;
	}
	
}
