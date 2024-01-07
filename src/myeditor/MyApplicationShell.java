package myeditor;

import lwt.LApplicationShell;
import lwt.dataserialization.LSerializer;
import myeditor.gui.MyContentGridEditor;
import myeditor.gui.MyContentListEditor;
import myeditor.gui.MyContentTreeEditor;
import myeditor.project.MyProject;

public class MyApplicationShell extends LApplicationShell {
	
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
		
		addView(treeEditor, MyVocab.instance.CONTENTTREE, "F2");
		addView(listEditor, MyVocab.instance.CONTENTLIST, "F3");
		addView(gridEditor, MyVocab.instance.CONTENTGRID, "F4");
		
		defaultView = treeEditor;
	}
	
	@Override
	protected LSerializer createProject(String path) {
		return new MyProject(path);
	}

}
