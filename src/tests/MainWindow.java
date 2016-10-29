package tests;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MainWindow {

	public static void main(String[] args) {
		MainWindow window = new MainWindow();
		try {
			window.open();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private Shell shell;

	public void open() {
		final Display display = new Display();
		shell = new Shell(display);
		createContents();
		shell.open();
		while (!shell.isDisposed()) {
		  if (!display.readAndDispatch())
		    display.sleep();
		}
		display.dispose();
	}
	
	private void createContents() {
		shell.setSize(632, 400);
		shell.setLayout(new FillLayout());
		ContentTreeEditor editor = new ContentTreeEditor(shell, SWT.NONE);
		editor.onVisible();
	}
  
}
