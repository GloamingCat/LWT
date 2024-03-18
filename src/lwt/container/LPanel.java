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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

import lbase.LFlags;
import lbase.event.LMouseEvent;
import lbase.event.listener.LMouseListener;
import lwt.graphics.LPoint;

public class LPanel extends Composite implements LContainer {

	private ArrayList<LMouseListener> mouseListeners = null;
	private boolean equalRows = false;

	//////////////////////////////////////////////////
	// {{ Constructors
	
	/** 
	 * Internal, no layout.
	 */
	LPanel(Composite parent, int style) {
		super(parent, style);
	}

	/** 
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter parent new LShell(800, 600)
	 */
	public LPanel(LContainer parent) {
		this(parent.getContentComposite(), SWT.NONE);
	}

	// }}
	
	//////////////////////////////////////////////////
	// {{ Inner Layout
	
	/**
	 * Fill layout (spacing = 0).
	 */
	public void setFillLayout(boolean horizontal) {
		if (horizontal) {
			setLayout(new FillLayout(SWT.HORIZONTAL));
		} else {
			setLayout(new FillLayout(SWT.VERTICAL));
		}
	}
	
	/**
	 * Grid layout (spacing = 5).
	 */
	public void setGridLayout(int columns) {
		GridLayout gl = new GridLayout(columns, false);
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		setLayout(gl);
	}

	/*
	 * Column/row layout (spacing = 5).
	 */
	public void setSequentialLayout(boolean horizontal) {
		RowLayout layout = new RowLayout(horizontal ? SWT.HORIZONTAL : SWT.VERTICAL);
		layout.spacing = 5;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
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
		if (h)
			gridData.horizontalAlignment = SWT.FILL;
		if (v)
			gridData.verticalAlignment = SWT.FILL;
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
				LMouseEvent e = createMouseEvent(e0, true, false);
				for (LMouseListener l : mouseListeners)
					l.onMouseChange(e);
			}
			@Override
			public void mouseDown(MouseEvent e0) {
				LMouseEvent e = createMouseEvent(e0, false, false);
				for (LMouseListener l : mouseListeners)
					l.onMouseChange(e);
			}
			@Override
			public void mouseDoubleClick(MouseEvent e0) {
				LMouseEvent e = createMouseEvent(e0, true, true);
				for (LMouseListener l : mouseListeners)
					l.onMouseChange(e);
			}
		});
		addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e0) {
				LMouseEvent e = createMouseEvent(e0, false, false);
				for (LMouseListener l : mouseListeners)
					l.onMouseChange(e);
			}
		});
	}
	
	private LMouseEvent createMouseEvent(MouseEvent e, boolean release, boolean repeat) {
		int x = e.x;
		int y = e.y;
		int button = 0;
		if (e.button == 1)
			button = LFlags.LEFT;
		else if (e.button == 2)
			button = LFlags.RIGHT;
		else if (e.button == 3)
			button = LFlags.MIDDLE;
		int type;
		if (release) {
			if (repeat)
				type = LFlags.DOUBLEPRESS;
			else
				type = LFlags.RELEASE;
		} else {
			if (repeat)
				type = LFlags.REPEATPRESS;
			else
				type = LFlags.PRESS;
		}
		return new LMouseEvent(button, x, y, type);
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
	public Composite getContentComposite() {
		return this;
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
	protected void checkSubclass() { }
	
}
