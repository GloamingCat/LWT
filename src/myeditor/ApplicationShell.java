package myeditor;

import lwt.LDefaultApplicationShell;
import lwt.LVocab;
import lwt.dataserialization.LSerializer;
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
		mntmContentList.setText(Vocab.instance.CONTENTLIST);
		
		MenuItem mntmContentTree = new MenuItem(menuView, SWT.NONE);
		mntmContentTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setCurrentView(treeEditor);
			}
		});
		mntmContentTree.setText(Vocab.instance.CONTENTTREE);
	}
	
	@Override
	protected LVocab getVocab() {
		return Vocab.instance;
	}
	
	@Override
	protected LSerializer createProject(String path) {
		return new Project(path);
	}
	
	@Override
	protected void checkSubclass() { }

}
