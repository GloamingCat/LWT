package lwt.editor;

import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.dialog.LObjectDialog;
import lwt.event.LDeleteEvent;
import lwt.event.LEditEvent;
import lwt.event.LInsertEvent;
import lwt.event.LMoveEvent;
import lwt.event.listener.LCollectionListener;
import lwt.widget.LTree;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public abstract class LTreeEditor<T, ST> extends LCollectionEditor<T, ST> {

	private LObjectDialog<ST> editDialog = null;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LTreeEditor(Composite parent, int style) {
		super(parent, style);
		
		LTreeEditor<T, ST> self = this;
		LTree<T, ST> tree = new LTree<T, ST>(this, SWT.NONE) {
			@Override
			public LEditEvent<ST> editTreeItem(LPath path) {
				return onEditItem(path);
			}
			@Override
			public Object toObject(LPath path) {
				LDataTree<T> node = getTree().getNode(path);
				if (node == null)
					return null;
				return node.data;
			}
			@Override
			public LDataTree<T> emptyNode() {
				return new LDataTree<T>(createNewData());
			}
			@Override
			public LDataTree<T> duplicateNode(LPath nodePath) {
				return self.duplicateNode(getTree().getNode(nodePath));
			}
			@Override
			public LDataTree<T> toNode(LPath path) {
				return getTree().getNode(path);
			}
		};
		setCollection(tree);
		tree.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		tree.addInsertListener(new LCollectionListener<T>() {
			public void onInsert(LInsertEvent<T> event) {
				getTree().insert(event.parentPath, event.index, event.node);
			}
		});
		
		tree.addDeleteListener(new LCollectionListener<T>() {
			public void onDelete(LDeleteEvent<T> event) {
				getTree().delete(event.parentPath, event.index);
			}
		});
		
		tree.addMoveListener(new LCollectionListener<T>() {
			public void onMove(LMoveEvent<T> event) {
				getTree().move(event.sourceParent, event.sourceIndex, 
						event.destParent, event.destIndex);
			}
		});
		
		tree.addEditListener(new LCollectionListener<ST>() {
			public void onEdit(LEditEvent<ST> event) {
				setEditableData(event.path, event.newData);
			}
		});
	}
	
	public void setObjectDialog(LObjectDialog<ST> dialog) {
		this.editDialog = dialog;
	}
	
	public void onVisible() {
		collection.setItems(getTree().toStringNode());
		super.onVisible();
	}

	public void setObject(Object obj) {
		@SuppressWarnings("unchecked")
		LDataTree<T> db = (LDataTree<T>) obj;
		collection.setItems(db.toStringNode());
	}
	
	public LDataTree<T> duplicateNode(LDataTree<T> node) {
		LDataTree<T> copy = new LDataTree<T>(duplicateData(node.data));
		for(LDataTree<T> child : node.children) {
			LDataTree<T> childCopy = duplicateNode(child);
			childCopy.setParent(copy);
		}
		return copy;
	}
	
	public LEditEvent<ST> onEditItem(LPath path) {
		ST oldData = getEditableData(path);
		ST newData = editDialog.open(oldData);
		if (newData != null) {
			return new LEditEvent<ST>(path, oldData, newData);
		}
		return null;
	}
	
	public abstract LDataTree<T> getTree();
	
	public abstract T createNewData();
	
	public abstract T duplicateData(T original);
	
	public abstract ST getEditableData(LPath path);
	
	public abstract void setEditableData(LPath path, ST data);
	
}
