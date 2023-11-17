package myeditor.views;

import lwt.LContainer;
import lwt.action.LActionStack;
import lwt.dataestructure.LDataList;
import lwt.editor.LDefaultGridEditor;
import lwt.editor.LView;
import myeditor.data.MyContent;

public class MyContentGridEditor extends LView {

	private LDefaultGridEditor<MyContent> gridEditor;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MyContentGridEditor(LContainer parent) {
		super(parent, true, false);
		
		actionStack = new LActionStack(this);
		
		final LDataList<MyContent> contentList = createExampleList();
		gridEditor = new LDefaultGridEditor<MyContent>(this) {
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
		addChild(gridEditor);

	}
	
	private LDataList<MyContent> createExampleList() {
		LDataList<MyContent> list = new LDataList<>();
		for(int i = 0; i < 9; i++) {
			list.add(new MyContent("item " + i, i));
		}
		return list;
	}
	
}
