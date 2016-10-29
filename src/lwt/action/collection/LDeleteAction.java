package lwt.action.collection;

import lwt.action.LAction;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LDeleteEvent;
import lwt.event.LInsertEvent;
import lwt.widget.LMenuCollection;

public class LDeleteAction implements LAction {

	private LMenuCollection collection;
	private LPath parentPath;
	private int index;
	private LDataTree<String> stringNode;
	
	public LDeleteAction(LMenuCollection c, LPath parentPath, int index, LDataTree<String> stringNode) {
		collection = c;
		this.parentPath = parentPath;
		this.index = index;
		this.stringNode = stringNode;
	}
	
	@Override
	public void undo() {
		LInsertEvent e = collection.insertTreeItem(parentPath, index, stringNode);
		collection.notifyInsertListeners(e);
	}

	@Override
	public void redo() {
		LDeleteEvent e = collection.deleteTreeItem(parentPath, index);
		collection.notifyDeleteListeners(e);
	}

}
