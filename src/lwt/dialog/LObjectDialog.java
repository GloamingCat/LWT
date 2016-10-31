package lwt.dialog;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;

public abstract class LObjectDialog<T> extends Dialog {

	protected T result;
	protected LObjectShell<T> shell;
	protected Composite content;

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
		shell = createShell(initial);
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return shell.getResult();
	}
	
	protected abstract LObjectShell<T> createShell(T initial);
	
}
