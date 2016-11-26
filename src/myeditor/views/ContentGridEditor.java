package myeditor.views;

import lwt.action.LActionStack;
import lwt.dataestructure.LDataList;
import lwt.editor.LDefaultGridEditor;
import lwt.editor.LView;
import myeditor.data.Content;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class ContentGridEditor extends LView {

	private LDefaultGridEditor<Content> gridEditor;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ContentGridEditor(Composite parent, int style) {
		super(parent, style);
		
		setLayout(new FillLayout());
		
		actionStack = new LActionStack(this);
		
		final LDataList<Content> contentList = createExampleList();
		gridEditor = new LDefaultGridEditor<Content>(this, SWT.NONE) {
			@Override
			public LDataList<Content> getDataCollection() {
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
		addChild(gridEditor);

	}
	
	private LDataList<Content> createExampleList() {
		LDataList<Content> list = new LDataList<>();
		for(int i = 0; i < 9; i++) {
			list.add(new Content("item " + i, i));
		}
		return list;
	}
	
}
