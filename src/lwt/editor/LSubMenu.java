package lwt.editor;

import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import lbase.event.LSelectionEvent;
import lbase.event.listener.LSelectionListener;
import lwt.LGlobals;

public class LSubMenu extends Menu implements lbase.gui.LMenu {
	
	LSubMenu(MenuItem item) {
		super(item);
	}
	
	LSubMenu(Shell c, int type) {
		super(c, type);
	}
	
	public LSubMenu(Control c) {
		super(c);
		c.setMenu(this);
	}
	
	public LSubMenu addSubMenu(String name, String key) {
		MenuItem item = new MenuItem(this, SWT.CASCADE);
		item.setText(name);
		LSubMenu menu = new LSubMenu(item);
		item.setMenu(menu);
		setData(key, menu);
		return menu;
	}
	
	public void setMenuButton(boolean value, String buttonName, String buttonKey, 
			Consumer<Object> action, String acc) {
		if (value) {
			if (getData(buttonKey) == null) {
				MenuItem item = new MenuItem(this, SWT.NONE);
				item.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent arg0) {
						action.accept(arg0.data);
					}
				});
				setData(buttonKey, item);
				if (acc != null) {
					String[] keys = acc.split("\\+");
					item.setAccelerator(LGlobals.getAccelerator(keys));
					buttonName += "\t" + acc;
				}
				item.setText(buttonName);
			}
		} else {
			if (getData(buttonKey) != null) {
				MenuItem item = (MenuItem) getData(buttonKey);
				item.dispose();
			}
		}
	}
	
	public void addSeparator() {
		new MenuItem(this, SWT.SEPARATOR);
	}
	
	public void setButtonEnabled(String key, boolean value) {
		if (getData(key) != null) {
			MenuItem item = (MenuItem) getData(key);
			item.setEnabled(value);
		}
	}
	
	public void setMenuEnabled(String key, boolean value) {
		if (getData(key) != null) {
			Menu item = (Menu) getData(key);
			item.getParentItem().setEnabled(value);
		}
	}
	
	public void addSelectionListener(LSelectionListener listener) {
		addMenuListener(new MenuAdapter() {
			@Override
			public void menuShown(MenuEvent arg0) {
				listener.onSelect(new LSelectionEvent(null, null, -1));
			}
		});
	}
	
	@Override
	public void checkSubclass() {}
		
}
