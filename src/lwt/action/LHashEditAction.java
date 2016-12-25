package lwt.action;

import lwt.event.LHashEditEvent;
import lwt.widget.LHashTable;

public class LHashEditAction<T> implements LAction {

	private LHashEditEvent<T> event;
	private LHashTable<T> widget;
	
	public LHashEditAction(LHashTable<T> widget, LHashEditEvent<T> event) {
		this.widget = widget;
		this.event = event;
	}
	
	private void apply() {
		widget.setValue(event.key, event.newValue);
		widget.notifyEditListeners(event);
	}
	
	private void swap() {
		T oldv = event.oldValue;
		T newv = event.newValue;
		event.oldValue = newv;
		event.newValue = oldv;
	}
	
	@Override
	public void undo() {
		swap();
		apply();
	}

	@Override
	public void redo() {
		swap();
		apply();
	}
	
}
