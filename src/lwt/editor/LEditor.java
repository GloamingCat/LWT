package lwt.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import lwt.LVocab;
import lwt.container.LContainer;
import lwt.container.LView;

public abstract class LEditor extends LView {

	//////////////////////////////////////////////////
	// {{ Constructors
	
	/**
	 * No layout.
	 * @param parent
	 * @param doubleBuffered
	 */
	public LEditor(LContainer parent, boolean doubleBuffered) {
		super(parent, doubleBuffered);
	}

	/**
	 * Fill/row layout.
	 * @param parent
	 * @param horizontal
	 * @param equalCells
	 * @param doubleBuffered
	 */
	public LEditor(LContainer parent, boolean horizontal, boolean equalCells, boolean doubleBuffered) {
		super(parent, horizontal, equalCells, doubleBuffered);
	}
	
	/**
	 * Fill layout with no margin.
	 * @param parent
	 * @param horizontal
	 * @param doubleBuffered
	 */
	public LEditor(LContainer parent, boolean horizontal, boolean doubleBuffered) {
		super(parent, horizontal, doubleBuffered);
	}
	
	/**
	 * Grid layout.
	 * @param parent
	 * @param columns
	 * @param equalCols
	 * @param doubleBuffered
	 */
	public LEditor(LContainer parent, int columns, boolean equalCols, boolean doubleBuffered) {
		super(parent, columns, equalCols, doubleBuffered);
	}

	// }}
	
	//////////////////////////////////////////////////
	// {{ Object
	
	public abstract void setObject(Object object);
	public abstract void saveObjectValues();

	// }}
	
	//////////////////////////////////////////////////
	// {{ Menu
	
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
	
	public abstract void onCopyButton(Menu menu);
	public abstract void onPasteButton(Menu menu);
	public abstract boolean canDecode(String str);
	
	// }}
	
}
