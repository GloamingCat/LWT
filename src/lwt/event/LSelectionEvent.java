package lwt.event;

import org.eclipse.swt.widgets.TreeItem;

public class LSelectionEvent {

	public TreeItem item;
	public int detail;
	
	public LSelectionEvent(TreeItem item) {
		this.item = item;
	}
	
}
