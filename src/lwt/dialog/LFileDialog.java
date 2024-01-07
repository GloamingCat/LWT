package lwt.dialog;

import org.eclipse.swt.widgets.FileDialog;

import lwt.dataserialization.LFileManager;

public class LFileDialog {
	
	protected FileDialog dialog;

	public LFileDialog(LShell parent, String title, boolean open) {
		dialog = new FileDialog(parent);
		dialog.setText(title);
		dialog.setFilterExtensions(new String[] {"*.json"});
		dialog.setFilterPath(LFileManager.applicationPath());
	}
	
	public String open() {
		return dialog.open();
	}
	
}
