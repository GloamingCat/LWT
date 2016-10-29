package lwt.widget;

import java.util.ArrayList;
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import lwt.action.collection.LMoveAction;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LMoveEvent;
import lwt.event.LSelectionEvent;
import lwt.event.listener.LCollectionListener;
import lwt.event.listener.LSelectionListener;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public abstract class LCollection extends LWidget {
	
	protected final String BLOCK = "block";
	
	protected Tree tree;
	private TreeItem dragSource;
	
	public LCollection(Composite parent, int style) {
		super(parent, style);

	    tree = new Tree(this, SWT.BORDER);
	    tree.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent arg0) {
	    		if (tree.getSelectionCount() > 0) {
	    			TreeItem item = tree.getSelection()[0];
	    			LSelectionEvent event = new LSelectionEvent(item);
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
	    			dragSource = selection[0];
	    			setDraggable(dragSource, false);
	    			event.doit = true;
	    		} else {
	    			event.doit = false;
	    		}
	    	};

			public void dragSetData(DragSourceEvent event) {
				event.data = "oi";
			}

			public void dragFinished(DragSourceEvent event) {
				if (event.detail != DND.DROP_MOVE) {
					setDraggable(dragSource, true);
				}
				dragSource = null;
			}
			
	    });
	    return source;
	}
	
	private DropTarget createDropTarget(Transfer[] types, int operations) {
	    final LCollection self = this;
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
	    			LMoveEvent e = moveTreeItem(dragSource, null, -1);
	    			if (actionStack != null) {
	    				LMoveAction action = new LMoveAction(self, e.sourceParent, e.sourceIndex, e.destParent, e.destIndex);
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
					LMoveEvent e = moveTreeItem(dragSource, dragDest, -1);
	    			if (actionStack != null) {
	    				LMoveAction action = new LMoveAction(self, e.sourceParent, e.sourceIndex, e.destParent, e.destIndex);
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
				LMoveEvent e = moveTreeItem(dragSource, parent, index);
    			if (actionStack != null) {
    				LMoveAction action = new LMoveAction(self, e.sourceParent, e.sourceIndex, e.destParent, e.destIndex);
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
	
	public Tree getTree() {
		return tree;
	}
	
	protected abstract int indexByBounds(Point pt, Rectangle bounds);

	protected void setDraggable(TreeItem item, boolean value) {
		if (value)
			item.setData(BLOCK, null);
		else
			item.setData(BLOCK, BLOCK);
		for(TreeItem child : item.getItems()) {
			setDraggable(child, value);
		}
	}
	
	protected int indexOf(TreeItem item) {
		if (item.getParentItem() == null) {
			return tree.indexOf(item);
		} else {
			return item.getParentItem().indexOf(item);
		}
	}

	public TreeItem createTreeItem(TreeItem parent, int index, LDataTree<String> stringNode) {
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
		fromStringNode(newItem, stringNode);
		return newItem;
	}
	
	public LDataTree<String> disposeTreeItem(TreeItem item) {
		LDataTree<String> stringNode = toStringNode(item);
		item.dispose();
		return stringNode;
	}
	
	public LMoveEvent moveTreeItem(TreeItem item, TreeItem parent, int destIndex) {
		LPath destParent = toPath(parent);
		LPath sourceParent = null;
		int sourceIndex;
		if (item.getParentItem() == null) {
			sourceIndex = tree.indexOf(item);
		} else {
			sourceIndex = item.getParentItem().indexOf(item);
			sourceParent = toPath(item.getParentItem());
		}
		LDataTree<String> stringNode = toStringNode(item);
		createTreeItem(parent, destIndex, stringNode);
		disposeTreeItem(item);
		return new LMoveEvent(sourceParent, destParent, sourceIndex, destIndex, stringNode);
	}
	
	//-------------------------------------------------------------------------------------
	// Path
	//-------------------------------------------------------------------------------------
	
	public TreeItem toTreeItem(LPath parentPath, int index) {
		if (parentPath == null) {
			return tree.getItem(index);
		} else {
			TreeItem parent = toTreeItem(parentPath);
			return parent.getItem(index);
		}
	}
	
	public TreeItem toTreeItem(LPath path) {
		if (path == null) {
			return null;
		}
		TreeItem item = tree.getItem(path.index);
		path = path.child;
		while(path != null) {
			item = item.getItem(path.index);
			path = path.child;
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
		LPath path = new LPath(tree.indexOf(item));
		while(indexes.isEmpty() == false) {
			path = new LPath(indexes.pop(), path);
		}
		return path;
	}
	
	//-------------------------------------------------------------------------------------
	// String Node
	//-------------------------------------------------------------------------------------
	
	public LDataTree<String> toStringNode(TreeItem item) {
		LDataTree<String> node = new LDataTree<>(item.getText());
		for(TreeItem child : item.getItems()) {
			toStringNode(child).setParent(node);
		}
		return node;
	}
	
	public void fromStringNode(TreeItem item, LDataTree<String> node) {
		item.setText(node.data);
		for(LDataTree<String> child : node.children) {
			TreeItem newItem = new TreeItem(item, item.getStyle());
			fromStringNode(newItem, child);
		}
	}
	
	//-------------------------------------------------------------------------------------
	// Tree Events
	//-------------------------------------------------------------------------------------
	
	public LSelectionEvent select(LPath path) {
		TreeItem item = toTreeItem(path);
		tree.setSelection(item);
		return new LSelectionEvent(item);
	}
	
	public LMoveEvent move(LPath sourceParent, int sourceIndex, LPath destParent, int destIndex) {
		TreeItem sourceItem = toTreeItem(sourceParent, sourceIndex);
		TreeItem destParentItem = toTreeItem(destParent);
		return moveTreeItem(sourceItem, destParentItem, destIndex);
	}
	
	public void setDragEnabled(boolean value) {
		for(TreeItem item : tree.getItems()) {
			setDraggable(item, value);
		}
	}

	//-------------------------------------------------------------------------------------
	// Listeners
	//-------------------------------------------------------------------------------------
	
	protected ArrayList<LSelectionListener> selectionListeners = new ArrayList<>();
	public void addSelectionListener(LSelectionListener listener) {
		selectionListeners.add(listener);
	}
	
	protected ArrayList<LCollectionListener> moveListeners = new ArrayList<>();
	public void addMoveListener(LCollectionListener listener) {
		moveListeners.add(listener);
	}
	
	//-------------------------------------------------------------------------------------
	// Listener Notify
	//-------------------------------------------------------------------------------------
	
	public void notifySelectionListeners(LSelectionEvent event) {
		for(LSelectionListener listener : selectionListeners) {
			listener.onSelect(event);
		}
	}

	public void notifyMoveListeners(LMoveEvent event) {
		for(LCollectionListener listener : moveListeners) {
			listener.onMove(event);
		}
	}
	
	//-------------------------------------------------------------------------------------
	// Refresh
	//-------------------------------------------------------------------------------------
	
	public void refresh() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			notifySelectionListeners(new LSelectionEvent(item));
		} else if (tree.getItemCount() > 0) {
			TreeItem item = tree.getItems()[0];
			notifySelectionListeners(new LSelectionEvent(item));			
		} else {
			notifySelectionListeners(new LSelectionEvent(null));
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
	
	public Object getSelectedObject() {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			return item.getData();
		} else if (tree.getItemCount() > 0) {
			TreeItem item = tree.getItems()[0];
			return item.getData();		
		} else {
			return null;
		}
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
