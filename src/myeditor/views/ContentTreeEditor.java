package myeditor.views;

import lwt.action.LActionStack;
import lwt.dataestructure.LDataTree;
import lwt.editor.LDefaultTreeEditor;
import lwt.editor.LView;
import myeditor.data.Content;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridData;

public class ContentTreeEditor extends LView {

	private LDefaultTreeEditor<Content> treeEditor;
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
		treeEditor = new LDefaultTreeEditor<Content>(sashForm, SWT.NONE) {
			@Override
			public LDataTree<Content> getTree() {
				return contentTree;
			}
			@Override
			public Content createNewData() {
				return new Content("Bla", 0);
			}
			@Override
			public Content duplicateData(Content original) {
				return new Content(original.name, original.value);
			}
		};
		treeEditor.getCollection().setInsertNewEnabled(true);
		treeEditor.getCollection().setEditEnabled(false);
		treeEditor.getCollection().setDuplicateEnabled(true);
		treeEditor.getCollection().setDragEnabled(true);
		treeEditor.getCollection().setDeleteEnabled(true);
		addChild(treeEditor);
		
		contentEditor = new ContentEditor(sashForm, SWT.NONE);
		contentEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		treeEditor.addChild(contentEditor);
		
		sashForm.setWeights(new int[] {1, 2});
		
	}
	
	private LDataTree<Content> createExampleTree() {
		LDataTree<Content> root = new LDataTree<>();
		for (int i = 0; i < 3; i++) {
			String name = "item " + i;
			Content data = new Content(name, i);
			LDataTree<Content> node = new LDataTree<Content>(data, root);
			for (int j = 0; j < 3; j++) {
				name = "item " + i + " " + j;
				data = new Content(name, j);
				LDataTree<Content> subnode = new LDataTree<Content>(data, node);
				for (int k = 0; k < 3; k++) {
					name = "item " + i + " " + j + " " + k;
					data = new Content(name, k);
					new LDataTree<Content>(data, subnode);
				}
			}
	    }
		return root;
	}

}
