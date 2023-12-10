package myeditor.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import lwt.container.LContainer;
import lwt.editor.LObjectEditor;
import lwt.widget.LLabel;
import lwt.widget.LSpinner;
import lwt.widget.LText;
import myeditor.MyVocab;
import myeditor.data.MyContent;

public class MyContentEditor extends LObjectEditor<MyContent> {

	/**
	 * Create the composite.
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter parent new lwt.dialog.LShell()
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
	public MyContent duplicateData(MyContent original) {
		return original.clone();
	}

	@Override
	public String encodeData(MyContent obj) {
		return obj.encode();
	}

	@Override
	public MyContent decodeData(String str) {
		return MyContent.decode(str);
	}
	
}
