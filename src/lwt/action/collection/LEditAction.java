package lwt.action.collection;

import lwt.action.LAction;
import lwt.dataestructure.LPath;
import lwt.event.LEditEvent;
import lwt.widget.LMenuCollection;

public class LEditAction implements LAction {

	private LMenuCollection collection;
	private LPath path;
	private Object oldData;
	private Object newData;
	
	public LEditAction(LMenuCollection collection, LPath path, Object oldData, Object newData) {
		this.collection = collection;
		this.path = path;
		this.oldData = oldData;
		this.newData = newData;
	}
	
	@Override
	public void undo() {
		LEditEvent e = new LEditEvent(path, newData, oldData);
		collection.notifyEditListeners(e);
	}

	@Override
	public void redo() {
		LEditEvent e = new LEditEvent(path, oldData, newData);
		collection.notifyEditListeners(e);
	}

}
