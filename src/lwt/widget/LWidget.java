package lwt.widget;

import lwt.LVocab;
import lwt.action.LAction;
import lwt.container.LContainer;
import lwt.dialog.LShell;
import lwt.LFlags;
import lwt.LMenuInterface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

public abstract class LWidget extends Composite {

	protected LMenuInterface menuInterface;

	public LWidget(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
		parent.setTabList(null);
		createContent(style);
	}

	/**
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter parent new lwt.container.LPanel(new LShell(800, 600), true)
	 */
	public LWidget(LContainer parent) {
		this(parent.getComposite(), SWT.NONE);
	}
	
	protected abstract void createContent(int flags);

	public void setMenuInterface(LMenuInterface mi) {
		menuInterface = mi;
	}

	public void newAction(LAction action) {
		if (menuInterface != null) {
			menuInterface.actionStack.newAction(action);
		}
	}
	
	//////////////////////////////////////////////////
	// {{ Menu
	
	private Menu addMenu(Composite parent) {
		Menu menu = new Menu(parent);
		parent.setMenu(menu);
		setCopyEnabled(menu, true);
		setPasteEnabled(menu, true);
		addFocusOnClick(parent);
		return menu;
	}
	
	private void addFocusOnClick(Composite c) {
		LWidget widget = this;
		c.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				if (e.button == 1) { // Left button
					System.out.println(widget);
					menuInterface.setFocusWidget(widget);
				}
			}
		});
	}
	
	public void addMenu() {
		addMenu(this);
	}
	
	public void addMenu(LContainer frame) {
		Composite c = frame.getComposite();
		Menu menu = getMenu();
		if (menu == null) {
			menu = addMenu(c);
			setMenu(menu);
			addFocusOnClick(this);
		} else if (c.getMenu() == null) {
			c.setMenu(menu);
			addFocusOnClick(c);
		}
	}
	
	public void addMenu(LWidget widget) {
		Menu menu = getMenu();
		if (menu == null) {
			menu = addMenu((Composite) widget);
			setMenu(menu);
			addFocusOnClick(this);
		} else if (widget.getMenu() == null) {
			widget.setMenu(menu);
			addFocusOnClick(widget);
		}
	}

	public void setCopyEnabled(Menu menu, boolean value) {
		String str = toString();
		LMenuInterface.setMenuButton(menu, value, LVocab.instance.COPY, "copy", new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.out.println("Copied from " + str);
				onCopyButton(menu);
			}
		}, 'C');
	}

	public void setPasteEnabled(Menu menu, boolean value) {
		String str = toString();
		LMenuInterface.setMenuButton(menu, value, LVocab.instance.PASTE, "paste", new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.out.println("Pasted on " + str);
				onPasteButton(menu);
			}
		}, 'V');
	}

	public abstract void onCopyButton(Menu menu);
	public abstract void onPasteButton(Menu menu);
	public abstract boolean canDecode(String str);
	
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
	
	@Override
	protected void checkSubclass() { }

	@Override
	public LShell getShell() {
		return (LShell) super.getShell();
	}
	
	@Override
	public Object getData() {
		return super.getData();
	}
	
	@Override
	public Object getData(String key) {
		return super.getData(key);
	}
	
	
}
