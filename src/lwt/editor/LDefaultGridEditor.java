package lwt.editor;

import lwt.container.LContainer;
import lwt.dataestructure.LPath;
import lwt.datainterface.LGraphical;

import org.eclipse.swt.graphics.Image;

public abstract class LDefaultGridEditor<T extends LGraphical> extends LGridEditor<T, T> {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LDefaultGridEditor(LContainer parent) {
		super(parent);
	}

	public T getEditableData(LPath path) {
		return getDataCollection().get(path.index);
	}
	
	public void setEditableData(LPath path, T data) {
		getDataCollection().set(path.index, data);
	}

	protected Image getImage(int i) {
		return getDataCollection().get(i).toImage();
	}

}
