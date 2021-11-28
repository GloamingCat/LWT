package lwt.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class LDescriptor extends Text {

	public LDescriptor(Composite parent, int style) {
		super(parent, style);
	}
	
	public LDescriptor(Composite parent) {
		super(parent, SWT.BORDER | SWT.READ_ONLY);
	}
	
	@Override
	protected void checkSubclass() {}

}
