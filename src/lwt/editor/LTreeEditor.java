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

public abstract class LTreeEditor<T> extends LCollectionEditor {

	private LObjectDialog<T> editDialog = null;
	private LTree tree;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LTreeEditor(Composite parent, int style) {
		super(parent, style);
		
		LTreeEditor<T> self = this;
		tree = new LTree(this, SWT.NONE) {
			@Override
			public LEditEvent editTreeItem(LPath path) {
				LDataTree<T> node = self.getTree().getNode(path);
				T newData = editDialog.open(node.data);
				if (newData != null) {
					return new LEditEvent(path, node.data, newData);
				}
				return null;
			}
		};
		setCollection(tree);
		tree.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		tree.addInsertListener(new LCollectionListener() {
			public void onInsert(LInsertEvent event) {
				
			}
		});
		
		tree.addDeleteListener(new LCollectionListener() {
			public void onDelete(LDeleteEvent event) {
				
			}
		});
		
		tree.addMoveListener(new LCollectionListener() {
			public void onMove(LMoveEvent event) {
				
			}
		});
		
		tree.addEditListener(new LCollectionListener() {
			public void onEdit(LEditEvent event) {
				
			}
		});
	}
	
	public void setObjectDialog(LObjectDialog<T> dialog) {
		this.editDialog = dialog;
	}
	
	public void onVisible() {
		setTree(getTree());
		super.onVisible();
	}
	
	public void setTree(LDataTree<T> node) {
		collection.clear();
		for(LDataTree<?> child : node.children) {
			LDataTree<String> stringNode = child.toStringNode();
			collection.createTreeItem(null, -1, stringNode);
		}
	}
	
	public abstract LDataTree<T> getTree();

	public void setObject(Object obj) {
		@SuppressWarnings("unchecked")
		LDataTree<T> db = (LDataTree<T>) obj;
		setTree(db);
	}
	
}
