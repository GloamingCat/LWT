package tests;

import lwt.action.LActionStack;
import lwt.dataestructure.LDataTree;
import lwt.editor.LEditor;
import lwt.editor.LTreeEditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridData;

public class ContentTreeEditor extends LEditor {

	private LTreeEditor<Content> treeEditor;
	private ContentEditor contentEditor;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ContentTreeEditor(Composite parent, int style) {
		super(parent, style);
		
		setLayout(new FillLayout());
		
		actionStack = new LActionStack(this);
		
		SashForm sashForm = new SashForm(this, SWT.NONE);
		
		final LDataTree<Content> contentTree = createExampleTree();
		treeEditor = new LTreeEditor<Content>(sashForm, SWT.NONE) {
			@Override
			public LDataTree<Content> getTree() {
				return contentTree;
			}
		};
		treeEditor.setInsertNewEnabled(true);
		treeEditor.setEditEnabled(false);
		treeEditor.setDuplicateEnabled(true);
		treeEditor.setDragEnabled(true);
		treeEditor.setDeleteEnabled(true);
		addSubEditor(treeEditor);
		
		contentEditor = new ContentEditor(sashForm, SWT.NONE);
		contentEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		addSubEditor(contentEditor);
		
		sashForm.setWeights(new int[] {1, 2});
		
	}
	
	private LDataTree<Content> createExampleTree() {
		LDataTree<Content> root = new LDataTree<>();
		for (int i = 0; i < 3; i++) {
			LDataTree<Content> node = new LDataTree<>();
			String name = "item " + i;
			root.children.add(node);
			node.data = new Content(name, i);
			for (int j = 0; j < 3; j++) {
				LDataTree<Content> subNode = new LDataTree<>();
				name = "item " + i + " " + j;
				node.children.add(subNode);
				subNode.data = new Content(name, j);
				for (int k = 0; k < 3; k++) {
					LDataTree<Content> subsubNode = new LDataTree<>();
					name = "item " + i + " " + j + " " + k;
					subNode.children.add(subsubNode);
					subsubNode.data = new Content(name, k);
				}
			}
	    }
		return root;
	}

}
