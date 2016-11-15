package lwt.widget;

import java.util.ArrayList;

import lwt.action.collection.LDeleteAction;
import lwt.action.collection.LEditAction;
import lwt.action.collection.LInsertAction;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LDeleteEvent;
import lwt.event.LEditEvent;
import lwt.event.LInsertEvent;
import lwt.event.listener.LCollectionListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TreeItem;

public abstract class LMenuCollection<T, ST> extends LCollection<T> {

	protected Menu menu;
	protected MenuItem mntmEdit;
	protected MenuItem mntmInsertNew;
	protected MenuItem mntmDuplicate;
	protected MenuItem mntmDelete;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LMenuCollection(Composite parent, int style) {
		super(parent, style);
	    menu = new Menu(tree);
	    tree.setMenu(menu);
	}
	
	public LInsertEvent<T> insert(LPath parentPath, int index, LDataTree<T> node) {
		TreeItem parent = toTreeItem(parentPath);
		createTreeItem(parent, index, node);
		refreshItems();
		return new LInsertEvent<T>(parentPath, index, node);
	}
	
	public LDeleteEvent<T> delete(LPath parentPath, int index) {
		TreeItem item = toTreeItem(parentPath, index);
		LDataTree<T> node = disposeTreeItem(item);
		refreshItems();
		return new LDeleteEvent<T>(parentPath, index, node);
	}
	
	public LEditEvent<ST> edit(LPath path) {
		return null;
	}
	
	public abstract LDataTree<T> emptyNode();
	
	public abstract LDataTree<T> duplicateNode(LPath nodePath);
	
	//-------------------------------------------------------------------------------------
	// Modify Menu
	//-------------------------------------------------------------------------------------
	
	public void setEditEnabled(boolean value) {
		if (value) {
			LMenuCollection<T, ST> self = this;
			if (mntmEdit == null) {
			    mntmEdit = new MenuItem(menu, SWT.NONE);
			    mntmEdit.addSelectionListener(new SelectionAdapter() {
			    	@Override
			    	public void widgetSelected(SelectionEvent arg0) {
			    		if (tree.getSelectionCount() > 0) {
			    			TreeItem item = tree.getSelection()[0];
			    			LPath path = toPath(item);
			    			LEditEvent<ST> event = edit(path);
			    			if (event != null) {
				    			if (actionStack != null) {
				    				LEditAction<ST> action = new LEditAction<ST>(self, path, event.oldData, event.newData);
				    				actionStack.newAction(action);
				    			}
				    			notifyEditListeners(event);
				    			renameItem(path);
				    			notifySelectionListeners(select(event.path));
			    			}
			    		}
			    	}
			    });
			    mntmEdit.setText("Edit");
			}
		} else {
			if (mntmEdit != null) {
				mntmEdit.dispose();
			}
		}
	}
	
	public void setInsertNewEnabled(boolean value) {
		if (value) {
			LMenuCollection<T, ST> self = this;
			if (mntmInsertNew == null) {
			    mntmInsertNew = new MenuItem(menu, SWT.NONE);
			    mntmInsertNew.addSelectionListener(new SelectionAdapter() {
			    	@Override
			    	public void widgetSelected(SelectionEvent arg0) {
			    		LPath parentPath = null;
			    		int index = -1;
			    		LDataTree<T> newNode = emptyNode();
			    		if (tree.getSelectionCount() > 0) {
			    			TreeItem item = tree.getSelection()[0];
			    			parentPath = toPath(item.getParentItem());
			    			index = indexOf(item) + 1;
			    		}
		    			LInsertEvent<T> event = insert(parentPath, index, newNode);
		    			if (event != null) {
				    		if (actionStack != null) {
				    			LInsertAction<T> action = new LInsertAction<T>(self, parentPath, index, newNode); 
				    			actionStack.newAction(action);
				    		}
			    			notifyInsertListeners(event);
			    			notifySelectionListeners(select(event.parentPath, event.index));
		    			}
			    	}
			    });
			    mntmInsertNew.setText("Insert new");
			}
		} else {
			if (mntmInsertNew != null) {
				mntmInsertNew.dispose();
			}
		}
	}
	
	public void setDuplicateEnabled(boolean value) {
		if (value) {
			LMenuCollection<T, ST> self = this;
			if (mntmDuplicate == null) {
			    mntmDuplicate = new MenuItem(menu, SWT.NONE);
			    mntmDuplicate.addSelectionListener(new SelectionAdapter() {
			    	@Override
			    	public void widgetSelected(SelectionEvent arg0) {
			    		if (tree.getSelectionCount() > 0) {
			    			TreeItem item = tree.getSelection()[0];
			    			LPath itemPath = toPath(item);
			    			LDataTree<T> node = duplicateNode(itemPath);
			    			LPath parentPath = toPath(item.getParentItem());
			    			LInsertEvent<T> event = insert(parentPath, indexOf(item), node);
			    			if (event != null) {
				    			event.detail = 1;
				    			if (actionStack != null) {
				    				LInsertAction<T> action = new LInsertAction<T>(self, parentPath, event.index, node);
				    				actionStack.newAction(action);
				    			}
				    			notifyInsertListeners(event);
				    			notifySelectionListeners(select(event.parentPath, event.index));
			    			}
			    		}
			    	}
			    });
			    mntmDuplicate.setText("Duplicate");
			}
		} else {
			if (mntmDuplicate != null) {
				mntmDuplicate.dispose();
			}
		}
	}
	
	public void setDeleteEnabled(boolean value) {
		if (value) {
			LMenuCollection<T, ST> self = this;
			if (mntmDelete == null) {
			    mntmDelete = new MenuItem(menu, SWT.NONE);
			    mntmDelete.addSelectionListener(new SelectionAdapter() {
			    	@Override
			    	public void widgetSelected(SelectionEvent arg0) {
			    		if (tree.getSelectionCount() > 0) {
			    			TreeItem item = tree.getSelection()[0];
			    			LPath parentPath = toPath(item.getParentItem());
			    			LDeleteEvent<T> event = delete(parentPath, indexOf(item));
			    			if (event != null) {
				    			if (actionStack != null) {
				    				LDeleteAction<T> action = new LDeleteAction<T>(self, parentPath, event.index, event.stringNode);
				    				actionStack.newAction(action);
				    			}
				    			notifyDeleteListeners(event);
				    			notifySelectionListeners(select(event.parentPath, event.index));
			    			}
			    		}
			    	}
			    });
			    mntmDelete.setText("Delete");
			}
		} else {
			if (mntmDelete != null) {
				mntmDelete.dispose();
			}
		}
	}

	//-------------------------------------------------------------------------------------
	// Listeners
	//-------------------------------------------------------------------------------------
	
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
	
}
