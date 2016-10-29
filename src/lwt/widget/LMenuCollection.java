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

public abstract class LMenuCollection extends LCollection {

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
	
	public LInsertEvent insertTreeItem(LPath parentPath, int index, LDataTree<String> stringNode) {
		TreeItem parent = toTreeItem(parentPath);
		createTreeItem(parent, index, stringNode);
		return new LInsertEvent(parentPath, index, stringNode);
	}
	
	public LDeleteEvent deleteTreeItem(LPath parentPath, int index) {
		TreeItem item = toTreeItem(parentPath, index);
		LDataTree<String> node = disposeTreeItem(item);
		return new LDeleteEvent(parentPath, index, node);
	}
	
	public LEditEvent editTreeItem(LPath path) {
		return null;
	}
	
	//-------------------------------------------------------------------------------------
	// Modify Menu
	//-------------------------------------------------------------------------------------
	
	public void setEditEnabled(boolean value) {
		if (value) {
			LMenuCollection self = this;
			if (mntmEdit == null) {
			    mntmEdit = new MenuItem(menu, SWT.NONE);
			    mntmEdit.addSelectionListener(new SelectionAdapter() {
			    	@Override
			    	public void widgetSelected(SelectionEvent arg0) {
			    		if (tree.getSelectionCount() > 0) {
			    			TreeItem item = tree.getSelection()[0];
			    			LPath path = toPath(item);
			    			LEditEvent event = editTreeItem(path);
			    			if (event != null) {
				    			if (actionStack != null) {
				    				LEditAction action = new LEditAction(self, path, event.oldData, event.newData);
				    				actionStack.newAction(action);
				    			}
				    			notifyEditListeners(event);
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
			LMenuCollection self = this;
			if (mntmInsertNew == null) {
			    mntmInsertNew = new MenuItem(menu, SWT.NONE);
			    mntmInsertNew.addSelectionListener(new SelectionAdapter() {
			    	@Override
			    	public void widgetSelected(SelectionEvent arg0) {
			    		LPath parentPath = null;
			    		int index = -1;
			    		LDataTree<String> stringNode = new LDataTree<>("New Item");
			    		if (tree.getSelectionCount() > 0) {
			    			TreeItem item = tree.getSelection()[0];
			    			parentPath = toPath(item.getParentItem());
			    			index = indexOf(item);
			    		}
		    			LInsertEvent event = insertTreeItem(parentPath, index, stringNode);
		    			if (event != null) {
				    		if (actionStack != null) {
				    			LInsertAction action = new LInsertAction(self, parentPath, index, stringNode); 
				    			actionStack.newAction(action);
				    		}
			    			notifyInsertListeners(event);
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
			LMenuCollection self = this;
			if (mntmDuplicate == null) {
			    mntmDuplicate = new MenuItem(menu, SWT.NONE);
			    mntmDuplicate.addSelectionListener(new SelectionAdapter() {
			    	@Override
			    	public void widgetSelected(SelectionEvent arg0) {
			    		if (tree.getSelectionCount() > 0) {
			    			TreeItem item = tree.getSelection()[0];
			    			LDataTree<String> node = toStringNode(item);
			    			LPath parentPath = toPath(item.getParentItem());
			    			LInsertEvent event = insertTreeItem(parentPath, indexOf(item), node);
			    			if (event != null) {
				    			event.detail = 1;
				    			if (actionStack != null) {
				    				LInsertAction action = new LInsertAction(self, parentPath, event.index, node);
				    				actionStack.newAction(action);
				    			}
				    			notifyInsertListeners(event);
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
			LMenuCollection self = this;
			if (mntmDelete == null) {
			    mntmDelete = new MenuItem(menu, SWT.NONE);
			    mntmDelete.addSelectionListener(new SelectionAdapter() {
			    	@Override
			    	public void widgetSelected(SelectionEvent arg0) {
			    		if (tree.getSelectionCount() > 0) {
			    			TreeItem item = tree.getSelection()[0];
			    			LPath parentPath = toPath(item.getParentItem());
			    			LDeleteEvent event = deleteTreeItem(parentPath, indexOf(item));
			    			if (event != null) {
				    			if (actionStack != null) {
				    				LDeleteAction action = new LDeleteAction(self, parentPath, event.index, event.stringNode);
				    				actionStack.newAction(action);
				    			}
				    			notifyDeleteListeners(event);
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
	
	protected ArrayList<LCollectionListener> editListeners = new ArrayList<>();
	public void addEditListener(LCollectionListener listener) {
		editListeners.add(listener);
	}
	
	protected ArrayList<LCollectionListener> insertListeners = new ArrayList<>();
	public void addInsertListener(LCollectionListener listener) {
		insertListeners.add(listener);
	}
		
	protected ArrayList<LCollectionListener> deleteListeners = new ArrayList<>();
	public void addDeleteListener(LCollectionListener listener) {
		deleteListeners.add(listener);
	}
	
	//-------------------------------------------------------------------------------------
	// Listener Notify
	//-------------------------------------------------------------------------------------
	
	public void notifyEditListeners(LEditEvent event) {
		for(LCollectionListener listener : editListeners) {
			listener.onEdit(event);
		}
	}
	
	public void notifyInsertListeners(LInsertEvent event) {
		for(LCollectionListener listener : insertListeners) {
			listener.onInsert(event);
		}
	}
	
	public void notifyDeleteListeners(LDeleteEvent event) {
		for(LCollectionListener listener : deleteListeners) {
			listener.onDelete(event);
		}
	}
	
}
