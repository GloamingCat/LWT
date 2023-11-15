package lwt.editor;

import lwt.dataestructure.LPath;
import lwt.datainterface.LGraphical;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

public abstract class LDefaultGridEditor<T extends LGraphical> extends LGridEditor<T, T> {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LDefaultGridEditor(Composite parent, int style) {
		super(parent, style);
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
