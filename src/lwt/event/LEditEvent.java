package lwt.event;

import lwt.dataestructure.LPath;

public class LEditEvent<T> {

	public LPath path;
	public T oldData;
	public T newData;
	
	public LEditEvent(LPath path, T newData, T oldData) {
		this.path = path;
		this.newData = newData;
		this.oldData = oldData;
	}
	
}
