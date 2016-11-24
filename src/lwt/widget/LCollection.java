package lwt.widget;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;

import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LDeleteEvent;
import lwt.event.LEditEvent;
import lwt.event.LInsertEvent;
import lwt.event.LMoveEvent;
import lwt.event.listener.LCollectionListener;

public abstract class LCollection<T, ST> extends LWidget {
	
	public LCollection(Composite parent, int style) {
		super(parent, style);
	}

	//-------------------------------------------------------------------------------------
	// Modify
	//-------------------------------------------------------------------------------------
	
	public abstract LMoveEvent<T> move(LPath sourceParent, int sourceIndex, LPath destParent, int destIndex);
	public abstract LInsertEvent<T> insert(LPath parentPath, int index, LDataTree<T> node);
	public abstract LDeleteEvent<T> delete(LPath parentPath, int index);
	public abstract LEditEvent<ST> edit(LPath path);
	
	//-------------------------------------------------------------------------------------
	// Listeners
	//-------------------------------------------------------------------------------------

	protected ArrayList<LCollectionListener<T>> moveListeners = new ArrayList<>();
	public void addMoveListener(LCollectionListener<T> listener) {
		moveListeners.add(listener);
	}
	
	protected ArrayList<LCollectionListener<ST>> editListeners = new ArrayList<>();
	public void addEditListener(LCollectionListener<ST> listener) {
		editListeners.add(listener);
	}
	
	protected ArrayList<LCollectionListener<T>> insertListeners = new ArrayList<>();
	public void addInsertListener(LCollectionListener<T> listener) {
		insertListeners.add(listener);
	}
		
	protected ArrayList<LCollectionListener<T>> deleteListeners = new ArrayList<>();
	public void addDeleteListener(LCollectionListener<T> listener) {
		deleteListeners.add(listener);
	}
	
	//-------------------------------------------------------------------------------------
	// Listener Notify
	//-------------------------------------------------------------------------------------

	public void notifyMoveListeners(LMoveEvent<T> event) {
		for(LCollectionListener<T> listener : moveListeners) {
			listener.onMove(event);
		}
	}
	
	public void notifyEditListeners(LEditEvent<ST> event) {
		for(LCollectionListener<ST> listener : editListeners) {
			listener.onEdit(event);
		}
	}
	
	public void notifyInsertListeners(LInsertEvent<T> event) {
		for(LCollectionListener<T> listener : insertListeners) {
			listener.onInsert(event);
		}
	}
	
	public void notifyDeleteListeners(LDeleteEvent<T> event) {
		for(LCollectionListener<T> listener : deleteListeners) {
			listener.onDelete(event);
		}
	}
	
	//-------------------------------------------------------------------------------------
	// Object
	//-------------------------------------------------------------------------------------
	
	public abstract T toObject(LPath path);
	
	public abstract void refreshObject(LPath path);
	
	public abstract void refreshAll();
	
}
