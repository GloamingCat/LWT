package myeditor;

import lwt.LDefaultApplicationShell;
import lwt.dataserialization.LProject;
import myeditor.project.Project;
import myeditor.views.ContentListEditor;
import myeditor.views.ContentTreeEditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MenuItem;

public class ApplicationShell extends LDefaultApplicationShell {

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			ApplicationShell shell = new ApplicationShell(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public ApplicationShell(Display display) {
		super(display);
		setText("My Editor");
		setSize(450, 300);
		
		ContentTreeEditor treeEditor = new ContentTreeEditor(this, SWT.NONE);
		ContentListEditor listEditor = new ContentListEditor(this, SWT.NONE);
		
		MenuItem mntmContentList = new MenuItem(menuView, SWT.NONE);
		mntmContentList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setCurrentView(listEditor);
			}
		});
		mntmContentList.setText("Content List");
		
		MenuItem mntmContentTree = new MenuItem(menuView, SWT.NONE);
		mntmContentTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setCurrentView(treeEditor);
			}
		});
		mntmContentTree.setText("Content Tree");
	}
	
	@Override
	protected LProject createProject(String path) {
		return new Project(path);
	}
	
	@Override
	protected void checkSubclass() { }

}
