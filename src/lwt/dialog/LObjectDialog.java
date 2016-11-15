package lwt.dialog;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;

public class LObjectDialog<T> extends Dialog {

	protected T result;
	protected Composite content;
	protected Constructor<? extends LObjectShell<T>> constructor;

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
		LObjectShell<T> shell = createShell();
		shell.open(initial);
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return shell.getResult();
	}
	
	protected LObjectShell<T> createShell() {
		try {
			return constructor.newInstance(getParent().getShell());
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void setShell(Class<? extends LObjectShell<T>> type) {
		try {
			constructor = type.getConstructor(Shell.class);
		} catch (NoSuchMethodException | SecurityException 
				| IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
