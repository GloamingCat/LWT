package lwt.action.collection;

import lwt.action.LAction;
import lwt.dataestructure.LPath;
import lwt.event.LEditEvent;
import lwt.widget.LMenuCollection;

public class LEditAction<T> implements LAction {

	private LMenuCollection<?, T> collection;
	private LPath path;
	private T oldData;
	private T newData;
	
	public LEditAction(LMenuCollection<?, T> collection, LPath path, T oldData, T newData) {
		this.collection = collection;
		this.path = path;
		this.oldData = oldData;
		this.newData = newData;
	}
	
	@Override
	public void undo() {
		LEditEvent<T> e = new LEditEvent<T>(path, newData, oldData);
		collection.notifyEditListeners(e);
		collection.renameItem(path);
		collection.notifySelectionListeners(collection.select(e.path));
	}

	@Override
	public void redo() {
		LEditEvent<T> e = new LEditEvent<T>(path, oldData, newData);
		collection.notifyEditListeners(e);
		collection.renameItem(path);
		collection.notifySelectionListeners(collection.select(e.path));
	}

}
