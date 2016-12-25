package lwt.event.listener;

import lwt.event.LHashEditEvent;
import lwt.event.LHashKeyEvent;

public class LHashListener<T> {

	public void onInsert(LHashKeyEvent<T> event) {}
	public void onDelete(LHashKeyEvent<T> event) {}
	public void onEdit(LHashEditEvent<T> event) {}
	
}
