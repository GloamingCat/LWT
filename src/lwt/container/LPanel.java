package lwt.container;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import lwt.LFlags;
import lwt.dialog.LShell;
import lwt.event.LMouseEvent;
import lwt.event.listener.LMouseListener;
import lwt.graphics.LPoint;

public class LPanel extends Composite implements LContainer {

	private ArrayList<LMouseListener> mouseListeners = null;
	
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

	//////////////////////////////////////////////////
	// {{ Listeners
	
	public void addMouseListener(LMouseListener l) {
		if (mouseListeners == null) {
			mouseListeners = new ArrayList<>();
			setMouseListeners();
		}
		mouseListeners.add(l);
	}
	
	public void removeMouseListener(LMouseListener l) {
		mouseListeners.remove(l);
	}
	
	private void setMouseListeners() {
		addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e0) {
				LMouseEvent e = new LMouseEvent(e0, true, false);
				for (LMouseListener l : mouseListeners)
					l.onMouseChange(e);
			}
			@Override
			public void mouseDown(MouseEvent e0) {
				LMouseEvent e = new LMouseEvent(e0, false, false);
				for (LMouseListener l : mouseListeners)
					l.onMouseChange(e);
			}
			@Override
			public void mouseDoubleClick(MouseEvent e0) {
				LMouseEvent e = new LMouseEvent(e0, true, true);
				for (LMouseListener l : mouseListeners)
					l.onMouseChange(e);
			}
		});
		addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e0) {
				LMouseEvent e = new LMouseEvent(e0, false, false);
				for (LMouseListener l : mouseListeners)
					l.onMouseChange(e);
			}
		});
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
	
	@Override
	public Composite getComposite() {
		return this;
	}
	
	@Override
	public Object getChild(int i) {
		return getChildren()[i];
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
