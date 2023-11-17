package lwt.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class LLabel extends Composite {

	// Style
	public static final int TOP = 0x1;
	public static final int CENTER = 0x01;

	private Label label;

	public LLabel(Composite parent, String text, int style) {
		super(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 3;
		setLayout(gridLayout);
		label = new Label(this, SWT.NONE);
		label.setText(text);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		if ((style & TOP) > 0) {
			setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		} else {
			setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		}
		if ((style & CENTER) > 0)
			label.setAlignment(SWT.CENTER);
	}

	public LLabel(Composite parent, String text) {
		this(parent, text, 0);
	}

	public LLabel(Composite parent, int hfill, int vfill) {
		super(parent, SWT.NONE);
		label = null;
		setSize(0, 0);
		GridData gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false, hfill, vfill);
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

}
