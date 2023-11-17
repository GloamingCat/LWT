package lwt.dialog;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;

public class LObjectDialog<T> extends Dialog {

	protected T result;
	protected Composite content;
	protected LShellFactory<T> factory;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public LObjectDialog(LShell parent, int style) {
		super(parent, style | SWT.APPLICATION_MODAL);
		setText("Object Dialog");
	}
	
	public LObjectDialog(LShell parent) {
		this(parent, parent.getStyle());
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public T open(T initial) {
		LObjectShell<T> shell = factory.createShell(getParent());
		shell.setText(getText());
		shell.open(initial);
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		T result = shell.getResult();
		return result;
	}
	
	public void setFactory(LShellFactory<T> factory) {
		this.factory = factory;
	}
	
	public LShell getParent() {
		return (LShell) super.getParent();
	}
	
}
