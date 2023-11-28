package lwt.widget;

import lwt.LVocab;
import lwt.action.LAction;
import lwt.action.LActionStack;
import lwt.container.LContainer;
import lwt.dialog.LShell;
import lwt.LFlags;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public abstract class LWidget extends Composite implements LContainer {

	public static Object clipboard = null;
	protected LActionStack actionStack;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LWidget(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
	}
	
	public LWidget(LContainer parent) {
		super(parent.getComposite(), SWT.NONE);
		setLayout(new FillLayout());
	}

	public void setActionStack(LActionStack stack) {
		this.actionStack = stack;
	}

	public void newAction(LAction action) {
		if (actionStack != null) {
			actionStack.newAction(action);
		}
	}

	protected void setMenuButton(Menu menu, boolean value, String buttonName, String buttonKey, 
			SelectionAdapter adapter) {
		setMenuButton(menu, value, buttonName, buttonKey, adapter, (char) 0);
	}

	protected void setMenuButton(Menu menu, boolean value, String buttonName, String buttonKey, 
			SelectionAdapter adapter, char acc) {
		if (value) {
			if (menu.getData(buttonKey) == null) {
				MenuItem item = new MenuItem(menu, SWT.NONE);
				item.addSelectionListener(adapter);
				menu.setData(buttonKey, item);
				if (acc != 0) {
					item.setAccelerator(SWT.MOD1 | acc);
					buttonName += "\tCtrl + &" + acc;
				}
				item.setText(buttonName);
			}
		} else {
			if (menu.getData(buttonKey) != null) {
				MenuItem item = (MenuItem) menu.getData(buttonKey);
				item.dispose();
			}
		}
	}

	public void setCopyEnabled(Menu menu, boolean value) {
		setMenuButton(menu, value, LVocab.instance.COPY, "copy", new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				onCopyButton(menu);
			}
		}, 'C');

	}

	public void setPasteEnabled(Menu menu, boolean value) {
		setMenuButton(menu, value, LVocab.instance.PASTE, "paste", new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				onPasteButton(menu);
			}
		}, 'V');
	}

	protected abstract void onCopyButton(Menu menu);
	protected abstract void onPasteButton(Menu menu);
	
	
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
	
	public Composite getComposite() {
		return this;
	}
	
	public LShell getShell() {
		return (LShell) super.getShell();
	}
	
}
