package lwt.dialog;

import org.eclipse.swt.widgets.Display;

public abstract class LWindowFactory<T> {

	public abstract LObjectWindow<T> createWindow(LWindow parent);
	public T openWindow(LWindow parent, T initial) {
		LObjectWindow<T> shell = createWindow(parent);
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
