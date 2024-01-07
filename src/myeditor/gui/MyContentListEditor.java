package myeditor.gui;

import lwt.container.LContainer;
import lwt.container.LSashPanel;
import lwt.container.LView;
import lwt.dataestructure.LDataList;
import lwt.editor.LDefaultListEditor;
import myeditor.data.MyContent;
import myeditor.project.MyProject;

public class MyContentListEditor extends LView {

	private LDefaultListEditor<MyContent> listEditor;
	private MyContentEditor contentEditor;
	
	/**
	 * Create the composite.
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter parent new lwt.dialog.LShell()
	 */
	public MyContentListEditor(LContainer parent) {
		super(parent, false);
		setFillLayout(true);
		
		createMenuInterface();
		
		LSashPanel sashForm = new LSashPanel(this, true);
		
		listEditor = new MyContentList(sashForm);
		listEditor.getCollectionWidget().setInsertNewEnabled(true);
		listEditor.getCollectionWidget().setEditEnabled(false);
		listEditor.getCollectionWidget().setDuplicateEnabled(true);
		listEditor.getCollectionWidget().setDragEnabled(true);
		listEditor.getCollectionWidget().setDeleteEnabled(true);
		listEditor.getCollectionWidget().setCopyEnabled(true);
		listEditor.getCollectionWidget().setPasteEnabled(true);
		addChild(listEditor);
		
		contentEditor = new MyContentEditor(sashForm);
		contentEditor.setMargins(5, 5);
		listEditor.addChild(contentEditor);
		
		sashForm.setWeights(1, 2);
	}

	private class MyContentList extends LDefaultListEditor<MyContent> {
		public MyContentList(LContainer parent) {
			super(parent);
		}
		@Override
		public LDataList<MyContent> getDataCollection() {
			return MyProject.current.contentList;
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
