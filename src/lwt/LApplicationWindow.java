package lwt;

import lwt.container.LContainer;
import lwt.container.LStack;
import lwt.container.LView;
import lwt.dialog.LErrorDialog;
import lwt.dialog.LFileDialog;
import lwt.dialog.LConfirmDialog;
import lwt.dialog.LWindow;
import lwt.editor.LSubMenu;
import lwt.editor.LMenuBar;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.SWTResourceManager;

import lbase.LVocab;
import lbase.action.LActionManager;
import lbase.serialization.LFileManager;
import lbase.serialization.LSerializer;

public abstract class LApplicationWindow extends LWindow implements LContainer, lbase.gui.LApplicationWindow {

	protected LSerializer project = null;
	protected String applicationName;
	protected String projectExtension = "txt";

	protected ArrayList<LView> views = new ArrayList<>();
	protected LView defaultView = null;
	protected LView currentView;
	protected LStack stack;

	public LMenuBar menuBar;
	public LSubMenu menuProject;
	public LSubMenu menuEdit;
	public LSubMenu menuView;
	public LSubMenu menuHelp;

	/**
	 * Create the shell.
	 * @wbp.eval.method.parameter initialWidth 800
	 * @wbp.eval.method.parameter initialHeight 600
	 */
	public LApplicationWindow(int initialWidth, int initialHeight, String title, String icon) {
		super();
		SWTResourceManager.rootClass = getClass();
		setSize(new Point(initialWidth, initialHeight));
		if (title != null) {
			setText(title);
			applicationName = "LTH Editor";
		}
		if (icon != null) {
			setImage(SWTResourceManager.getImage(LApplicationWindow.class, icon));
		}

		LVocab vocab = LVocab.instance;

		stack = new LStack(this);

		addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				event.doit = askSave();
			}
		});

		menuBar = new LMenuBar(this);

		menuProject = menuBar.addSubMenu(vocab.PROJECT, "project");
		menuProject.addMenuButton(vocab.NEW, "new", (d) -> project = newProject(), "Ctrl+&N");
		menuProject.addMenuButton(vocab.OPEN, "open", (d) -> project = openProject(), "Ctrl+&O");
		menuProject.addMenuButton(vocab.SAVE, "save", (d) -> saveProject(), "Ctrl+&S");
		menuProject.addSeparator();
		menuProject.addMenuButton(vocab.EXIT, "exit", (d) -> close(), "Alt+F4");

		menuEdit = menuBar.addSubMenu(vocab.EDIT, "edit");
		menuEdit.addMenuButton(vocab.UNDO, "undo", (d) -> currentView.getActionStack().undo(), "Ctrl+&Z");
		menuEdit.setButtonEnabled("undo", false);
		menuEdit.addMenuButton(vocab.REDO, "redo", (d) -> currentView.getActionStack().redo(), "Ctrl+&Y");
		menuEdit.setButtonEnabled("redo", false);
		menuEdit.addSeparator();
		menuEdit.addMenuButton(vocab.COPY, "copy", (d) -> currentView.getMenuInterface().copy(), "Ctrl+&C");
		menuEdit.setButtonEnabled("copy", false);
		menuEdit.addMenuButton(vocab.PASTE, "paste", (d) -> currentView.getMenuInterface().paste(), "Ctrl+&V");
		menuEdit.setButtonEnabled("paste", false);

		menuView = menuBar.addSubMenu(vocab.VIEW, "view");
		menuBar.setMenuEnabled("view", false);
		menuHelp = menuBar.addSubMenu(vocab.HELP, "help");
		menuBar.setMenuEnabled("help", false);

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
		menuView.addMenuButton(name, name, (d) -> setCurrentView(view), shortcut);
		views.add(view);
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
			menuBar.setMenuEnabled("view", true);
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
		setRedraw(false);
		stack.setTop(currentView);
		currentView.onVisible();
		refreshEditButtons();
		setRedraw(true);
	}
	
	@Override
	public void refreshEditButtons() {
		menuProject.setButtonEnabled("save", project != null && LActionManager.getInstance().hasChanges());
		menuEdit.setButtonEnabled("undo", currentView.getActionStack().canUndo());
		menuEdit.setButtonEnabled("redo", currentView.getActionStack().canRedo());
	}
	
	@Override
	public void refreshClipboardButtons() {
		menuEdit.setButtonEnabled("copy", currentView.getMenuInterface().canCopy());
		menuEdit.setButtonEnabled("paste", currentView.getMenuInterface().canPaste());
	}
	
	@Override
	public LSubMenu getEditMenu() {
		return menuEdit;
	}

	protected abstract LSerializer createProject(String path);

	public LSerializer newProject() {
		if (!askSave()) {
			return project;
		}
		LVocab vocab = LVocab.instance;
		LFileDialog dialog = new LFileDialog(this, vocab.NEWPROJECT, projectExtension, true);
		String resultPath = dialog.open();
		if (resultPath == null)
			return project;
		LSerializer newProject = createProject(resultPath);
		if (newProject.isDataFolder(LFileManager.getDirectory(resultPath))) {
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
		menuBar.setMenuEnabled("view", true);
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
		LFileDialog dialog = new LFileDialog(this, vocab.OPENPROJECT, projectExtension, false);
		String resultFile = dialog.open();
		if (resultFile == null)
			return project;
		LSerializer previous = project;
		System.out.println("Opened: " + resultFile);
		project = createProject(resultFile);
		if (project.load()) {
			menuBar.setMenuEnabled("view", true);
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

	public Composite getContentComposite() {
		return this;
	}

	@Override
	public LWindow getWindow() {
		return this;
	}

}
