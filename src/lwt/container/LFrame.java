package lwt.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import lwt.LFlags;
import lwt.widget.LWidget;

public class LFrame extends Group implements LContainer {
	

	/**
	 * Internal, no layout.
	 */
	LFrame(Composite parent, int style) {
		super(parent, style);
	}
	
	/**
	 * Internal, with fill layout.
	 */
	LFrame(Composite parent, boolean horizontal, int style) {
		super(parent, style);
		if (horizontal) {
			setLayout(new FillLayout(SWT.HORIZONTAL));
		} else {
			setLayout(new FillLayout(SWT.VERTICAL));
		}
	}
	
	/**
	 * Internal, with grid layout.
	 */
	LFrame(Composite parent, int columns, boolean equalCols, int style) {
		super(parent, style);
		setLayout(new GridLayout(columns, equalCols));
	}
	
	/**
	 * Internal, with fill or row layout.
	 */
	LFrame(Composite parent, boolean horizontal, boolean equalCells, int style) {
		super(parent, style);
		int dir = horizontal ? SWT.HORIZONTAL : SWT.VERTICAL;
		if (equalCells) {
			FillLayout layout = new FillLayout(dir);
			layout.spacing = 5;
			layout.marginWidth = 5;
			layout.marginHeight = 5;
			setLayout(layout);
		} else {
			RowLayout layout = new RowLayout(dir);
			layout.spacing = 5;
			layout.marginWidth = 5;
			layout.marginHeight = 5;
			setLayout(layout);
		}
	}
	
	/** Grid layout.
	 * @param parent
	 * @param columns
	 * @param equalCols
	 */
	public LFrame(LContainer parent, String title, int columns, boolean equalCols) {
		this(parent.getComposite(), columns, equalCols, SWT.NONE);
		setText(title);
	}
	
	/** Fill/row layout.
	 * @param parent
	 * @param horizontal
	 */
	public LFrame(LContainer parent, String title, boolean horizontal, boolean equalCells) {
		this(parent.getComposite(), horizontal, equalCells, SWT.NONE);
		setText(title);
	}
	
	/** Fill layout with no margin.
	 * @param parent
	 * @param horizontal
	 */
	public LFrame(LContainer parent, String title, boolean horizontal) {
		this(parent.getComposite(), horizontal, SWT.NONE);
		setText(title);
	}
	
	/** No layout.
	 * @param parent
	 */
	public LFrame(LContainer parent, String title) {
		this(parent.getComposite(), SWT.NONE);
		setText(title);
	}
	
	/** Grid layout.
	 * @param parent
	 */
	public LFrame(LContainer parent, String title, int columns) {
		this(parent, title, columns, false);
	}
	
	public void addWidget(LWidget widget, boolean hspace, boolean vspace, int cols, int rows) {
		GridData gd = new GridData(SWT.FILL, SWT.FILL, hspace, vspace, cols, rows);
		widget.setLayoutData(gd);
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
	public Object getChild(int i) {
		return getChildren()[i];
	}

	@Override
	protected void checkSubclass() { }
	
}
