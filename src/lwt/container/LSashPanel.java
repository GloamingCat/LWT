package lwt.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import lwt.LFlags;

public class LSashPanel extends SashForm implements LContainer {

	 LSashPanel(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * Fill layout.
	 */
	public LSashPanel(LContainer parent, boolean horizontal) {
		super(parent.getComposite(), horizontal ? SWT.HORIZONTAL : SWT.VERTICAL);
	}

	/**
	 * Fill horizontal layout.
	 */
	public LSashPanel(LContainer parent) {
		this(parent, true);
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
	
	public void setGridData(int ax, int ay, boolean grabx, boolean graby, int cols, int rows) {
		setLayoutData(new GridData(ax, ay, grabx, graby, cols, rows));
	}
	
	public void setSpread(int cols, int rows) {
		GridData gridData = (GridData) initGridData();
		gridData.horizontalSpan = cols;
		gridData.verticalSpan = rows;
	}
	
	public void setAlignment(int a) {
		int h = SWT.FILL;
		int v = SWT.FILL;
		if ((a & LFlags.LEFT) > 0)
			h = SWT.LEFT;
		if ((a & LFlags.RIGHT) > 0)
			h = SWT.RIGHT;
		if ((a & LFlags.MIDDLE) > 0)
			h = SWT.CENTER;
		if ((a & LFlags.TOP) > 0)
			v = SWT.TOP;
		if ((a & LFlags.BOTTOM) > 0)
			v = SWT.BOTTOM;
		if ((a & LFlags.CENTER) > 0)
			v = SWT.CENTER;
		GridData gridData = initGridData();
		gridData.horizontalAlignment = h;
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
	
	public void setWeights(int... w) {
		super.setWeights(w);
	}
	
	@Override
	public Composite getComposite() {
		return this;
	}
	
	@Override
	public Object getChild(int i) {
		return getChildren()[i];
	}
	
	@Override
	protected void checkSubclass() { }
	
}
