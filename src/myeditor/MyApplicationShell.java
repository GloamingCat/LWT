package myeditor;

import lwt.LDefaultApplicationShell;
import lwt.dataserialization.LSerializer;
import myeditor.project.MyProject;
import myeditor.views.MyContentGridEditor;
import myeditor.views.MyContentListEditor;
import myeditor.views.MyContentTreeEditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MenuItem;

public class MyApplicationShell extends LDefaultApplicationShell {
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			MyApplicationShell shell = new MyApplicationShell();
			shell.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * @param display
	 */
	public MyApplicationShell() {
		super(450, 300, "My Editor", null);
		
		MyContentTreeEditor treeEditor = new MyContentTreeEditor(this);
		MyContentListEditor listEditor = new MyContentListEditor(this);
		MyContentGridEditor gridEditor = new MyContentGridEditor(this);
		
		MenuItem mntmContentList = new MenuItem(menuView, SWT.NONE);
		mntmContentList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setCurrentView(listEditor);
			}
		});
		mntmContentList.setText(MyVocab.instance.CONTENTLIST);
		
		MenuItem mntmContentTree = new MenuItem(menuView, SWT.NONE);
		mntmContentTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setCurrentView(treeEditor);
			}
		});
		mntmContentTree.setText(MyVocab.instance.CONTENTTREE);
		
		MenuItem mntmContentGrid = new MenuItem(menuView, SWT.NONE);
		mntmContentGrid.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setCurrentView(gridEditor);
			}
		});
		mntmContentGrid.setText(MyVocab.instance.CONTENTGRID);
	}
	
	@Override
	protected LSerializer createProject(String path) {
		return new MyProject(path);
	}
	
	@Override
	protected void checkSubclass() { }

}
