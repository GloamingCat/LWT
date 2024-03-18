package lwt.widget;

import java.util.ArrayList;

import lbase.LVocab;
import lbase.action.collection.LDeleteAction;
import lbase.action.collection.LEditAction;
import lbase.action.collection.LInsertAction;
import lbase.data.LDataCollection;
import lbase.data.LDataTree;
import lbase.data.LPath;
import lbase.event.LDeleteEvent;
import lbase.event.LEditEvent;
import lbase.event.LInsertEvent;
import lbase.event.LMoveEvent;
import lbase.event.listener.LCollectionListener;
import lbase.gui.LMenu;
import lwt.container.LContainer;

public abstract class LCollection<T, ST> extends LWidget implements lbase.gui.LCollection<T, ST> {
	
	public LCollection(LContainer parent) {
		super(parent);
	}
	
	public LCollection(LContainer parent, int flags) {
		super(parent, flags);
	}
	
	public abstract LDataCollection<T> getDataCollection();
	public abstract void setDataCollection(LDataCollection<T> collection);
	
	//-------------------------------------------------------------------------------------
	// Actions
	//-------------------------------------------------------------------------------------
	
	protected LEditEvent<ST> newEditAction(LPath path) {
		LEditEvent<ST> event = edit(path);
		if (event != null) {
			if (menuInterface != null) {
				LEditAction<ST> action = new LEditAction<ST>(this, path, event.oldData, event.newData);
				menuInterface.actionStack.newAction(action);
			}
			notifyEditListeners(event);
		}
		return event;
	}
	
	protected LInsertEvent<T> newInsertAction(LPath parentPath, int i, LDataTree<T> node) {
		LInsertEvent<T> event = insert(parentPath, i, node);
		if (event != null) {
			if (menuInterface != null) {
				LInsertAction<T> action = new LInsertAction<T>(this, parentPath, event.index, node);
				menuInterface.actionStack.newAction(action);
			}
			notifyInsertListeners(event);
		}
		return event;
	}
	
	protected LDeleteEvent<T> newDeleteAction(LPath parentPath, int i) {
		LDeleteEvent<T> event = delete(parentPath, i);
		if (event != null) {
			if (menuInterface != null) {
				LDeleteAction<T> action = new LDeleteAction<T>(this, parentPath, event.index, event.node);
				menuInterface.actionStack.newAction(action);
			}
			notifyDeleteListeners(event);
		}
		return event;
	}

	//-------------------------------------------------------------------------------------
	// Menu buttons
	//-------------------------------------------------------------------------------------
	
	protected void setEditEnabled(LMenu menu, boolean value) {
		menu.setMenuButton(value, LVocab.instance.EDIT, "edit", (d) -> onEditButton(menu), "Space");
	}
	
	protected void setInsertNewEnabled(LMenu menu, boolean value) {
		menu.setMenuButton(value, LVocab.instance.INSERTNEW, "new", (d) -> onInsertNewButton(menu), "Ctrl+&N");
	}
	
	protected void setDuplicateEnabled(LMenu menu, boolean value) {
		menu.setMenuButton(value, LVocab.instance.DUPLICATE, "duplicate", (d) -> onDuplicateButton(menu), "Ctrl+&D");
	}
	
	protected void setDeleteEnabled(LMenu menu, boolean value) {
		menu.setMenuButton(value, LVocab.instance.DELETE, "delete", (d) -> onDeleteButton(menu), "Del");	}
	
	//-------------------------------------------------------------------------------------
	// Menu handlers
	//-------------------------------------------------------------------------------------
	
	protected abstract void onEditButton(LMenu menu);
	protected abstract void onInsertNewButton(LMenu menu);
	protected abstract void onDuplicateButton(LMenu menu);
	protected abstract void onDeleteButton(LMenu menu);
	
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
