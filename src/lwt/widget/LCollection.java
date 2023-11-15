package lwt.widget;

import java.util.ArrayList;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

import lwt.LVocab;
import lwt.action.collection.LDeleteAction;
import lwt.action.collection.LEditAction;
import lwt.action.collection.LInsertAction;
import lwt.dataestructure.LDataCollection;
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
	
	public abstract LDataCollection<T> getDataCollection();
	public abstract void setDataCollection(LDataCollection<T> collection);
	
	//-------------------------------------------------------------------------------------
	// Actions
	//-------------------------------------------------------------------------------------
	
	protected LEditEvent<ST> newEditAction(LPath path) {
		LEditEvent<ST> event = edit(path);
		if (event != null) {
			if (actionStack != null) {
				LEditAction<ST> action = new LEditAction<ST>(this, path, event.oldData, event.newData);
				actionStack.newAction(action);
			}
			notifyEditListeners(event);
		}
		return event;
	}
	
	protected LInsertEvent<T> newInsertAction(LPath parentPath, int i, LDataTree<T> node) {
		LInsertEvent<T> event = insert(parentPath, i, node);
		if (event != null) {
			if (actionStack != null) {
				LInsertAction<T> action = new LInsertAction<T>(this, parentPath, event.index, node);
				actionStack.newAction(action);
			}
			notifyInsertListeners(event);
		}
		return event;
	}
	
	protected LDeleteEvent<T> newDeleteAction(LPath parentPath, int i) {
		LDeleteEvent<T> event = delete(parentPath, i);
		if (event != null) {
			if (actionStack != null) {
				LDeleteAction<T> action = new LDeleteAction<T>(this, parentPath, event.index, event.node);
				actionStack.newAction(action);
			}
			notifyDeleteListeners(event);
		}
		return event;
	}

	//-------------------------------------------------------------------------------------
	// Menu buttons
	//-------------------------------------------------------------------------------------
	
	protected void setEditEnabled(Menu menu, boolean value) {
		setMenuButton(menu, value, LVocab.instance.EDIT, "edit", new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent arg0) {
	    		onEditButton(menu);
	    	}
		});
	}
	
	protected void setInsertNewEnabled(Menu menu, boolean value) {
		setMenuButton(menu, value, LVocab.instance.INSERTNEW, "new", new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent arg0) {
	    		onInsertNewButton(menu);
	    	}
		}, 'N');
	}
	
	protected void setDuplicateEnabled(Menu menu, boolean value) {
		setMenuButton(menu, value, LVocab.instance.DUPLICATE, "duplicate", new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent arg0) {
	    		onDuplicateButton(menu);
	    	}
		}, 'D');
	}
	
	protected void setDeleteEnabled(Menu menu, boolean value) {
		setMenuButton(menu, value, LVocab.instance.DELETE, "delete", new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent arg0) {
	    		onDeleteButton(menu);
	    	}
		});
	}
	
	//-------------------------------------------------------------------------------------
	// Menu handlers
	//-------------------------------------------------------------------------------------
	
	protected abstract void onEditButton(Menu menu);
	protected abstract void onInsertNewButton(Menu menu);
	protected abstract void onDuplicateButton(Menu menu);
	protected abstract void onDeleteButton(Menu menu);
	
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
	
	public abstract LPath getSelectedPath();
	
}
