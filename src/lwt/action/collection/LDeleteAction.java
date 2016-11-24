package lwt.action.collection;

import lwt.action.LAction;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LDeleteEvent;
import lwt.event.LInsertEvent;
import lwt.widget.LCollection;

public class LDeleteAction<T> implements LAction {

	private LCollection<T, ?> collection;
	private LPath parentPath;
	private int index;
	private LDataTree<T> node;
	
	public LDeleteAction(LCollection<T, ?> c, LPath parentPath, int index, LDataTree<T> node) {
		collection = c;
		this.parentPath = parentPath;
		this.index = index;
		this.node = node;
	}
	
	@Override
	public void undo() {
		LInsertEvent<T> e = collection.insert(parentPath, index, node);
		collection.notifyInsertListeners(e);
	}

	@Override
	public void redo() {
		LDeleteEvent<T> e = collection.delete(parentPath, index);
		collection.notifyDeleteListeners(e);
	}

}
