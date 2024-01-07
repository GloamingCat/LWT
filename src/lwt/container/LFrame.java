package lwt.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Layout;

import lwt.LFlags;
import lwt.dialog.LShell;
import lwt.graphics.LPoint;

public class LFrame extends Group implements LContainer {

	private boolean equalRows = false;
	
	//////////////////////////////////////////////////
	// {{ Constructors
	
	/**
	 * Internal, no layout.
	 */
	LFrame(Composite parent, int style) {
		super(parent, style);
	}
	
	/** No layout.
	 * @param parent
	 */
	public LFrame(LContainer parent, String title) {
		this(parent.getComposite(), SWT.NONE);
		setText(title);
	}

	//////////////////////////////////////////////////
	// {{ Inner Layout
	
	/**
	 * Fill layout (spacing = 0).
	 */
	public void setFillLayout(boolean horizontal) {
		FillLayout fl = new FillLayout(horizontal ?
				SWT.HORIZONTAL : SWT.VERTICAL);
		fl.marginWidth = 5;
		fl.marginHeight = 5;
		setLayout(fl);
	}
	
	/**
	 * Grid layout (spacing = 5).
	 */
	public void setGridLayout(int columns) {
		GridLayout gl = new GridLayout(columns, false);
		gl.marginWidth = 5;
		gl.marginHeight = 5;
		setLayout(gl);
	}

	/*
	 * Column/row layout (spacing = 5).
	 */
	public void setSequentialLayout(boolean horizontal) {
		RowLayout layout = new RowLayout(horizontal ? SWT.HORIZONTAL : SWT.VERTICAL);
		layout.spacing = 5;
		layout.marginWidth = 5;
		layout.marginHeight = 5;
		setLayout(layout);
	}

	public void setMargins(int h, int v) {
		Layout l = getLayout();
		if (l instanceof GridLayout) {
			GridLayout gl = (GridLayout) l;
			gl.marginWidth = h;
			gl.marginHeight = v;
		} else if (l instanceof RowLayout) {
			RowLayout rl = (RowLayout) l;
			rl.marginWidth = h;
			rl.marginHeight = v;
		} else {
			FillLayout fl = (FillLayout) l;
			fl.marginWidth = h;
			fl.marginHeight = v;
		}
	}
	
	public void setSpacing(int h, int v) {
		Layout l = getLayout();
		if (l instanceof GridLayout) {
			GridLayout gl = (GridLayout) l;
			gl.horizontalSpacing = h;
			gl.verticalSpacing = v;
		} else if (l instanceof RowLayout) {
			RowLayout rl = (RowLayout) l;
			rl.spacing = h;
		} else {
			FillLayout fl = (FillLayout) l;
			fl.spacing = h;
		}
	}
	
	public void setSpacing(int s) {
		setSpacing(s, s);
	}

	public void setEqualCells(boolean horizontal, boolean vertical) {
		equalRows = vertical;
		Layout l = getLayout();
		if (l instanceof GridLayout) {
			GridLayout gl = (GridLayout) l;
			gl.makeColumnsEqualWidth = horizontal;
		} else if (l instanceof RowLayout) {
			RowLayout rl = (RowLayout) l;
			rl.pack = horizontal;
		}
	}
	
	public void setEqualCells(boolean value) {
		setEqualCells(value, value);
	}
	
	@Override
	public void layout() {
		if (equalRows && getLayout() instanceof GridLayout) {
			int height = computeSize(SWT.DEFAULT, SWT.DEFAULT).y
					/ getChildCount();
			for (Control child : getChildren()) {
				GridData gd = (GridData) child.getLayoutData();
				if (gd == null) {
					gd = new GridData();
					child.setLayoutData(gd);
				}
				gd.heightHint = height;
			}
		}
		super.layout();
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
		GridData gridData = initGridData();
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
		GridData gridData = initGridData();
		gridData.grabExcessHorizontalSpace = h;
		gridData.grabExcessVerticalSpace = v;
		if (h) {
			gridData.horizontalAlignment = SWT.FILL;
			gridData.widthHint = 1;
		}
		if (v) {
			gridData.verticalAlignment = SWT.FILL;
			gridData.heightHint = 1;
		}
	}
	
	public void setMinimumWidth(int w) {
		GridData gridData = initGridData();
		gridData.minimumWidth = w;
		if (w == 0)
			w = -1;
		gridData.widthHint = w;
	}
	
	public void setMinimumHeight(int h) {
		GridData gridData = initGridData();
		gridData.minimumHeight = h;
		if (h == 0)
			h = -1;
		gridData.heightHint = h;
	}
	
	// }}

	//////////////////////////////////////////////////
	// {{ Size
	
	public LPoint getCurrentSize() {
		return new LPoint(getSize());
	}
	
	public void setCurrentSize(LPoint size) {
		setSize(size.x, size.y);
	}
	
	public void setCurrentSize(int x, int y) {
		setSize(x, y);
	}
	
	// }}
	
	public void setTitle(String text) {
		setText(text);
	}
	
	public void setHoverText(String text) {
		setToolTipText(text);
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
	public int getChildCount() {
		return this.getChildren().length;
	}

	@Override
	public Object getData() {
		return super.getData();
	}
	
	@Override
	public Object getData(String key) {
		return super.getData(key);
	}
	
	@Override
	public LShell getShell() {
		return (LShell) super.getShell();
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
	
	@Override
	protected void checkSubclass() { }
	
}
