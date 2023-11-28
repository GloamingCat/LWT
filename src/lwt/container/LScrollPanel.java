package lwt.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import lwt.LFlags;

public class LScrollPanel extends ScrolledComposite implements LContainer {

	/**
	 * Internal, no layout.
	 */
	LScrollPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
	}
	
	/**
	 * Internal, with fill layout.
	 */
	LScrollPanel(Composite parent, boolean horizontal, int style) {
		super(parent, style);
	}

	/** Fill layout with no margin.
	 * @param parent
	 * @param horizontal
	 */
	public LScrollPanel(LContainer parent, boolean large) {
		this(parent.getComposite(), SWT.V_SCROLL | SWT.H_SCROLL);
		if (large) {
			setExpandVertical(true);
			setExpandHorizontal(true);
		}
	}

	/** No layout.
	 * @param parent
	 */
	public LScrollPanel(LContainer parent) {
		this(parent, false);
	}

	//////////////////////////////////////////////////
	// {{ Layout

	public GridData initGridData() {
		Object ld = getLayoutData();
		if (ld != null) {
			return (GridData) ld;
		} else {
			GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
			setLayoutData(gd);
			return gd;
		}
	}
	
	public void setSpread(int cols, int rows) {
		GridData gridData = (GridData) initGridData();
		gridData.horizontalSpan = cols;
		gridData.verticalSpan = rows;
	}
	
	public void setAlignment(int a) {
		GridData gridData = initGridData();
		int h = SWT.FILL;
		if ((a & LFlags.LEFT) > 0)
			h = SWT.LEFT;
		if ((a & LFlags.RIGHT) > 0)
			h = SWT.RIGHT;
		if ((a & LFlags.MIDDLE) > 0)
			h = SWT.CENTER;
		gridData.horizontalAlignment = h;
		int v = SWT.FILL;
		if ((a & LFlags.TOP) > 0)
			v = SWT.TOP;
		if ((a & LFlags.BOTTOM) > 0)
			v = SWT.BOTTOM;
		if ((a & LFlags.CENTER) > 0)
			v = SWT.CENTER;		
		gridData.verticalAlignment = v;
	}

	public void setExpand(boolean h, boolean v) {
		initGridData();
		GridData gridData = initGridData();
		gridData.grabExcessHorizontalSpace = h;
		gridData.grabExcessVerticalSpace = v;
	}
	
	public void setMinimumWidth(int w) {
		GridData gridData = initGridData();
		gridData.minimumWidth = w;
		gridData.widthHint = w;
	}
	
	public void setMinimumHeight(int h) {
		GridData gridData = initGridData();
		gridData.minimumHeight = h;
		gridData.heightHint = h;
	}
	
	// }}

	@Override
	public Composite getComposite() {
		return this;
	}

	@Override
	protected void checkSubclass() { }
	
}
