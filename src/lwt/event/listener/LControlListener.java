package lwt.event.listener;

import lwt.event.LControlEvent;

public interface LControlListener<T> {

	void onModify(LControlEvent<T> event);
	
}
