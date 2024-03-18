package lwt.dialog;

import org.eclipse.swt.widgets.FileDialog;

import lbase.serialization.LFileManager;

import org.eclipse.swt.widgets.DirectoryDialog;

public class LFileDialog {
	
	private FileDialog fileDialog = null;
	private DirectoryDialog folderDialog = null;

	public LFileDialog(LWindow parent, String title, String extension, boolean create) {
		if (extension != null) {
			fileDialog = new FileDialog(parent);
			fileDialog.setText(title);
			if (create)
				fileDialog.setFileName("New Project." + extension);
			fileDialog.setFilterExtensions(new String[] {"*." + extension});
			fileDialog.setFilterNames(new String[] {"Project file (*." + extension + ")"});
			fileDialog.setFilterPath(LFileManager.applicationPath());
		} else {
			folderDialog = new DirectoryDialog(parent);
			folderDialog.setText(title);
			folderDialog.setFilterPath(LFileManager.applicationPath());
		}
	}
	
	public String open() {
		return fileDialog == null ? folderDialog.open() :
			fileDialog.open();
	}
	
}
