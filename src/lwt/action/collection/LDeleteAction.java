package lwt.action.collection;

import lwt.action.LAction;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LDeleteEvent;
import lwt.event.LInsertEvent;
import lwt.widget.LMenuCollection;

public class LDeleteAction<T> implements LAction {

	private LMenuCollection<T, ?> collection;
	private LPath parentPath;
	private int index;
	private LDataTree<T> node;
	
	public LDeleteAction(LMenuCollection<T, ?> c, LPath parentPath, int index, LDataTree<T> node) {
		collection = c;
		this.parentPath = parentPath;
		this.index = index;
		this.node = node;
	}
	
	@Override
	public void undo() {
		LInsertEvent<T> e = collection.insertTreeItem(parentPath, index, node);
		collection.notifyInsertListeners(e);
	}

	@Override
	public void redo() {
		LDeleteEvent<T> e = collection.deleteTreeItem(parentPath, index);
		collection.notifyDeleteListeners(e);
	}

}
