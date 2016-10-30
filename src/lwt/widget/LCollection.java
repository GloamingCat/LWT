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

public abstract class LCollection<T> extends LWidget {
	
	protected final String BLOCK = "block";
	
	protected Tree tree;
	private LDataTree<T> dragNode;
	private TreeItem dragParent;
	private int dragIndex;
	
	public LCollection(Composite parent, int style) {
		super(parent, style);

	    tree = new Tree(this, SWT.BORDER);
	    tree.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent arg0) {
	    		if (tree.getSelectionCount() > 0) {
	    			TreeItem item = tree.getSelection()[0];
	    			LPath path = toPath(item);
	    			//path.print();
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
				event.data = dragNode.data.toString();
			}

			public void dragFinished(DragSourceEvent event) {
				if (event.detail == DND.DROP_NONE) {
					System.out.println("Drag cancelled");
					createTreeItem(dragParent, dragIndex, dragNode);
				}
				dragParent = null;
				dragIndex = -1;
				dragNode = null;
			}
			
	    });
	    return source;
	}
	
	private DropTarget createDropTarget(Transfer[] types, int operations) {
	    final LCollection<T> self = this;
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
	
	protected abstract int indexByBounds(Point pt, Rectangle bounds);
	
	protected int indexOf(TreeItem item) {
		if (item.getParentItem() == null) {
			return tree.indexOf(item);
		} else {
			return item.getParentItem().indexOf(item);
		}
	}
	
	//-------------------------------------------------------------------------------------
	// Internal operations
	//-------------------------------------------------------------------------------------

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
		setItemNode(newItem, node);
		return newItem;
	}
	
	protected LDataTree<T> disposeTreeItem(TreeItem item) {
		LDataTree<T> stringNode = toNode(item);
		item.dispose();
		return stringNode;
	}
	
	protected LMoveEvent<T> moveTreeItem(LDataTree<T> node, TreeItem sourceParent, int sourceIndex, 
			TreeItem destParent, int destIndex) {
		if (sourceParent == destParent && destIndex == sourceIndex)
			return null;
		createTreeItem(destParent, destIndex, node);
		return new LMoveEvent<T>(toPath(sourceParent), sourceIndex, toPath(destParent), destIndex, node);
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
	
	public TreeItem toTreeItem(LPath parentPath, int index) {
		if (parentPath == null) {
			if (index == -1)
				index = tree.getItemCount() - 1;
			return tree.getItem(index);
		} else {
			TreeItem parent = toTreeItem(parentPath);
			if (index == -1)
				index = parent.getItemCount() - 1;
			return parent.getItem(index);
		}
	}
	
	public TreeItem toTreeItem(LPath path) {
		if (path == null) {
			return null;
		}
		if (path.index == -1)
			path.index = tree.getItemCount() - 1;
		TreeItem item = tree.getItem(path.index);
		path = path.child;
		while(path != null) {
			if (path.index == -1)
				path.index = item.getItemCount() - 1;
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
		LPath root = new LPath(tree.indexOf(item));
		LPath path = root;
		while(indexes.isEmpty() == false) {
			path = new LPath(indexes.pop(), path);
		}
		return root;
	}
	
	public abstract Object toObject(LPath path);
	
	//-------------------------------------------------------------------------------------
	// String Node
	//-------------------------------------------------------------------------------------
	
	public void renameCurrentItem() {
		TreeItem[] s = tree.getSelection();
		if (s.length > 0) {
			LPath path = toPath(s[0]);
			s[0].setText(toObject(path).toString());
			System.out.println("nfisnfudi");
		}
	}
	
	public void setItems(LDataTree<T> root) {
		clear();
		for(LDataTree<T> child : root.children) {
			createTreeItem(null, -1, child);
		}
	}
	
	public LDataTree<String> toStringNode(TreeItem item) {
		LDataTree<String> node = new LDataTree<>(item.getText());
		for(TreeItem child : item.getItems()) {
			toStringNode(child).setParent(node);
		}
		return node;
	}
	
	public void setItemNode(TreeItem item, LDataTree<T> node) {
		item.setText(node.data.toString());
		for(LDataTree<T> child : node.children) {
			TreeItem newItem = new TreeItem(item, item.getStyle());
			setItemNode(newItem, child);
		}
	}
	
	//-------------------------------------------------------------------------------------
	// Tree Events
	//-------------------------------------------------------------------------------------
	
	public LSelectionEvent select(LPath path) {
		TreeItem item = toTreeItem(path);
		tree.setSelection(item);
		return new LSelectionEvent(path, toObject(path));
	}
	
	public LMoveEvent<T> move(LPath sourceParent, int sourceIndex, LPath destParent, int destIndex) {
		TreeItem sourceItem = toTreeItem(sourceParent, sourceIndex);
		LDataTree<T> node = disposeTreeItem(sourceItem);
		return moveTreeItem(node, toTreeItem(sourceParent), sourceIndex, toTreeItem(destParent), destIndex);
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
	// Listeners
	//-------------------------------------------------------------------------------------
	
	protected ArrayList<LSelectionListener> selectionListeners = new ArrayList<>();
	public void addSelectionListener(LSelectionListener listener) {
		selectionListeners.add(listener);
	}
	
	protected ArrayList<LCollectionListener<T>> moveListeners = new ArrayList<>();
	public void addMoveListener(LCollectionListener<T> listener) {
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

	public void notifyMoveListeners(LMoveEvent<T> event) {
		for(LCollectionListener<T> listener : moveListeners) {
			listener.onMove(event);
		}
	}
	
	//-------------------------------------------------------------------------------------
	// Refresh
	//-------------------------------------------------------------------------------------
	
	public void refresh() {
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
	
	public void clear() {
		for(TreeItem item : tree.getItems()) {
			item.dispose();
		}
	}
	
	//-------------------------------------------------------------------------------------
	// Selection
	//-------------------------------------------------------------------------------------	
	
	public Object getSelectedObject() {
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
