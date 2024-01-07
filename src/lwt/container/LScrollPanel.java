package lwt.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import lwt.LFlags;
import lwt.graphics.LPoint;

public class LScrollPanel extends ScrolledComposite implements LContainer {
	
	private Control content;

	//////////////////////////////////////////////////
	// {{ Constructors

	/**
	 * Internal, no layout.
	 */
	LScrollPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
		addControlListener(new ControlListener() {
			@Override
			public void controlResized(ControlEvent e) {
				if (content == null) {
					content = getChildren()[0];
					setContent(content);
				}
				layout();
			}
			@Override
			public void controlMoved(ControlEvent e) {
				
			}
		});
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
	
	// }}

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
	
	public void refreshSize(LPoint size) {
		setMinSize(size.x, size.y);
	}
	
	public void refreshSize(int width, int height) {
		setMinSize(width, height);
		layout();
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
	protected void checkSubclass() { }
	
}
