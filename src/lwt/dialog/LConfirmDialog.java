package lwt.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

public class LConfirmDialog {
	
	public static final int YES_NO_CANCEL = 0;
	public static final int YES_NO = 1;
	public static final int OK_CANCEL = 2;

	public final static int YES = 0;
	public final static int NO = 1;
	public final static int CANCEL = 2;
	public final static int CLOSE = -1;
	
	protected MessageBox msg;

	public LConfirmDialog(LWindow parent, String title, String message, int opts) {
		int style = SWT.YES | SWT.NO;
		if (opts == YES_NO_CANCEL)
			style |= SWT.CANCEL;
		else if (opts == OK_CANCEL)
			style = SWT.OK | SWT.CANCEL;
		msg = new MessageBox(parent.getWindow(), SWT.APPLICATION_MODAL | SWT.ICON_QUESTION | style);
		msg.setText(title);
		msg.setMessage(message);
	}
	
	public int open() {
		int opts = msg.open();
		if (opts == SWT.CANCEL)
			return CANCEL;
		if (opts == SWT.OK || opts == SWT.YES)
			return YES;
		if (opts == SWT.NO)
			return NO;
		return CLOSE;
	}
	
}
