package lwt.dialog;

import org.eclipse.swt.widgets.Display;

public abstract class LShellFactory<T> {

	public abstract LObjectShell<T> createShell(LShell parent);
	public T openShell(LShell parent, T initial) {
		LObjectShell<T> shell = createShell(parent);
		shell.open(initial);
		shell.layout(true, true);
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		T result = shell.getResult();
		return result;
	}
	
}
