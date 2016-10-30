package lwt.event.listener;

import lwt.event.LDeleteEvent;
import lwt.event.LEditEvent;
import lwt.event.LInsertEvent;
import lwt.event.LMoveEvent;

public class LCollectionListener<T> {

	public void onInsert(LInsertEvent<T> event) {}
	public void onDelete(LDeleteEvent<T> event) {}
	public void onMove(LMoveEvent<T> event) {}
	public void onEdit(LEditEvent<T> event) {}
	
}
