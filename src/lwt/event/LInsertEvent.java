package lwt.event;

import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;

public class LInsertEvent<T> {

	public LPath parentPath;
	public int index;
	public LDataTree<T> node;
	public int detail = 0;
	
	public LInsertEvent(LPath parentPath, int index, LDataTree<T> node) {
		this.parentPath = parentPath;
		this.index = index;
		this.node = node;
	}
	
}
