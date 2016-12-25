package lwt.action;

import lwt.event.LHashKeyEvent;
import lwt.widget.LHashTable;

public class LHashInsertAction<T> implements LAction {

	private LHashKeyEvent<T> event;
	private LHashTable<T> widget;
	
	public LHashInsertAction(LHashTable<T> widget, LHashKeyEvent<T> event) {
		this.widget = widget;
		this.event = event;
	}

	@Override
	public void undo() {
		widget.deleteKey(event.key);
		widget.notifyDeleteListeners(event);
	}

	@Override
	public void redo() {
		widget.insertKey(event.key, event.value);
		widget.notifyInsertListeners(event);
	}
	
}
