package myeditor.gui;

import lwt.container.LContainer;
import lwt.container.LSashPanel;
import lwt.container.LView;
import lwt.dataestructure.LDataTree;
import lwt.editor.LDefaultTreeEditor;
import myeditor.data.MyContent;
import myeditor.project.MyProject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

public class MyContentTreeEditor extends LView {

	private LDefaultTreeEditor<MyContent> treeEditor;
	private MyContentEditor contentEditor;
	
	public MyContentTreeEditor(LContainer parent) {
		super(parent, true, false);
		
		createMenuInterface();
		
		LSashPanel sashForm = new LSashPanel(this, true);
		
		treeEditor = new MyContentTree(sashForm);
		treeEditor.getCollectionWidget().setInsertNewEnabled(true);
		treeEditor.getCollectionWidget().setEditEnabled(false);
		treeEditor.getCollectionWidget().setDuplicateEnabled(true);
		treeEditor.getCollectionWidget().setDragEnabled(true);
		treeEditor.getCollectionWidget().setDeleteEnabled(true);
		treeEditor.getCollectionWidget().setCopyEnabled(true);
		treeEditor.getCollectionWidget().setPasteEnabled(true);
		addChild(treeEditor);
		
		contentEditor = new MyContentEditor(sashForm);
		contentEditor.setMargins(5, 5);
		contentEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		treeEditor.addChild(contentEditor);
		
		sashForm.setWeights(new int[] {1, 2});
		
	}
	
	private class MyContentTree extends LDefaultTreeEditor<MyContent> {
		public MyContentTree(LContainer parent) {
			super(parent);
		}
		@Override
		public LDataTree<MyContent> getDataCollection() {
			return MyProject.current.contentTree;
		}
		@Override
		public MyContent createNewElement() {
			return new MyContent("New Element");
		}
		@Override
		public MyContent duplicateElement(MyContent original) {
			return original.clone();
		}
		@Override
		protected String encodeElement(MyContent data) {
			return data.encode();
		}
		@Override
		protected MyContent decodeElement(String str) {
			return MyContent.decode(str);
		}
		@Override
		public boolean canDecode(String str) {
			return MyContent.canDecode(str);
		}
	}

}
