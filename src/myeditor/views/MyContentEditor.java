package myeditor.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridData;

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
	public MyContentEditor(Composite parent, int style) {
		super(parent, style);
		
		setLayout(new GridLayout(2, false));
		
		new LLabel(this, MyVocab.instance.NAME);
		
		LText text = new LText(this);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		addControl(text, "name");
		
		new LLabel(this, MyVocab.instance.VALUE);
		
		LSpinner spinner = new LSpinner(this, SWT.NONE);
		spinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		addControl(spinner, "value");
		
	}

	@Override
	public MyContent duplicateData(MyContent obj) {
		return obj.clone();
	}
	
}
