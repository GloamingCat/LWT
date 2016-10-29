package lwt.action.collection;

import lwt.action.LAction;
import lwt.dataestructure.LPath;
import lwt.event.LMoveEvent;
import lwt.widget.LCollection;

public class LMoveAction implements LAction {

	private LCollection collection;
	private LPath sourceParent;
	private LPath destParent;
	private int sourceIndex;
	private int destIndex;
	
	public LMoveAction(LCollection collection, LPath sourceParent, int sourceIndex, LPath destParent, int destIndex) {
		this.collection = collection;
		this.sourceParent = sourceParent;
		this.destParent = destParent;
		this.sourceIndex = sourceIndex;
		this.destIndex = destIndex;
	}
	
	@Override
	public void undo() {
		LMoveEvent e = collection.move(destParent, destIndex, sourceParent, sourceIndex);
		collection.notifyMoveListeners(e);
	}

	@Override
	public void redo() {
		LMoveEvent e = collection.move(sourceParent, sourceIndex, destParent, destIndex);
		collection.notifyMoveListeners(e);
	}

}
