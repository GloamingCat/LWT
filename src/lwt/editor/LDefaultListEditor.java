package lwt.editor;

import lwt.container.LContainer;
import lwt.dataestructure.LPath;

public abstract class LDefaultListEditor<T> extends LListEditor<T, T> {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LDefaultListEditor(LContainer parent) {
		super(parent);
	}

	public T getEditableData(LPath path) {
		return getDataCollection().get(path.index);
	}
	
	public void setEditableData(LPath path, T data) {
		getDataCollection().set(path.index, data);
	}

}
