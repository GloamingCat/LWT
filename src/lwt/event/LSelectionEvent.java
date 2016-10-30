package lwt.event;

import lwt.dataestructure.LPath;

public class LSelectionEvent {

	public LPath path;
	public Object data;
	public int detail;
	
	public LSelectionEvent(LPath path, Object data) {
		this.path = path;
		this.data = data;
	}
	
}
