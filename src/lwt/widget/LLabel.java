package lwt.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class LLabel extends Composite {

	private Label label;
	
	public LLabel(Composite parent, String text) {
		super(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		label = new Label(this, SWT.NONE);
		label.setText(text);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		label.setLayoutData(gridData);
	}
	
	public LLabel(Composite parent, int fill) {
		super(parent, SWT.NONE);
		label = null;
		setSize(0, 0);
		GridData gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false, fill, 1);
		gridData.heightHint = 0;
		setLayoutData(gridData);
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public LLabel(Composite parent) {
		this(parent, "Text");
	}
	
	public void setText(String text) {
		label.setText(text);
	}
	
	public void setAlignment(int alignment) {
		label.setAlignment(alignment);
	}
	
}
