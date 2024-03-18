package myeditor;

import lbase.serialization.LSerializer;
import lwt.LApplicationWindow;
import myeditor.gui.MyConfigEditor;
import myeditor.gui.MyContentGridEditor;
import myeditor.gui.MyContentListEditor;
import myeditor.gui.MyContentTreeEditor;
import myeditor.project.MyProject;

public class MyApplicationShell extends LApplicationWindow {

	/**
	 * Launch the application.
	 * 
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
	 * 
	 * @param display
	 */
	public MyApplicationShell() {
		super(450, 300, "My Editor", null);
		applicationName = "LWT Test";

		MyContentTreeEditor treeEditor = new MyContentTreeEditor(stack);
		MyContentListEditor listEditor = new MyContentListEditor(stack);
		MyContentGridEditor gridEditor = new MyContentGridEditor(stack);
		MyConfigEditor configEditor = new MyConfigEditor(stack);

		addView(treeEditor, MyVocab.instance.CONTENTTREE, "F2");
		addView(listEditor, MyVocab.instance.CONTENTLIST, "F3");
		addView(gridEditor, MyVocab.instance.CONTENTGRID, "F4");
		addView(configEditor, MyVocab.instance.CONTENTTYPES, "F5");

		defaultView = treeEditor;
		loadDefault();
	}

	@Override
	protected LSerializer createProject(String path) {
		return new MyProject(path);
	}

}
