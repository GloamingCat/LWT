package myeditor.views;

import lwt.action.LActionStack;
import lwt.dataestructure.LDataList;
import lwt.editor.LDefaultListEditor;
import lwt.editor.LView;
import myeditor.data.MyContent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public class MyContentListEditor extends LView {

	private LDefaultListEditor<MyContent> listEditor;
	private MyContentEditor contentEditor;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MyContentListEditor(Composite parent, int style) {
		super(parent, style);
		
		setLayout(new FillLayout());
		
		actionStack = new LActionStack(this);
		
		SashForm sashForm = new SashForm(this, SWT.NONE);
		
		final LDataList<MyContent> contentList = createExampleList();
		listEditor = new LDefaultListEditor<MyContent>(sashForm, SWT.NONE) {
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
		
		contentEditor = new MyContentEditor(sashForm, SWT.NONE);
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
