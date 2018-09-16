package lwt.widget;

import java.util.Stack;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import lwt.action.collection.LMoveAction;
import lwt.dataestructure.LDataCollection;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LMoveEvent;
import lwt.event.LSelectionEvent;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public abstract class LTreeBase<T, ST> extends LSelectableCollection<T, ST> {
	
	protected static final String BLOCK = "block";
	
	protected Tree tree;
	protected LDataCollection<T> dataCollection;
	
	private LDataTree<T> dragNode;
	private TreeItem dragParent;
	private int dragIndex;
	
	public LTreeBase(Composite parent, int style) {
		super(parent, style);

	    tree = new Tree(this, SWT.BORDER | SWT.VIRTUAL);
	    tree.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent arg0) {
	    		if (tree.getSelectionCount() > 0) {
	    			TreeItem item = tree.getSelection()[0];
	    			LPath path = toPath(item);
	    			LSelectionEvent event = new LSelectionEvent(path, toObject(path));
	    			event.detail = arg0.detail;
	    			notifySelectionListeners(event);
	    		}
	    	}
	    });
	    
	    Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
	    int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;

	    createDragSource(types, operations);
	    createDropTarget(types, operations);
	}
	
	private DragSource createDragSource(Transfer[] types, int operations) {
	    final DragSource source = new DragSource(tree, operations);
	    source.setTransfer(types);
	    source.addDragListener(new DragSourceListener() {
	    	
	    	public void dragStart(DragSourceEvent event) {
	    		TreeItem[] selection = tree.getSelection();
	    		if (selection.length > 0) {
	    			Object block = selection[0].getData(BLOCK);
	    			if (block != null) {
	    				event.doit = false;
	    				return;
	    			}
	    			dragNode = toNode(selection[0]);
	    			if (selection[0].getParentItem() != null) {
	    				dragParent = selection[0].getParentItem();
	    				dragIndex = selection[0].getParentItem().indexOf(selection[0]);
	    			} else {
	    				dragParent = null;
	    				dragIndex = tree.indexOf(selection[0]);
	    			}
	    			selection[0].dispose();
	    			event.doit = true;
	    		} else {
	    			event.doit = false;
	    		}
	    	};

			public void dragSetData(DragSourceEvent event) {
				event.data = "bla";
			}

			public void dragFinished(DragSourceEvent event) {
				if (event.detail == DND.DROP_NONE) {
					TreeItem item = createTreeItem(dragParent, dragIndex, dragNode);
					notifySelectionListeners(selectTreeItem(item));
				}
				refreshAll();
				dragParent = null;
				dragIndex = -1;
				dragNode = null;
			}
			
	    });
	    return source;
	}
	
	private DropTarget createDropTarget(Transfer[] types, int operations) {
	    final LTreeBase<T, ST> self = this;
	    final Display display = Display.getCurrent();
	    DropTarget target = new DropTarget(tree, operations);
	    target.setTransfer(types);
	    
	    target.addDropListener(new DropTargetAdapter() {
	    	
	    	public void dragOver(DropTargetEvent event) {
	    		if (event.item != null) {
	    			TreeItem item = (TreeItem) event.item;
	    			Object block  = item.getData(BLOCK);
	    			if (block != null) {
	    				event.feedback = 0;
	    				return;
	    			}
	    			event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;
	    			Point pt = display.map(null, tree, event.x, event.y);
	    			Rectangle bounds = item.getBounds();
	    			int d = indexByBounds(pt, bounds);
	    			if (d == 0) {
	    				event.feedback |= DND.FEEDBACK_INSERT_BEFORE;
	    			} else if (d == 1) {
	    				event.feedback |= DND.FEEDBACK_INSERT_AFTER;
	    			} else {
	    				event.feedback |= DND.FEEDBACK_SELECT;
	    			}
	    		}
	    	}
	    	
	    	public void drop(DropTargetEvent event) {
	    		if (event.data == null) {
	    			event.detail = DND.DROP_NONE;
	    			return;
	    		}
	    		TreeItem dragDest = (TreeItem) event.item;
	    		if (dragDest == null) {
	    			LMoveEvent<T> e = moveTreeItem(dragNode, dragParent, dragIndex, null, -1);
	    			if (e == null) {
	    				event.detail = DND.DROP_NONE;
	    				return;
	    			}
	    			if (actionStack != null) {
	    				LMoveAction<T> action = new LMoveAction<T>(self, e.sourceParent, e.sourceIndex, e.destParent, e.destIndex);
	    				actionStack.newAction(action);
	    			}
	    			notifyMoveListeners(e);
	    			return;
	    		}
	    		Object block = dragDest.getData(BLOCK);
	    		if (block != null) {
	    			event.detail = DND.DROP_NONE;
	    			return;
	    		}
				Point pt = display.map(null, tree, event.x, event.y);
				Rectangle bounds = dragDest.getBounds();
				int d = indexByBounds(pt, bounds);
				if (d == -1) {
					LMoveEvent<T> e = moveTreeItem(dragNode, dragParent, dragIndex, dragDest, -1);
	    			if (e == null) {
	    				event.detail = DND.DROP_NONE;
	    				return;
	    			}
	    			if (actionStack != null) {
	    				LMoveAction<T> action = new LMoveAction<T>(self, e.sourceParent, e.sourceIndex, e.destParent, e.destIndex);
	    				actionStack.newAction(action);
	    			}
					notifyMoveListeners(e);
					return;
				}
				TreeItem parent = dragDest.getParentItem();
				int index = d;
				if (parent != null) {
					index += parent.indexOf(dragDest);
				} else {
					index += tree.indexOf(dragDest);
				}
				LMoveEvent<T> e = moveTreeItem(dragNode, dragParent, dragIndex, parent, index);
    			if (e == null) {
    				event.detail = DND.DROP_NONE;
    				return;
    			}
    			if (actionStack != null) {
    				LMoveAction<T> action = new LMoveAction<T>(self, e.sourceParent, e.sourceIndex, e.destParent, e.destIndex);
    				actionStack.newAction(action);
    			}
				notifyMoveListeners(e);
	    	}
	    });
	    return target;
	}
	
	//-------------------------------------------------------------------------------------
	// Auxiliary
	//-------------------------------------------------------------------------------------
	
	protected int indexByBounds(Point pt, Rectangle bounds) {
		if (pt.y < bounds.y + bounds.height / 3) {
			return 0;
		} else if (pt.y > bounds.y + 2 * bounds.height / 3) {
			return 1;
		} else {
			return -1;
		}
	}
	
	protected int indexOf(TreeItem item) {
		if (item.getParentItem() == null) {
			return tree.indexOf(item);
		} else {
			return item.getParentItem().indexOf(item);
		}
	}
	
	protected boolean isOutOfBounds(TreeItem parent, int i) {
		if (parent == null) {
			return i >= tree.getItemCount();
		} else {
			return i >= parent.getItemCount();
		}
	}
	
	//-------------------------------------------------------------------------------------
	// Internal operations
	//-------------------------------------------------------------------------------------

	protected LSelectionEvent selectTreeItem(TreeItem item) {
		if (item == null) {
			tree.deselectAll();
			return new LSelectionEvent(null, null);
		} else {
			tree.setSelection(item);
			LPath path = toPath(item);
			return new LSelectionEvent(path, toObject(path));
		}
	}
	
	protected TreeItem createTreeItem(TreeItem parent, int index, LDataTree<T> node) {
		TreeItem newItem;
		if (index == -1) {
			if (parent == null) {
				newItem = new TreeItem(tree, SWT.NONE);
			} else {
				newItem = new TreeItem(parent, SWT.NONE);
			}
		} else {
			if (parent == null) {
				newItem = new TreeItem(tree, SWT.NONE, index);
			} else {
				newItem = new TreeItem(parent, SWT.NONE, index);
			}
		}
		newItem.setImage((Image) null);
		setItemNode(newItem, node);
		return newItem;
	}
	
	protected LDataTree<T> disposeTreeItem(TreeItem item) {
		LDataTree<T> data = toNode(item);
		item.dispose();
		return data;
	}
	
	protected LMoveEvent<T> moveTreeItem(LDataTree<T> node, TreeItem sourceParent, int sourceIndex, 
			TreeItem destParent, int destIndex) {
		if (sourceParent == destParent && destIndex == sourceIndex)
			return null;
		LPath sourceParentPath = toPath(sourceParent);
		LPath destParentPath = toPath(destParent);
		createTreeItem(destParent, destIndex, node);
		return new LMoveEvent<T>(sourceParentPath, sourceIndex, destParentPath, destIndex, node);
	}
	
	//-------------------------------------------------------------------------------------
	// Tree Events
	//-------------------------------------------------------------------------------------
	
	public LSelectionEvent select(LPath path) {
		if (path == null) {
			tree.deselectAll();
			return new LSelectionEvent(null, null);
		}
		TreeItem item = toTreeItem(path);
		tree.setSelection(item);
		return new LSelectionEvent(path, toObject(path));
	}
	
	public LMoveEvent<T> move(LPath sourceParent, int sourceIndex, LPath destParent, int destIndex) {
		try {
			TreeItem sourceItem = toTreeItem(sourceParent, sourceIndex);
			LDataTree<T> node = disposeTreeItem(sourceItem);
			LMoveEvent<T> e = moveTreeItem(node, toTreeItem(sourceParent), sourceIndex, 
					toTreeItem(destParent), destIndex);
			refreshAll();
			return e;
		} catch(Exception e) {
			String dest = destParent == null ? "" : destParent.toString();
			String src = sourceParent == null ? "" : sourceParent.toString();
			System.out.println("Try move: " + src + sourceIndex + " to " + dest + destIndex);
			throw e;
		}
	}
	
	//-------------------------------------------------------------------------------------
	// Node
	//-------------------------------------------------------------------------------------
	
	public abstract LDataTree<T> toNode(LPath path);
	
	public LDataTree<T> toNode(TreeItem item) {
		LPath path = toPath(item);
		return toNode(path);
	}
	
	//-------------------------------------------------------------------------------------
	// Path
	//-------------------------------------------------------------------------------------
	
	public TreeItem toTreeItem(TreeItem parent, int index) {
		if (index == -1) {
			if (parent == null) {
				return tree.getItems()[tree.getItemCount() - 1];
			} else {
				return parent.getItems()[parent.getItemCount() - 1];
			}
		} else {
			if (parent == null) {
				return tree.getItems()[index];
			} else {
				return parent.getItems()[index];
			}
		}
	}
	
	public TreeItem toTreeItem(LPath parentPath, int index) {
		if (parentPath == null) {
			if (index >= tree.getItemCount() || tree.getItemCount() == 0)
				return null;
			if (index == -1)
				index = tree.getItemCount() - 1;
			return tree.getItems()[index];
		} else {
			TreeItem parent = toTreeItem(parentPath);
			if (index >= parent.getItemCount() || parent.getItemCount() == 0)
				return null;
			if (index == -1)
				index = parent.getItemCount() - 1;
			return parent.getItems()[index];
		}
	}
	
	public TreeItem toTreeItem(LPath path) {
		if (path == null) {
			return null;
		}
		TreeItem item = null;
		try {
			if (path.index == -1)
				path.index = tree.getItemCount() - 1;
			item = tree.getItems()[path.index];
			path = path.child;
			while(path != null) {
				if (path.index == -1)
					path.index = item.getItemCount() - 1;
				item = item.getItems()[path.index];
				path = path.child;
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			ArrayIndexOutOfBoundsException e2 = new ArrayIndexOutOfBoundsException(
					e.getMessage() + " " + path.index);
			e2.setStackTrace(e.getStackTrace());
			return null;
		}
		return item;
	}
	
	public LPath toPath(TreeItem item) {
		if (item == null) {
			return null;
		}
		Stack<Integer> indexes = new Stack<>();
		TreeItem parent = item.getParentItem();
		while(parent != null) {
			indexes.push(parent.indexOf(item));
			item = parent;
			parent = item.getParentItem();
		}
		LPath root = new LPath(tree.indexOf(item));
		LPath path = root;
		while(indexes.isEmpty() == false) {
			path.child = new LPath(indexes.pop());
			path = path.child;
		}
		return root;
	}
	
	//-------------------------------------------------------------------------------------
	// String Node
	//-------------------------------------------------------------------------------------
	
	public void setDataCollection(LDataCollection<T> collection) {
		if (collection == null) {
			setItems(null);
		} else {
			setItems(collection.toTree());
		}
	}
	
	public void setItems(LDataTree<T> root) {
		clear();
		if (root == null) {
			tree.setEnabled(false);
		} else {
			tree.setEnabled(true);
			for(LDataTree<T> child : root.children) {
				createTreeItem(null, -1, child);
			}
		}
	}
	
	public void setItemNode(TreeItem item, LDataTree<T> node) {
		item.setText(dataToString(node.data));
		item.setData(node.data);
		for(LDataTree<T> child : node.children) {
			TreeItem newItem = new TreeItem(item, item.getStyle());
			setItemNode(newItem, child);
		}
	}
	
	protected String dataToString(T data) {
		return data.toString();
	}
	
	//-------------------------------------------------------------------------------------
	// Drag enable
	//-------------------------------------------------------------------------------------
	
	public void setDragEnabled(boolean value) {
		for(TreeItem item : tree.getItems()) {
			setDraggable(item, value);
		}
	}
	
	protected void setDraggable(TreeItem item, boolean value) {
		if (value)
			item.setData(BLOCK, null);
		else
			item.setData(BLOCK, BLOCK);
		for(TreeItem child : item.getItems()) {
			setDraggable(child, value);
		}
	}
	
	//-------------------------------------------------------------------------------------
	// Refresh
	//-------------------------------------------------------------------------------------
	
	public void forceSelection(LPath path) {
		TreeItem item = toTreeItem(path);
		if (item == null) {
			tree.deselectAll();
			notifySelectionListeners(new LSelectionEvent(null, null));
		} else {
			tree.select(item);
			notifySelectionListeners(new LSelectionEvent(path, toObject(path)));
		}
	}
	
	public void forceSelection(LPath parent, int index) {
		if (parent == null) {
			parent = new LPath(index);
		} else {
			parent = parent.addLast(index);
		}
		forceSelection(parent);
	}
	
	public void refreshSelection() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			LPath path = toPath(item);
			notifySelectionListeners(new LSelectionEvent(path, toObject(path)));
		} else if (tree.getItemCount() > 0) {
			TreeItem item = tree.getItems()[0];
			LPath path = toPath(item);
			notifySelectionListeners(new LSelectionEvent(path, toObject(path)));		
		} else {
			notifySelectionListeners(new LSelectionEvent(null, null));
		}
	}
	
	public void refreshObject(LPath path) {
		TreeItem item = toTreeItem(path);
		if (item != null) {
			item.setText(dataToString(toObject(path)));
		}
	}
	
	public void refreshAll() {
		if (dataCollection != null) {
			int i = 0;
			for (LDataTree<T> child : dataCollection.toTree().children) {
				refreshNode(child, tree.getItems()[i]);
				i++;
			}
		}
	}
	
	private void refreshNode(LDataTree<T> node, TreeItem item) {
		item.setText(dataToString(node.data));
		int i = 0;
		for (LDataTree<T> child : node.children) {
			refreshNode(child, item.getItems()[i]);
		}
	}
	
	public void clear() {
		for(TreeItem item : tree.getItems()) {
			item.dispose();
		}
	}
	
	//-------------------------------------------------------------------------------------
	// Selection
	//-------------------------------------------------------------------------------------	
	
	public T getSelectedObject() {
		LPath path = getSelectedPath();
		return toObject(path);
	}

	public LPath getSelectedPath() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			return toPath(item);
		} else if (tree.getItemCount() > 0) {
			TreeItem item = tree.getItems()[0];
			return toPath(item);		
		} else {
			return null;
		}
	}
	
}
