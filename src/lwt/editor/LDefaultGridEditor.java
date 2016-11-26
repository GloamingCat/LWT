package lwt.editor;

import lwt.dataestructure.LPath;
import lwt.datainterface.Graphical;

import org.eclipse.swt.widgets.Composite;

public abstract class LDefaultGridEditor<T extends Graphical> extends LGridEditor<T, T> {

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
	
	protected String getImagePath(int i) {
		return getDataCollection().get(i).getImagePath();
	}

}
