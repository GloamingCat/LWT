package tests;

import lwt.editor.LView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.custom.StackLayout;

public class MainWindow {

	private LView currentView = null;
	
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
		StackLayout stackLayout = new StackLayout();
		shell.setSize(632, 400);
		shell.setLayout(stackLayout);
		
		ContentTreeEditor treeEditor = new ContentTreeEditor(shell, SWT.NONE);
		ContentListEditor listEditor = new ContentListEditor(shell, SWT.NONE);
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmEdit = new MenuItem(menu, SWT.CASCADE);
		mntmEdit.setText("Edit");
		
		Menu menuEdit = new Menu(mntmEdit);
		mntmEdit.setMenu(menuEdit);
		
		MenuItem mntmUndo = new MenuItem(menuEdit, SWT.NONE);
		mntmUndo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				currentView.getActionStack().undo();
			}
		});
		mntmUndo.setAccelerator(SWT.MOD1 | 'Z');
		mntmUndo.setText("Undo \t Ctrl + &Z");
		
		MenuItem mntmRedo = new MenuItem(menuEdit, SWT.NONE);
		mntmRedo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				currentView.getActionStack().redo();
			}
		});
		mntmRedo.setAccelerator(SWT.MOD1 | 'Y');
		mntmRedo.setText("Redo \t Ctrl + &Y");
		
		MenuItem mntmView = new MenuItem(menu, SWT.CASCADE);
		mntmView.setText("View");
		
		Menu menuView = new Menu(mntmView);
		mntmView.setMenu(menuView);
		
		MenuItem mntmContentList = new MenuItem(menuView, SWT.NONE);
		mntmContentList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				currentView = listEditor;
				stackLayout.topControl = currentView;
				shell.layout();
				currentView.onVisible();
			}
		});
		mntmContentList.setText("Content List");
		
		MenuItem mntmContentTree = new MenuItem(menuView, SWT.NONE);
		mntmContentTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				currentView = treeEditor;
				stackLayout.topControl = currentView;
				shell.layout();
				currentView.onVisible();
			}
		});
		mntmContentTree.setText("Content Tree");
		
		currentView = listEditor;
		stackLayout.topControl = currentView;
		shell.layout();
		currentView.onVisible();
	}
}
