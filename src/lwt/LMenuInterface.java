package lwt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import lwt.action.LActionStack;
import lwt.container.LView;
import lwt.editor.LEditor;
import lwt.widget.LWidget;

public class LMenuInterface {

	private LEditor focusEditor;
	private LWidget focusWidget;
	private LApplicationShell shell;
	public final LActionStack actionStack;
	
	public LMenuInterface(LView root) {
		actionStack = new LActionStack(root);
		if (root.getShell() instanceof LApplicationShell)
			shell = (LApplicationShell) root.getShell();
	}
	
	public void copy() {
		if (focusWidget != null)
			focusWidget.onCopyButton(shell == null ? null : shell.menuEdit);
		else if (focusEditor != null)
			focusEditor.onCopyButton(shell == null ? null : shell.menuEdit);
	}
	
	public void paste() {
		if (focusWidget != null)
			focusWidget.onPasteButton(shell == null ? null : shell.menuEdit);
		else if (focusEditor != null)
			focusEditor.onPasteButton(shell == null ? null : shell.menuEdit);
	}
	
	public boolean canCopy() {
		return focusWidget != null || focusEditor != null;
	}
	
	public boolean canPaste() {
		Object obj = LGlobals.clipboard.getContents(TextTransfer.getInstance());
		if (obj == null)
			return false;
		if (focusWidget != null) {
			if (!focusWidget.canDecode((String) obj))
				return false;
		}
		if (focusEditor == null)
			return false;
		return focusEditor.canDecode((String) obj);
	}
	
	public void setFocusEditor(LEditor editor) {
		focusEditor = editor;
		focusWidget = null;
		if (shell != null) {
			shell.refreshClipboardButtons();
		}
	}
	
	public void setFocusWidget(LWidget widget) {
		focusWidget = widget;
		if (shell != null) {
			shell.refreshClipboardButtons();
		}
	}
	
	//////////////////////////////////////////////////
	// {{ Menu Buttons
	
	public static void setMenuButton(Menu menu, boolean value, String buttonName, String buttonKey, 
			SelectionAdapter adapter) {
		setMenuButton(menu, value, buttonName, buttonKey, adapter, (char) 0);
	}

	public static void setMenuButton(Menu menu, boolean value, String buttonName, String buttonKey, 
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
	
	// }}
	
}
