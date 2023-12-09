package lwt.event.listener;

import lwt.event.LHashEditEvent;
import lwt.event.LHashKeyEvent;

public interface LHashListener<T> {

	void onInsert(LHashKeyEvent<T> event);
	void onDelete(LHashKeyEvent<T> event);
	void onEdit(LHashEditEvent<T> event);
	
}
