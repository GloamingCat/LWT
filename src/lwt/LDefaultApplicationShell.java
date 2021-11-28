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
import org.eclipse.wb.swt.SWTResourceManager;

public abstract class LDefaultApplicationShell extends Shell {

	protected LSerializer project = null;
	protected String applicationName;
	
	protected LView defaultView = null;
	protected LView currentView;
	protected StackLayout stackLayout;
	
	protected Menu menuProject;
	protected Menu menuEdit;
	protected Menu menuView;
	protected Menu menuHelp;
	protected MenuItem mntmView;
	
	/**
	 * Create the shell.
	 * @param display
	 */
	public LDefaultApplicationShell(int initialWidth, int initialHeight, String title, String icon) {
		super(Display.getDefault(), SWT.SHELL_TRIM | SWT.DOUBLE_BUFFERED);
		if (title != null) {
			setText(title);
			applicationName = "LTH Editor";
		}
		if (icon != null) {
			setImage(SWTResourceManager.getImage(LDefaultApplicationShell.class, icon));
		}
		
		LVocab vocab = LVocab.instance;
		
		setSize(initialWidth, initialHeight);
		
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
		mntmProject.setText(vocab.PROJECT);
		
		menuProject = new Menu(mntmProject);
		mntmProject.setMenu(menuProject);
		
		MenuItem mntmNew = new MenuItem(menuProject, SWT.NONE);
		mntmNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				project = newProject();
			}
		});
		mntmNew.setText(vocab.NEW + "\tCtrl + &N");
		mntmNew.setAccelerator(SWT.MOD1 | 'N');
		
		MenuItem mntmOpen = new MenuItem(menuProject, SWT.NONE);
		mntmOpen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				project = openProject();
			}
		});
		mntmOpen.setText(vocab.OPEN + "\tCtrl + &O");
		mntmOpen.setAccelerator(SWT.MOD1 | 'O');
		
		MenuItem mntmSave = new MenuItem(menuProject, SWT.NONE);
		mntmSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				saveProject();
			}
		});
		mntmSave.setText(vocab.SAVE + "\tCtrl + &S");
		mntmSave.setAccelerator(SWT.MOD1 | 'S');
		
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
		mntmExit.setText(vocab.EXIT + "\t Alt + F4");
		mntmExit.setAccelerator(SWT.ALT | SWT.F4);
		
		MenuItem mntmEdit = new MenuItem(menu, SWT.CASCADE);
		mntmEdit.setText(vocab.EDIT);
		
		menuEdit = new Menu(mntmEdit);
		mntmEdit.setMenu(menuEdit);
		
		MenuItem mntmUndo = new MenuItem(menuEdit, SWT.NONE);
		mntmUndo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				currentView.undo();
			}
		});
		mntmUndo.setAccelerator(SWT.MOD1 | 'Z');
		mntmUndo.setText(vocab.UNDO + "\t Ctrl + &Z");
		
		MenuItem mntmRedo = new MenuItem(menuEdit, SWT.NONE);
		mntmRedo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				currentView.redo();
			}
		});
		mntmRedo.setAccelerator(SWT.MOD1 | 'Y');
		mntmRedo.setText(vocab.REDO + "\t Ctrl + &Y");
		
		menuEdit.addMenuListener(new MenuAdapter() {
			@Override
			public void menuShown(MenuEvent arg0) {
				if (currentView == null) {
					mntmUndo.setEnabled(false);
					mntmRedo.setEnabled(false);
				} else {
					mntmUndo.setEnabled(currentView.canUndo());
					mntmRedo.setEnabled(currentView.canRedo());
				}
			}
		});
		
		mntmView = new MenuItem(menu, SWT.CASCADE);
		mntmView.setText(vocab.VIEW);
		
		menuView = new Menu(mntmView);
		mntmView.setMenu(menuView);
		
		MenuItem mntmHelp = new MenuItem(menu, SWT.CASCADE);
		mntmHelp.setText(vocab.HELP);
		
		menuHelp = new Menu(mntmHelp);
		mntmHelp.setMenu(menuHelp);
		
		mntmView.setEnabled(false);
		mntmSave.setEnabled(false);
		
	}
	
	public void run() {
		open();
		layout();
		while (!isDisposed()) {
			if (!Display.getDefault().readAndDispatch()) {
				Display.getDefault().sleep();
			}
		}
	}
	
	protected void addView(final LView view, String name, String shortcut) {
		MenuItem item = new MenuItem(menuView, SWT.NONE);
		item.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setCurrentView(view);
			}
		});
		if (shortcut != null) {
			item.setText(name + "\t" + shortcut);
			item.setAccelerator(SWTHelper.accelerators.get(shortcut));
		} else {
			item.setText(name);
		}
	}
	
	protected boolean loadDefault(String path) {
		if (path == null) {
			String lattest = LFileManager.appDataPath(applicationName) + "lattest.txt";
			byte[] bytes = LFileManager.load(lattest);
			if (bytes != null && bytes.length > 0) {
				path = new String(bytes);
			} else {
				return false;
			}
		}
		LVocab vocab = LVocab.instance;
		LSerializer project = createProject(path);
		if (!project.load()) {
			MessageBox msg = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
			msg.setText(vocab.LOADERROR);
			msg.setMessage(vocab.LOADERRORMSG + "\n" + path);
			msg.open();
			return false;
		} else {
			this.project = project;
			mntmView.setEnabled(true);
			if (defaultView != null)
				setCurrentView(defaultView);
			return true;
		}
	}
	
	protected boolean loadDefault() {
		return loadDefault(null);
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
		LVocab vocab = LVocab.instance;
		DirectoryDialog dialog = new DirectoryDialog(this);
		dialog.setText(vocab.NEWPROJECT);
		dialog.setMessage(vocab.NEWMSG);
		dialog.setFilterPath(LFileManager.applicationPath());
		String resultPath = dialog.open();
		if (resultPath == null)
			return project;
		resultPath += "/";
		LSerializer newProject = createProject(resultPath);
		if (newProject.isDataFolder(resultPath)) {
			MessageBox msg = new MessageBox(this, SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
			msg.setText(vocab.EXISTINGPROJECT);
			msg.setMessage(vocab.EXISTINGMSG);
			int result = msg.open();
			if (result != SWT.YES) {
				return project;
			}
		}
		newProject.initialize();
		newProject.save();
		mntmView.setEnabled(true);
		String path = LFileManager.appDataPath(applicationName) + "lattest.txt";
		byte[] bytes = resultPath.getBytes();
		LFileManager.save(path, bytes);
		if (defaultView != null)
			setCurrentView(defaultView);
		return newProject;
	}
	
	public LSerializer openProject() {
		if (!askSave()) {
			return project;
		}
		LVocab vocab = LVocab.instance;
		DirectoryDialog dialog = new DirectoryDialog(this);
		dialog.setText(vocab.OPENPROJECT);
		dialog.setMessage(vocab.OPENMSG);
		dialog.setFilterPath(LFileManager.applicationPath());
		String resultPath = dialog.open();
		if (resultPath == null)
			return project;
		LSerializer previous = project;
		resultPath += "/";
		project = createProject(resultPath);
		if (!project.load()) {
			MessageBox msg = new MessageBox(this, SWT.ICON_ERROR | SWT.OK);
			msg.setText(vocab.LOADERROR);
			msg.setMessage(vocab.LOADERRORMSG + ":" + resultPath);
			msg.open();
			project = previous;
		} else {
			mntmView.setEnabled(true);
			String path = LFileManager.appDataPath(applicationName) + "lattest.txt";
			byte[] bytes = resultPath.getBytes();
			LFileManager.save(path, bytes);
			if (defaultView != null)
				setCurrentView(defaultView);
		}
		return project;
	}
	
	public void saveProject() {
		if (project == null || !LActionManager.getInstance().hasChanges())
			return;
		if (!project.save()) {
			LVocab vocab = LVocab.instance;
			MessageBox msg = new MessageBox(this, SWT.APPLICATION_MODAL | SWT.ICON_ERROR | SWT.OK);
			msg.setText(vocab.SAVEERROR);
			msg.setMessage(vocab.SAVEERRORMSG);
			msg.open();
		} else {
			LActionManager.getInstance().onSave();
		}
	}
	
	protected boolean askSave() {
		if (project != null && LActionManager.getInstance().hasChanges()) {
			LVocab vocab = LVocab.instance;
			MessageBox msg = new MessageBox(this, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO | SWT.CANCEL);
			msg.setText(vocab.UNSAVEDPROJECT);
			msg.setMessage(vocab.UNSAVEDMSG);
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
	
	@Override
	protected void checkSubclass() { }
	
}
