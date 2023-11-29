package lwt.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import lwt.LFlags;
import lwt.dialog.LShell;

public class LPanel extends Composite implements LContainer {

	//////////////////////////////////////////////////
	// {{ Internal Constructors
	
	/**
	 * Internal, no layout.
	 */
	LPanel(Composite parent, int style) {
		super(parent, style);
	}
	
	/**
	 * Internal, with fill layout.
	 */
	LPanel(Composite parent, boolean horizontal, int style) {
		super(parent, style);
		setFillLayout(horizontal);
	}

	/**
	 * Internal, with grid layout.
	 */
	LPanel(Composite parent, int columns, boolean equalCols, int style) {
		super(parent, style);
		setGridLayout(columns, equalCols);
	}

	/**
	 * Internal, with fill or row layout.
	 */
	LPanel(Composite parent, boolean horizontal, boolean equalCells, int style) {
		super(parent, style);
		setLinearLayout(horizontal, equalCells);
	}
		
	// }}
	
	//////////////////////////////////////////////////
	// {{ Public Constructors
	
	/** Grid layout.
	 */
	public LPanel(LContainer parent, int columns, boolean equalCols) {
		this(parent.getComposite(), columns, equalCols, SWT.NONE);
	}
	
	/** Grid layout with unequal columns.
	 */
	public LPanel(LContainer parent, int columns) {
		this(parent, columns, false);
	}
	
	/** Fill/row layout.
	 */
	public LPanel(LContainer parent, boolean horizontal, boolean equalCells) {
		this(parent.getComposite(), horizontal, equalCells, SWT.NONE);
	}
	
	/** Fill layout with no margin.
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter parent new LShell(800, 600)
	 * @wbp.eval.method.parameter horizontal true
	 */
	public LPanel(LContainer parent, boolean horizontal) {
		this(parent.getComposite(), horizontal, SWT.NONE);
	}

	/** No layout.
	 * @param parent
	 */
	public LPanel(LContainer parent) {
		this(parent.getComposite(), SWT.NONE);
	}
	
	// }}
	
	//////////////////////////////////////////////////
	// {{ Inner Layout
	
	public void setFillLayout(boolean horizontal) {
		if (horizontal) {
			setLayout(new FillLayout(SWT.HORIZONTAL));
		} else {
			setLayout(new FillLayout(SWT.VERTICAL));
		}
	}
	
	public void setGridLayout(int columns, boolean equalCols) {
		GridLayout gl = new GridLayout(columns, equalCols);
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		setLayout(gl);
	}
	
	public void setLinearLayout(boolean horizontal, boolean equalCells) {
		int dir = horizontal ? SWT.HORIZONTAL : SWT.VERTICAL;
		if (equalCells) {
			FillLayout layout = new FillLayout(dir);
			layout.spacing = 5;
			setLayout(layout);
		} else {
			RowLayout layout = new RowLayout(dir);
			layout.spacing = 5;
			setLayout(layout);
		}
	}
	
	// }}
	
	//////////////////////////////////////////////////
	// {{ Parent Layout

	GridData initGridData() {
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
	public LShell getShell() {
		return (LShell) super.getShell();
	}
	
	@Override
	protected void checkSubclass() { }
	
}
