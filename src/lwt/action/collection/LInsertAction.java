package lwt.action.collection;

import lwt.action.LAction;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LDeleteEvent;
import lwt.event.LInsertEvent;
import lwt.widget.LMenuCollection;

public class LInsertAction<T> implements LAction {

	private LMenuCollection<T, ?> collection;
	private LPath parent;
	private int index;
	private LDataTree<T> node;
	
	public LInsertAction(LMenuCollection<T, ?> c, LPath parent, int index, LDataTree<T> node) {
		collection = c;
		this.parent = parent;
		this.index = index;
		this.node = node;
	}
	
	@Override
	public void undo() {
		LDeleteEvent<T> e = collection.deleteTreeItem(parent, index);
		collection.notifyDeleteListeners(e);
	}

	@Override
	public void redo() {
		LInsertEvent<T> e = collection.insertTreeItem(parent, index, node);
		collection.notifyInsertListeners(e);
	}
	
}
