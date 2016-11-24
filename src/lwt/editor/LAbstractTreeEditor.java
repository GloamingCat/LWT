package lwt.editor;

import lwt.dataestructure.LDataCollection;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LDeleteEvent;
import lwt.event.LEditEvent;
import lwt.event.LInsertEvent;
import lwt.event.LMoveEvent;
import lwt.event.LSelectionEvent;
import lwt.event.listener.LCollectionListener;
import lwt.event.listener.LSelectionListener;
import lwt.widget.LTree;

import org.eclipse.swt.widgets.Composite;

public abstract class LAbstractTreeEditor<T, ST> extends LCollectionEditor<T, ST> {
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LAbstractTreeEditor(Composite parent, int style) {
		super(parent, style);
	}
	
	protected void setListeners() {
		super.setListeners();
		getCollection().addInsertListener(new LCollectionListener<T>() {
			public void onInsert(LInsertEvent<T> event) {
				getCollection().forceSelection(event.parentPath, event.index);
			}
		});
		getCollection().addDeleteListener(new LCollectionListener<T>() {
			public void onDelete(LDeleteEvent<T> event) {
				getCollection().forceSelection(event.parentPath, event.index);
			}
		});
		getCollection().addMoveListener(new LCollectionListener<T>() {
			public void onMove(LMoveEvent<T> event) {
				getCollection().forceSelection(event.destParent, event.destIndex);
			}
		});
		getCollection().addEditListener(new LCollectionListener<ST>() {
			public void onEdit(LEditEvent<ST> event) {
				getCollection().forceSelection(event.path);
			}
		});
	}
	
	public void addChild(LObjectEditor editor) {
		getCollection().addSelectionListener(new LSelectionListener() {
			@Override
			public void onSelect(LSelectionEvent event) {
				LPath path = getCollection().getSelectedPath();
				editor.setObject(getCollection().toObject(path), path);
			}
		});
		editor.collectionEditor = this;
		addChild((LEditor) editor);
	}
	
	public abstract LTree<T, ST> getCollection();
	public abstract T createNewData();
	public abstract T duplicateData(T original);
	
	public void setObject(Object obj) {
		@SuppressWarnings("unchecked")
		LDataCollection<T> db = (LDataCollection<T>) obj;
		getCollection().setItems(db.toTree());
	}
	
	public LDataTree<T> duplicateNode(LDataTree<T> node) {
		LDataTree<T> copy = new LDataTree<T>(duplicateData(node.data));
		for(LDataTree<T> child : node.children) {
			LDataTree<T> childCopy = duplicateNode(child);
			childCopy.setParent(copy);
		}
		return copy;
	}
	
	public void onVisible() {
		onChildVisible();
		forceFirstSelection();
	}
	
	public abstract void forceFirstSelection();
	
}
