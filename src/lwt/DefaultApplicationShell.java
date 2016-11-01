package lwt;

import java.io.File;

import lwt.dataserialization.LFileManager;
import lwt.dataserialization.LProject;
import lwt.editor.LView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public abstract class DefaultApplicationShell extends Shell {

	protected LProject project = null;
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
	public DefaultApplicationShell(Display display) {
		super(display, SWT.SHELL_TRIM);
		
		applicationName = getText();
		
		setSize(632, 400);
		
		stackLayout = new StackLayout();
		setLayout(stackLayout);
		
		Menu menu = new Menu(this, SWT.BAR);
		setMenuBar(menu);
		
		MenuItem mntmProject = new MenuItem(menu, SWT.CASCADE);
		mntmProject.setText("Project");
		
		menuProject = new Menu(mntmProject);
		mntmProject.setMenu(menuProject);
		
		MenuItem mntmNew = new MenuItem(menuProject, SWT.NONE);
		mntmNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				project = newProject();
			}
		});
		mntmNew.setText("New");
		mntmNew.setText("New\tAlt + &N");
		
		MenuItem mntmOpen = new MenuItem(menuProject, SWT.NONE);
		mntmOpen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				project = openProject();
			}
		});
		mntmOpen.setText("Open\tAlt + &O");
		
		MenuItem mntmSave = new MenuItem(menuProject, SWT.NONE);
		mntmSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				saveProject();
			}
		});
		mntmSave.setText("Save\tAlt + &S");
		
		menuProject.addMenuListener(new MenuAdapter() {
			@Override
			public void menuShown(MenuEvent arg0) {
				mntmSave.setEnabled(project != null && project.hasChanges());
			}
		});
		
		new MenuItem(menuProject, SWT.SEPARATOR);
		
		MenuItem mntmExit = new MenuItem(menuProject, SWT.NONE);
		mntmExit.setText("Exit \t Alt + F4");
		mntmExit.setAccelerator(SWT.ALT | SWT.F4);
		
		MenuItem mntmEdit = new MenuItem(menu, SWT.CASCADE);
		mntmEdit.setText("Edit");
		
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
		
		mntmView = new MenuItem(menu, SWT.CASCADE);
		mntmView.setText("View");
		
		menuView = new Menu(mntmView);
		mntmView.setMenu(menuView);
		
		MenuItem mntmHelp = new MenuItem(menu, SWT.CASCADE);
		mntmHelp.setText("Help");
		
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
	
	protected abstract LProject createProject(String path);
	
	public LProject newProject() {
		if (!askSave()) {
			return project;
		}
		DirectoryDialog dialog = new DirectoryDialog(this);
		dialog.setText("New Project");
		dialog.setMessage("Select a folder to save the project.");
		dialog.setFilterPath(LFileManager.applicationPath());
		String resultPath = dialog.open();
		if (resultPath == null)
			return project;
		
		File folder = new File(resultPath);
		boolean hasProject = false;
		for(File entry : folder.listFiles()) {
			if (entry.isDirectory() && entry.getName().equals("data")) {
				for (File entry2 : entry.listFiles()) {
					if (entry.isFile() && project.isDataFile(entry2.getName())) {
						hasProject = true;
						break;
					}
					if (entry2.isDirectory() && entry2.getName().equals("fields")) {
						hasProject = true;
						break;
					}
				}
			}
			if (hasProject) break;
		}
		if (hasProject) {
			MessageBox msg = new MessageBox(this, SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
			msg.setText("Existing project");
			msg.setMessage("There's already a project in this folder. Would you like to override it anyway?");
			int result = msg.open();
			if (result != SWT.YES) {
				return project;
			}
		}
		LProject newProject = createProject(resultPath);
		newProject.save();
		mntmView.setEnabled(true);
		return newProject;
	}
	
	public LProject openProject() {
		if (!askSave()) {
			return project;
		}
		DirectoryDialog dialog = new DirectoryDialog(this);
		dialog.setText("Open Project");
		dialog.setMessage("Select the projects folder.");
		dialog.setFilterPath(LFileManager.applicationPath());
		String resultPath = dialog.open();
		if (resultPath == null)
			return project;
		LProject previous = project;
		project = createProject(resultPath);
		if (!project.load()) {
			MessageBox msg = new MessageBox(this, SWT.ICON_ERROR | SWT.OK);
			msg.setText("Load error");
			msg.setMessage("Couldn't load the project files.");
			msg.open();
			project = previous;
		} else {
			mntmView.setEnabled(true);
			LFileManager.save(LFileManager.appDataPath(applicationName) + "lattest.txt", resultPath);
		}
		return project;
	}
	
	public void saveProject() {
		if (project == null || !project.hasChanges())
			return;
		if (!project.save()) {
			MessageBox msg = new MessageBox(this, SWT.APPLICATION_MODAL | SWT.ICON_ERROR | SWT.OK);
			msg.setText("Save error");
			msg.setMessage("Couldn't save project.");
			msg.open();
		}
	}
	
	protected boolean askSave() {
		if (project != null && project.hasChanges()) {
			MessageBox msg = new MessageBox(this, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.CANCEL);
			msg.setText("Unsaved project");
			msg.setMessage("There're some changes in the current project. Would you like to save it first?");
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
