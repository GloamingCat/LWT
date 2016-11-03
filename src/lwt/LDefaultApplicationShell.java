package lwt;

import lwt.action.LActionManager;
import lwt.dataserialization.LFileManager;
import lwt.dataserialization.LSerializer;
import lwt.editor.LView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public abstract class LDefaultApplicationShell extends Shell {

	protected LSerializer project = null;
	protected String applicationName;
	
	protected LView currentView;
	protected StackLayout stackLayout;
	
	protected Menu menuProject;
	protected Menu menuEdit;
	protected Menu menuView;
	protected Menu menuHelp;
	private MenuItem mntmView;
	
	/**
	 * Create the shell.
	 * @param display
	 */
	public LDefaultApplicationShell(Display display) {
		super(display, SWT.SHELL_TRIM);
		
		applicationName = getText();
		
		setSize(632, 400);
		
		stackLayout = new StackLayout();
		setLayout(stackLayout);
		
		addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
		        event.doit = askSave();
		    }
		});
		
		Menu menu = new Menu(this, SWT.BAR);
		setMenuBar(menu);
		
		MenuItem mntmProject = new MenuItem(menu, SWT.CASCADE);
		mntmProject.setText(Vocab.instance.PROJECT);
		
		menuProject = new Menu(mntmProject);
		mntmProject.setMenu(menuProject);
		
		MenuItem mntmNew = new MenuItem(menuProject, SWT.NONE);
		mntmNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				project = newProject();
			}
		});
		mntmNew.setText(Vocab.instance.NEW + "\tAlt + &N");
		
		MenuItem mntmOpen = new MenuItem(menuProject, SWT.NONE);
		mntmOpen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				project = openProject();
			}
		});
		mntmOpen.setText(Vocab.instance.OPEN + "\tAlt + &O");
		
		MenuItem mntmSave = new MenuItem(menuProject, SWT.NONE);
		mntmSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				saveProject();
			}
		});
		mntmSave.setText(Vocab.instance.SAVE + "\tAlt + &S");
		
		menuProject.addMenuListener(new MenuAdapter() {
			@Override
			public void menuShown(MenuEvent arg0) {
				mntmSave.setEnabled(project != null && LActionManager.getInstance().hasChanges());
			}
		});
		
		new MenuItem(menuProject, SWT.SEPARATOR);
		
		MenuItem mntmExit = new MenuItem(menuProject, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				close();
			}
		});
		mntmExit.setText(Vocab.instance.EXIT + "\t Alt + F4");
		mntmExit.setAccelerator(SWT.ALT | SWT.F4);
		
		MenuItem mntmEdit = new MenuItem(menu, SWT.CASCADE);
		mntmEdit.setText(Vocab.instance.EDIT);
		
		menuEdit = new Menu(mntmEdit);
		mntmEdit.setMenu(menuEdit);
		
		MenuItem mntmUndo = new MenuItem(menuEdit, SWT.NONE);
		mntmUndo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				currentView.getActionStack().undo();
			}
		});
		mntmUndo.setAccelerator(SWT.MOD1 | 'Z');
		mntmUndo.setText(Vocab.instance.UNDO + "\t Ctrl + &Z");
		
		MenuItem mntmRedo = new MenuItem(menuEdit, SWT.NONE);
		mntmRedo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				currentView.getActionStack().redo();
			}
		});
		mntmRedo.setAccelerator(SWT.MOD1 | 'Y');
		mntmRedo.setText(Vocab.instance.REDO + "\t Ctrl + &Y");
		
		menuEdit.addMenuListener(new MenuAdapter() {
			@Override
			public void menuShown(MenuEvent arg0) {
				if (currentView == null) {
					mntmUndo.setEnabled(false);
					mntmRedo.setEnabled(false);
				} else {
					mntmUndo.setEnabled(currentView.getActionStack().canUndo());
					mntmRedo.setEnabled(currentView.getActionStack().canRedo());
				}
			}
		});
		
		mntmView = new MenuItem(menu, SWT.CASCADE);
		mntmView.setText(Vocab.instance.VIEW);
		
		menuView = new Menu(mntmView);
		mntmView.setMenu(menuView);
		
		MenuItem mntmHelp = new MenuItem(menu, SWT.CASCADE);
		mntmHelp.setText(Vocab.instance.HELP);
		
		menuHelp = new Menu(mntmHelp);
		mntmHelp.setMenu(menuHelp);
		
		mntmView.setEnabled(false);
		mntmSave.setEnabled(false);
		
	}
	
	protected void setCurrentView(LView view) {
		currentView = view;
		stackLayout.topControl = currentView;
		layout();
		currentView.onVisible();
	}
	
	protected abstract LSerializer createProject(String path);
	
	public LSerializer newProject() {
		if (!askSave()) {
			return project;
		}
		DirectoryDialog dialog = new DirectoryDialog(this);
		dialog.setText(Vocab.instance.NEWPROJECT);
		dialog.setMessage(Vocab.instance.NEWMSG);
		dialog.setFilterPath(LFileManager.applicationPath());
		String resultPath = dialog.open();
		if (resultPath == null)
			return project;
		if (project.isDataFolder(resultPath)) {
			MessageBox msg = new MessageBox(this, SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
			msg.setText(Vocab.instance.EXISTINGPROJECT);
			msg.setMessage(Vocab.instance.EXISTINGMSG);
			int result = msg.open();
			if (result != SWT.YES) {
				return project;
			}
		}
		LSerializer newProject = createProject(resultPath);
		newProject.save();
		mntmView.setEnabled(true);
		return newProject;
	}
	
	public LSerializer openProject() {
		if (!askSave()) {
			return project;
		}
		DirectoryDialog dialog = new DirectoryDialog(this);
		dialog.setText(Vocab.instance.OPENPROJECT);
		dialog.setMessage(Vocab.instance.OPENMSG);
		dialog.setFilterPath(LFileManager.applicationPath());
		String resultPath = dialog.open();
		if (resultPath == null)
			return project;
		LSerializer previous = project;
		project = createProject(resultPath);
		if (!project.load()) {
			MessageBox msg = new MessageBox(this, SWT.ICON_ERROR | SWT.OK);
			msg.setText(Vocab.instance.LOADERROR);
			msg.setMessage(Vocab.instance.LOADERRORMSG);
			msg.open();
			project = previous;
		} else {
			mntmView.setEnabled(true);
			String path = LFileManager.appDataPath(applicationName) + "lattest.txt";
			byte[] bytes = resultPath.getBytes();
			LFileManager.save(path, bytes);
		}
		return project;
	}
	
	public void saveProject() {
		if (project == null || !LActionManager.getInstance().hasChanges())
			return;
		if (!project.save()) {
			MessageBox msg = new MessageBox(this, SWT.APPLICATION_MODAL | SWT.ICON_ERROR | SWT.OK);
			msg.setText(Vocab.instance.SAVEERROR);
			msg.setMessage(Vocab.instance.SAVEERRORMSG);
			msg.open();
		}
	}
	
	protected boolean askSave() {
		if (project != null && LActionManager.getInstance().hasChanges()) {
			MessageBox msg = new MessageBox(this, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.CANCEL);
			msg.setText(Vocab.instance.UNSAVEDPROJECT);
			msg.setMessage(Vocab.instance.UNSAVEDMSG);
			int result = msg.open();
			if (result == SWT.YES) {
				saveProject();
				return true;
			} else if (result == SWT.NO) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
	
}
