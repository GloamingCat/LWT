package lwt.event;

import lwt.dataestructure.LPath;

public class LSelectionEvent {

	public LPath path;
	public Object data;
	public int id;
	public boolean check;
	
	public LSelectionEvent(LPath path, Object data, int id) {
		this.path = path;
		this.data = data;
		this.id = id;
	}
	
}
