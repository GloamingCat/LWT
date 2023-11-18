package lwt.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

public class LSashPanel extends SashForm implements LContainer{

	 LSashPanel(Composite parent, int style) {
		super(parent, style);
	}

	 /**
	  * Fill layout.
	  * @param parent
	  * @param horizontal
	  */
	public LSashPanel(LContainer parent, boolean horizontal) {
		super(parent.getComposite(), horizontal ? SWT.HORIZONTAL : SWT.VERTICAL);
	}

	public LSashPanel(LContainer parent) {
		this(parent, true);
	}

	@Override
	protected void checkSubclass() { }
	
	public Composite getComposite() {
		return this;
	}
	
}
