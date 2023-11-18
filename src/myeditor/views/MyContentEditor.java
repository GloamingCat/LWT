package myeditor.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import lwt.LContainer;
import lwt.editor.LObjectEditor;
import lwt.widget.LLabel;
import lwt.widget.LSpinner;
import lwt.widget.LText;
import myeditor.MyVocab;
import myeditor.data.MyContent;

public class MyContentEditor extends LObjectEditor<MyContent> {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MyContentEditor(LContainer parent) {
		super(parent, 2, false, true);
		
		new LLabel(this, MyVocab.instance.NAME);
		
		LText text = new LText(this);
		addControl(text, "name");
		
		new LLabel(this, MyVocab.instance.VALUE);
		
		LSpinner spinner = new LSpinner(this);
		spinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		addControl(spinner, "value");
		
	}

	@Override
	public MyContent duplicateData(Object original) {
		MyContent data = (MyContent) original;
		return data.clone();
	}
	
}
