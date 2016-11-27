package lwt.dialog;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
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
	public LObjectDialog(Shell parent, int style) {
		super(parent, style | SWT.APPLICATION_MODAL);
		setText("Object Dialog");
	}
	
	public LObjectDialog(Shell parent) {
		this(parent, parent.getStyle());
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public T open(T initial) {
		LObjectShell<T> shell = factory.createShell(getParent().getShell());
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
	
}
