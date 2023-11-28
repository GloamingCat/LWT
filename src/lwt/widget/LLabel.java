package lwt.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;

import lwt.LFlags;
import lwt.container.LContainer;

public class LLabel extends LWidget {

	private Label label;
	
	LLabel (Composite parent, int style) {
		super(parent, style);
		label = new Label(this, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
	
	LLabel(Composite parent, int style, int hfill, int vfill) {
		super(parent, style);
		label = null;
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, false, hfill, vfill);
		gridData.widthHint = 0;
		gridData.heightHint = 0;
		setLayoutData(gridData);
	}

	public LLabel(LContainer parent, int style, String text, int columns) {
		this(parent.getComposite(), SWT.NONE);
		label.setText(text);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 3;
		setLayout(gridLayout);
		boolean expand = (style & LFlags.EXPAND) > 0;
		if ((style & LFlags.TOP) > 0) {
			setLayoutData(new GridData(SWT.LEFT, SWT.TOP, expand, false, columns, 1));
		} else if ((style & LFlags.BOTTOM) > 0) {
			setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, expand, false, columns, 1));
		} else if ((style & LFlags.RIGHT) > 0) {
			setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, expand, false, columns, 1));			
		} else if ((style & LFlags.CENTER) > 0) {
			setLayoutData(new GridData(SWT.FILL, SWT.FILL, expand, false, columns, 1));
			label.setAlignment(SWT.CENTER);
		} else {
			setLayoutData(new GridData(SWT.FILL, SWT.FILL, expand, false, columns, 1));
		}
	}

	public LLabel(LContainer parent, int style, String text) {
		this(parent, style, text, 1);
	}
	
	public LLabel(LContainer parent, String text, int columns) {
		this(parent, 0, text, columns);
	}
	
	public LLabel(LContainer parent, String text) {
		this(parent, 0, text, 1);
	}

	public LLabel(LContainer parent, int hfill, int vfill) {
		this(parent.getComposite(), SWT.NONE, hfill, vfill);
	}

	/**
	 * @wbp.parser.constructor
	 */
	public LLabel(LContainer parent) {
		this(parent, "Text");
	}

	public void setText(String text) {
		label.setText(text);
	}

	@Override
	protected void onCopyButton(Menu menu) {}

	@Override
	protected void onPasteButton(Menu menu) {}

}
