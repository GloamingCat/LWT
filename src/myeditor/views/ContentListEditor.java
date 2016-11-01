package myeditor.views;

import lwt.action.LActionStack;
import lwt.dataestructure.LDataList;
import lwt.editor.LDefaultListEditor;
import lwt.editor.LView;
import myeditor.data.Content;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public class ContentListEditor extends LView {

	private LDefaultListEditor<Content> listEditor;
	private ContentEditor contentEditor;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ContentListEditor(Composite parent, int style) {
		super(parent, style);
		
		setLayout(new FillLayout());
		
		actionStack = new LActionStack(this);
		
		SashForm sashForm = new SashForm(this, SWT.NONE);
		
		final LDataList<Content> contentList = createExampleList();
		listEditor = new LDefaultListEditor<Content>(sashForm, SWT.NONE) {
			@Override
			public LDataList<Content> getList() {
				return contentList;
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
		listEditor.setInsertNewEnabled(true);
		listEditor.setEditEnabled(false);
		listEditor.setDuplicateEnabled(true);
		listEditor.setDragEnabled(true);
		listEditor.setDeleteEnabled(true);
		addChild(listEditor);
		
		contentEditor = new ContentEditor(sashForm, SWT.NONE);
		contentEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		listEditor.addChild(contentEditor);
		
		sashForm.setWeights(new int[] {1, 2});
	}
	
	private LDataList<Content> createExampleList() {
		LDataList<Content> list = new LDataList<>();
		for(int i = 0; i < 9; i++) {
			list.add(new Content("item " + i, i));
		}
		return list;
	}
	
}
