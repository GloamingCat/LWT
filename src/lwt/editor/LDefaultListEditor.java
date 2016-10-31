package lwt.editor;

import lwt.dataestructure.LPath;

import org.eclipse.swt.widgets.Composite;

public abstract class LDefaultListEditor<T> extends LListEditor<T, T> {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LDefaultListEditor(Composite parent, int style) {
		super(parent, style);

	}

	public T getEditableData(LPath path) {
		return getList().get(path.index);
	}
	
	public void setEditableData(LPath path, T data) {
		getList().set(path.index, data);
	}

}
