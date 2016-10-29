package lwt.event;

import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;

public class LDeleteEvent {

	public LPath parentPath;
	public int index;
	public LDataTree<String> stringNode;
	
	public LDeleteEvent(LPath parentPath, int index, LDataTree<String> stringNode) {
		this.parentPath = parentPath;
		this.index = index;
		this.stringNode = stringNode;
	}
}
