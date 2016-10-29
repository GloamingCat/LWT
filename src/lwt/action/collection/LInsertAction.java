package lwt.action.collection;

import lwt.action.LAction;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LDeleteEvent;
import lwt.event.LInsertEvent;
import lwt.widget.LMenuCollection;

public class LInsertAction implements LAction {

	private LMenuCollection collection;
	private LPath parent;
	private int index;
	private LDataTree<String> stringNode;
	
	public LInsertAction(LMenuCollection c, LPath parent, int index, LDataTree<String> stringNode) {
		collection = c;
		this.parent = parent;
		this.index = index;
		this.stringNode = stringNode;
	}
	
	@Override
	public void undo() {
		LDeleteEvent e = collection.deleteTreeItem(parent, index);
		collection.notifyDeleteListeners(e);
	}

	@Override
	public void redo() {
		LInsertEvent e = collection.insertTreeItem(parent, index, stringNode);
		e.detail = 2;
		collection.notifyInsertListeners(e);
	}
	
}
