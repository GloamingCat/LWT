package lwt.dialog;

import lbase.LVocab;
import lwt.widget.LLabel;
import lwt.widget.LText;

public class LStringWindow extends LObjectWindow<String> {
	
	private LText txtName;

	public LStringWindow(LWindow parent, String title) {
		super(parent, title);
		content.setGridLayout(2);
		new LLabel(content, LVocab.instance.TEXT);
		txtName = new LText(content);
		txtName.setExpand(true, false);
		pack();
	}
	
	public void open(String initial) {
		super.open(initial);
		txtName.setValue(initial);
	}

	@Override
	protected String createResult(String initial) {
		if (txtName.getValue().equals(initial)) {
			return null;
		} else {
			return txtName.getValue();
		}
	}

}
