package tests;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;

import lwt.editor.LEditor;
import lwt.widget.LSpinner;
import lwt.widget.LText;

public class ContentEditor extends LEditor {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ContentEditor(Composite parent, int style) {
		super(parent, style);
		
		setLayout(new GridLayout(2, false));
		
		Label lblName = new Label(this, SWT.NONE);
		lblName.setText("Name");
		
		LText text = new LText(this, SWT.NONE);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblValue = new Label(this, SWT.NONE);
		lblValue.setText("Value");
		
		LSpinner spinner = new LSpinner(this, SWT.NONE);
		spinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
	}
	
}
