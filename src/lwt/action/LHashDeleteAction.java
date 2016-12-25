package lwt.action;

import lwt.event.LHashKeyEvent;
import lwt.widget.LHashTable;

public class LHashDeleteAction<T> implements LAction {

	private LHashKeyEvent<T> event;
	private LHashTable<T> widget;
	
	public LHashDeleteAction(LHashTable<T> widget, LHashKeyEvent<T> event) {
		this.widget = widget;
		this.event = event;
	}

	@Override
	public void undo() {
		widget.insertKey(event.key, event.value);
		widget.notifyInsertListeners(event);
	}

	@Override
	public void redo() {
		widget.deleteKey(event.key);
		widget.notifyDeleteListeners(event);
	}
	
}
