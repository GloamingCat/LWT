package lwt.dialog;

public interface LShellFactory<T> {

	public LObjectShell<T> createShell(LShell parent);
	
}
