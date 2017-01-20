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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MenuItem;

public class MyApplicationShell extends LDefaultApplicationShell {
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			MyApplicationShell shell = new MyApplicationShell(display);
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
	public MyApplicationShell(Display display) {
		super(display);
		setText("My Editor");
		setSize(450, 300);
		
		MyContentTreeEditor treeEditor = new MyContentTreeEditor(this, SWT.NONE);
		MyContentListEditor listEditor = new MyContentListEditor(this, SWT.NONE);
		MyContentGridEditor gridEditor = new MyContentGridEditor(this, SWT.NONE);
		
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
