package myeditor.gui;

import lbase.data.LDataList;
import lwt.container.LContainer;
import lwt.container.LFlexPanel;
import lwt.container.LView;
import lwt.editor.LDefaultGridEditor;
import myeditor.data.MyContent;
import myeditor.project.MyProject;

public class MyContentGridEditor extends LView {

	private LDefaultGridEditor<MyContent> gridEditor;
	private MyContentEditor contentEditor;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MyContentGridEditor(LContainer parent) {
		super(parent, false);
		setFillLayout(true);
		
		createMenuInterface();
		
		LFlexPanel sashForm = new LFlexPanel(this, true);
		
		gridEditor = new MyContentGrid(sashForm);
		gridEditor.getCollectionWidget().cellWidth = 40;
		gridEditor.getCollectionWidget().cellHeight = 40;
		gridEditor.getCollectionWidget().setColumns(4);
		addChild(gridEditor);
		
		contentEditor = new MyContentEditor(sashForm);
		contentEditor.setMargins(5, 5);
		gridEditor.addChild(contentEditor);
		
		sashForm.setWeights(1, 2);

	}

	private class MyContentGrid extends LDefaultGridEditor<MyContent> {
		public MyContentGrid(LContainer parent) {
			super(parent);
		}
		@Override
		public LDataList<MyContent> getDataCollection() {
			return MyProject.current.contentGrid;
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
