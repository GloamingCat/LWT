package lwt.event;

import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;

public class LDeleteEvent<T> {

	public LPath parentPath;
	public int index;
	public LDataTree<T> node;
	
	public LDeleteEvent(LPath parentPath, int index, LDataTree<T> node) {
		this.parentPath = parentPath;
		this.index = index;
		this.node = node;
	}
	
}
