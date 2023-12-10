package lwt;

import org.eclipse.swt.dnd.TextTransfer;

import lwt.action.LActionStack;
import lwt.container.LView;
import lwt.editor.LEditor;
import lwt.widget.LWidget;

public class LMenuInterface {

	private LEditor focusEditor;
	private LWidget focusWidget;
	private LDefaultApplicationShell shell;
	public final LActionStack actionStack;
	
	public LMenuInterface(LView root) {
		actionStack = new LActionStack(root);
		if (root.getShell() instanceof LDefaultApplicationShell)
			shell = (LDefaultApplicationShell) root.getShell();
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

}
