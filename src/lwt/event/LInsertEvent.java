package lwt.event;

import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;

public class LInsertEvent {

	public LPath parentPath;
	public int index;
	public LDataTree<String> stringNode;
	public int detail = 0;
	
	public LInsertEvent(LPath parentPath, int index, LDataTree<String> stringNode) {
		this.parentPath = parentPath;
		this.index = index;
		this.stringNode = stringNode;
	}
	
}
