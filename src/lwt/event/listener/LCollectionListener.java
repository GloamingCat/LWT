package lwt.event.listener;

import lwt.event.LDeleteEvent;
import lwt.event.LEditEvent;
import lwt.event.LInsertEvent;
import lwt.event.LMoveEvent;

public class LCollectionListener {

	public void onInsert(LInsertEvent event) {}
	public void onDelete(LDeleteEvent event) {}
	public void onMove(LMoveEvent event) {}
	public void onEdit(LEditEvent event) {}
	
}
