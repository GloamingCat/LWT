package myeditor.views;

import lwt.action.LActionStack;
import lwt.container.LContainer;
import lwt.container.LSashPanel;
import lwt.container.LView;
import lwt.dataestructure.LDataList;
import lwt.editor.LDefaultListEditor;
import myeditor.data.MyContent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

public class MyContentListEditor extends LView {

	private LDefaultListEditor<MyContent> listEditor;
	private MyContentEditor contentEditor;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MyContentListEditor(LContainer parent) {
		super(parent, true, false);
		
		actionStack = new LActionStack(this);
		
		LSashPanel sashForm = new LSashPanel(this, true);
		
		final LDataList<MyContent> contentList = createExampleList();
		listEditor = new LDefaultListEditor<MyContent>(sashForm) {
			@Override
			public LDataList<MyContent> getDataCollection() {
				return contentList;
			}
			@Override
			public MyContent createNewData() {
				return new MyContent("Bla", 0);
			}
			@Override
			public MyContent duplicateData(MyContent original) {
				return new MyContent(original.name, original.value);
			}
		};
		listEditor.getCollectionWidget().setInsertNewEnabled(true);
		listEditor.getCollectionWidget().setEditEnabled(false);
		listEditor.getCollectionWidget().setDuplicateEnabled(true);
		listEditor.getCollectionWidget().setDragEnabled(true);
		listEditor.getCollectionWidget().setDeleteEnabled(true);
		addChild(listEditor);
		
		contentEditor = new MyContentEditor(sashForm);
		contentEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		listEditor.addChild(contentEditor);
		
		sashForm.setWeights(new int[] {1, 2});
	}
	
	private LDataList<MyContent> createExampleList() {
		LDataList<MyContent> list = new LDataList<>();
		for(int i = 0; i < 9; i++) {
			list.add(new MyContent("item " + i, i));
		}
		return list;
	}
	
}
