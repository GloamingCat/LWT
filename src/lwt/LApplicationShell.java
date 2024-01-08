package lwt;

import lwt.action.LActionManager;
import lwt.container.LContainer;
import lwt.container.LView;
import lwt.dataserialization.LFileManager;
import lwt.dataserialization.LSerializer;
import lwt.dialog.LErrorDialog;
import lwt.dialog.LFileDialog;
import lwt.dialog.LConfirmDialog;
import lwt.dialog.LShell;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.wb.swt.SWTResourceManager;

public abstract class LApplicationShell extends LShell implements LContainer {

	protected LSerializer project = null;
	protected String applicationName;

	protected ArrayList<LView> views = new ArrayList<>();
	protected LView defaultView = null;
	protected LView currentView;
	protected StackLayout stackLayout;

	public Menu menuProject;
	public Menu menuEdit;
	public Menu menuView;
	public Menu menuHelp;
	protected MenuItem mntmView;
	protected MenuItem mntmUndo;
	protected MenuItem mntmRedo;
	protected MenuItem mntmCopy;
	protected MenuItem mntmPaste;
	protected MenuItem mntmDelete;
	protected MenuItem mntmEdit;

	/**
	 * Create the shell.
	 * @wbp.eval.method.parameter initialWidth 800
	 * @wbp.eval.method.parameter initialHeight 600
	 */
	public LApplicationShell(int initialWidth, int initialHeight, String title, String icon) {
		super();
		SWTResourceManager.rootClass = getClass();
		setSize(new Point(initialWidth, initialHeight));
		if (title != null) {
			setText(title);
			applicationName = "LTH Editor";
		}
		if (icon != null) {
			setImage(SWTResourceManager.getImage(LApplicationShell.class, icon));
		}

		LVocab vocab = LVocab.instance;

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

		mntmUndo = new MenuItem(menuEdit, SWT.NONE);
		mntmUndo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				currentView.getActionStack().undo();
			}
		});
		mntmUndo.setAccelerator(SWT.MOD1 | 'Z');
		mntmUndo.setText(vocab.UNDO + "\t Ctrl + &Z");
		mntmUndo.setEnabled(false);

		mntmRedo = new MenuItem(menuEdit, SWT.NONE);
		mntmRedo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				currentView.getActionStack().redo();
			}
		});
		mntmRedo.setAccelerator(SWT.MOD1 | 'Y');
		mntmRedo.setText(vocab.REDO + "\t Ctrl + &Y");
		mntmRedo.setEnabled(false);
		
		new MenuItem(menuEdit, SWT.SEPARATOR);
		
		mntmCopy = new MenuItem(menuEdit, SWT.NONE);
		mntmCopy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				currentView.getMenuInterface().copy();
			}
		});
		mntmCopy.setAccelerator(SWT.MOD1 | 'C');
		mntmCopy.setText(vocab.COPY + "\t Ctrl + &C");
		mntmCopy.setEnabled(false);

		mntmPaste = new MenuItem(menuEdit, SWT.NONE);
		mntmPaste.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				currentView.getMenuInterface().paste();
			}
		});
		mntmPaste.setAccelerator(SWT.MOD1 | 'V');
		mntmPaste.setText(vocab.PASTE + "\t Ctrl + &V");
		mntmPaste.setEnabled(false);

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
		LGlobals.clipboard.dispose();
		System.exit(0);
	}

	protected void addView(final LView view, String name, String shortcut) {
		MenuItem item = new MenuItem(menuView, SWT.NONE);
		item.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				setCurrentView(view);
			}
		});
		views.add(view);
		if (shortcut != null) {
			item.setText(name + "\t" + shortcut);
			item.setAccelerator(LGlobals.accelerators.get(shortcut));
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
			LErrorDialog msg = new LErrorDialog(this,
					vocab.LOADERROR,
					vocab.LOADERRORMSG + "\n" + path);
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
		setRedraw(false);
		layout();
		currentView.onVisible();
		refreshEditButtons();
		setRedraw(true);
	}
	
	public void refreshEditButtons() {
		mntmUndo.setEnabled(currentView.getActionStack().canUndo());
		mntmRedo.setEnabled(currentView.getActionStack().canRedo());
	}
	
	public void refreshClipboardButtons() {
		mntmCopy.setEnabled(currentView.getMenuInterface().canCopy());
		mntmPaste.setEnabled(currentView.getMenuInterface().canPaste());
	}

	protected abstract LSerializer createProject(String path);

	public LSerializer newProject() {
		if (!askSave()) {
			return project;
		}
		LVocab vocab = LVocab.instance;
		LFileDialog dialog = new LFileDialog(this, vocab.NEWPROJECT, false);
		String resultPath = dialog.open();
		if (resultPath == null)
			return project;
		LSerializer newProject = createProject(resultPath);
		if (newProject.isDataFolder(resultPath)) {
			LConfirmDialog msg = new LConfirmDialog(this, 
					vocab.EXISTINGPROJECT,
					vocab.EXISTINGMSG,
					LConfirmDialog.OK_CANCEL);
			int result = msg.open();
			if (result != LConfirmDialog.YES) {
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
		LFileDialog dialog = new LFileDialog(this, vocab.OPENPROJECT, true);
		String resultFile = dialog.open();
		if (resultFile == null)
			return project;
		LSerializer previous = project;
		System.out.println("Opened: " + resultFile);
		project = createProject(resultFile);
		if (project.load()) {
			mntmView.setEnabled(true);
			String path = LFileManager.appDataPath(applicationName) + "lattest.txt";
			byte[] bytes = resultFile.getBytes();
			LFileManager.save(path, bytes);
			for (LView view : views)
				view.restart();
			if (defaultView != null)
				setCurrentView(defaultView);
		} else {
			LErrorDialog msg = new LErrorDialog(this,
					vocab.LOADERROR,
					vocab.LOADERRORMSG + ":" + resultFile);
			msg.open();
			project = previous;
		}
		return project;
	}

	public void saveProject() {
		if (project == null || !LActionManager.getInstance().hasChanges())
			return;
		if (!project.save()) {
			LVocab vocab = LVocab.instance;
			LErrorDialog msg = new LErrorDialog(this,
					vocab.SAVEERROR,
					vocab.SAVEERRORMSG);
			msg.open();
		} else {
			LActionManager.getInstance().onSave();
		}
	}

	protected boolean askSave() {
		if (project != null && LActionManager.getInstance().hasChanges()) {
			LVocab vocab = LVocab.instance;
			LConfirmDialog msg = new LConfirmDialog(this, 
					vocab.UNSAVEDPROJECT,
					vocab.UNSAVEDMSG,
					LConfirmDialog.YES_NO_CANCEL);
			int result = msg.open();
			if (result == LConfirmDialog.YES) {
				saveProject();
				return true;
			} else if (result == LConfirmDialog.NO) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	public Composite getComposite() {
		return this;
	}

}
