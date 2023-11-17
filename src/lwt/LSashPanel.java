package lwt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

public class LSashPanel extends SashForm implements LContainer{

	public LSashPanel(Composite parent, int style) {
		super(parent, style);
	}

	public LSashPanel(LContainer parent) {
		this(parent, true);
	}
	
	public LSashPanel(LContainer parent, boolean horizontal) {
		super(parent.getComposite(), horizontal ? SWT.HORIZONTAL : SWT.VERTICAL);
	}

	@Override
	protected void checkSubclass() { }
	
	public Composite getComposite() {
		return this;
	}
	
}
