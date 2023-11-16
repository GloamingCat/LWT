package lwt.widget;

import lwt.LVocab;
import lwt.action.LAction;
import lwt.action.LActionStack;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public abstract class LWidget extends Composite {

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

}
