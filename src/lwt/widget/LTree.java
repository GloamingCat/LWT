package lwt.widget;

import lwt.LVocab;
import lwt.action.collection.LDeleteAction;
import lwt.action.collection.LEditAction;
import lwt.action.collection.LInsertAction;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LDeleteEvent;
import lwt.event.LEditEvent;
import lwt.event.LInsertEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TreeItem;

public abstract class LTree<T, ST> extends LTreeBase<T, ST> {

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
	public LTree(Composite parent, int style) {
		super(parent, style);
	    menu = new Menu(tree);
	    tree.setMenu(menu);
	}
	
	//-------------------------------------------------------------------------------------
	// Menu buttons
	//-------------------------------------------------------------------------------------
	
	public void setEditEnabled(boolean value) {
		if (value) {
			if (mntmEdit == null) {
			    mntmEdit = new MenuItem(menu, SWT.NONE);
			    mntmEdit.addSelectionListener(new SelectionAdapter() {
			    	@Override
			    	public void widgetSelected(SelectionEvent arg0) {
			    		onEditButton();
			    	}
			    });
			    mntmEdit.setText(LVocab.instance.EDIT);
			}
		} else {
			if (mntmEdit != null) {
				mntmEdit.dispose();
			}
		}
	}
	
	public void setInsertNewEnabled(boolean value) {
		if (value) {
			if (mntmInsertNew == null) {
			    mntmInsertNew = new MenuItem(menu, SWT.NONE);
			    mntmInsertNew.addSelectionListener(new SelectionAdapter() {
			    	@Override
			    	public void widgetSelected(SelectionEvent arg0) {
			    		onInsertNewButton();
			    	}
			    });
			    mntmInsertNew.setText(LVocab.instance.INSERTNEW);
			}
		} else {
			if (mntmInsertNew != null) {
				mntmInsertNew.dispose();
			}
		}
	}
	
	public void setDuplicateEnabled(boolean value) {
		if (value) {
			if (mntmDuplicate == null) {
			    mntmDuplicate = new MenuItem(menu, SWT.NONE);
			    mntmDuplicate.addSelectionListener(new SelectionAdapter() {
			    	@Override
			    	public void widgetSelected(SelectionEvent arg0) {
			    		onDuplicateButton();
			    	}
			    });
			    mntmDuplicate.setText(LVocab.instance.DUPLICATE);
			}
		} else {
			if (mntmDuplicate != null) {
				mntmDuplicate.dispose();
			}
		}
	}
	
	public void setDeleteEnabled(boolean value) {
		if (value) {
			if (mntmDelete == null) {
			    mntmDelete = new MenuItem(menu, SWT.NONE);
			    mntmDelete.addSelectionListener(new SelectionAdapter() {
			    	@Override
			    	public void widgetSelected(SelectionEvent arg0) {
			    		onDeleteButton();
			    	}
			    });
			    mntmDelete.setText(LVocab.instance.DELETE);
			}
		} else {
			if (mntmDelete != null) {
				mntmDelete.dispose();
			}
		}
	}
	
	//-------------------------------------------------------------------------------------
	// Modify
	//-------------------------------------------------------------------------------------
	
	public LInsertEvent<T> insert(LPath parentPath, int index, LDataTree<T> node) {
		TreeItem parent = toTreeItem(parentPath);
		createTreeItem(parent, index, node);
		refreshAll();
		return new LInsertEvent<T>(parentPath, index, node);
	}
	
	public LDeleteEvent<T> delete(LPath parentPath, int index) {
		TreeItem item = toTreeItem(parentPath, index);
		LDataTree<T> node = disposeTreeItem(item);
		refreshAll();
		return new LDeleteEvent<T>(parentPath, index, node);
	}
	
	public LEditEvent<ST> edit(LPath path) {
		return null;
	}
	
	protected abstract LDataTree<T> emptyNode();
	
	protected abstract LDataTree<T> duplicateNode(LPath nodePath);
	
	//-------------------------------------------------------------------------------------
	// Modify Menu
	//-------------------------------------------------------------------------------------
	
	protected void onEditButton() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			LPath path = toPath(item);
			LEditEvent<ST> event = edit(path);
			if (event != null) {
    			if (actionStack != null) {
    				LEditAction<ST> action = new LEditAction<ST>(this, path, event.oldData, event.newData);
    				actionStack.newAction(action);
    			}
    			notifyEditListeners(event);
    			refreshObject(path);
    			notifySelectionListeners(select(event.path));
			}
		}
	}
	
	protected void onInsertNewButton() {
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
    			LInsertAction<T> action = new LInsertAction<T>(this, parentPath, index, newNode); 
    			actionStack.newAction(action);
    		}
			notifyInsertListeners(event);
			notifySelectionListeners(select(event.parentPath, event.index));
		}
	}
	
	protected void onDuplicateButton() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			LPath itemPath = toPath(item);
			LDataTree<T> node = duplicateNode(itemPath);
			LPath parentPath = toPath(item.getParentItem());
			LInsertEvent<T> event = insert(parentPath, indexOf(item), node);
			if (event != null) {
    			event.detail = 1;
    			if (actionStack != null) {
    				LInsertAction<T> action = new LInsertAction<T>(this, parentPath, event.index, node);
    				actionStack.newAction(action);
    			}
    			notifyInsertListeners(event);
    			notifySelectionListeners(select(event.parentPath, event.index));
			}
		}
	}
	
	protected void onDeleteButton() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			LPath parentPath = toPath(item.getParentItem());
			LDeleteEvent<T> event = delete(parentPath, indexOf(item));
			if (event != null) {
    			if (actionStack != null) {
    				LDeleteAction<T> action = new LDeleteAction<T>(this, parentPath, event.index, event.stringNode);
    				actionStack.newAction(action);
    			}
    			notifyDeleteListeners(event);
    			notifySelectionListeners(select(event.parentPath, event.index));
			}
		}
	}
	
}
