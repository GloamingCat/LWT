package lwt.event;

import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;

public class LMoveEvent {

	public LPath sourceParent;
	public LPath destParent;
	public int sourceIndex;
	public int destIndex;
	public LDataTree<String> sourceNode;
	
	public LMoveEvent(LPath sourceParent, LPath destParent, int sourceIndex, int destIndex, LDataTree<String> sourceNode) {
		this.sourceParent = sourceParent;
		this.destParent = destParent;
		this.sourceIndex = sourceIndex;
		this.destIndex = destIndex;
		this.sourceNode = sourceNode;
	}
	
}
