package lwt.dialog;

import org.eclipse.swt.widgets.Shell;

public interface LShellFactory<T> {

	public LObjectShell<T> createShell(Shell parent);
	
}
