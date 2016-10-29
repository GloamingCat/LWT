package lwt.event;

import lwt.dataestructure.LPath;

public class LEditEvent {

	public LPath path;
	public Object oldData;
	public Object newData;
	
	public LEditEvent(LPath path, Object newData, Object oldData) {
		this.path = path;
		this.newData = newData;
		this.oldData = oldData;
	}
	
}
