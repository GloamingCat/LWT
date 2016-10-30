package lwt.editor;

import org.eclipse.swt.widgets.Composite;

public abstract class LEditor extends LView {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LEditor(Composite parent, int style) {
		super(parent, style);
	}
	
	public abstract void setObject(Object object);

}
