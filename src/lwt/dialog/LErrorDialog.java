package lwt.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

public class LErrorDialog {
	
	protected MessageBox msg;

	public LErrorDialog(LWindow parent, String title, String message) {
		msg = new MessageBox(parent.getWindow(), SWT.APPLICATION_MODAL | SWT.ICON_ERROR | SWT.OK);
		msg.setText(title);
		msg.setMessage(message);
	}
	
	public void open() {
		msg.open();
	}
	
}
