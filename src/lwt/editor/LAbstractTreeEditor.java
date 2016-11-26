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
		getCollectionWidget().addInsertListener(new LCollectionListener<T>() {
			public void onInsert(LInsertEvent<T> event) {
				getCollectionWidget().forceSelection(event.parentPath, event.index);
			}
		});
		getCollectionWidget().addDeleteListener(new LCollectionListener<T>() {
			public void onDelete(LDeleteEvent<T> event) {
				getCollectionWidget().forceSelection(event.parentPath, event.index);
			}
		});
		getCollectionWidget().addMoveListener(new LCollectionListener<T>() {
			public void onMove(LMoveEvent<T> event) {
				getCollectionWidget().forceSelection(event.destParent, event.destIndex);
			}
		});
		getCollectionWidget().addEditListener(new LCollectionListener<ST>() {
			public void onEdit(LEditEvent<ST> event) {
				getCollectionWidget().forceSelection(event.path);
			}
		});
	}
	
	public void addChild(LObjectEditor editor) {
		getCollectionWidget().addSelectionListener(new LSelectionListener() {
			@Override
			public void onSelect(LSelectionEvent event) {
				LPath path = getCollectionWidget().getSelectedPath();
				editor.setObject(getCollectionWidget().toObject(path), path);
			}
		});
		editor.collectionEditor = this;
		addChild((LEditor) editor);
	}
	
	
	
	public abstract LTree<T, ST> getCollectionWidget();
	protected abstract T createNewData();
	protected abstract T duplicateData(T original);
	
	public void setObject(Object obj) {
		@SuppressWarnings("unchecked")
		LDataCollection<T> db = (LDataCollection<T>) obj;
		setDataCollection(db);
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
	
	public void forceFirstSelection() {
		if (getDataCollection() != null) {
			LDataTree<T> tree = getDataCollection().toTree();
			getCollectionWidget().setItems(tree);
			if (tree.children.size() > 0) {
				getCollectionWidget().forceSelection(new LPath(0));
			} else {
				getCollectionWidget().forceSelection(null);
			}
		} else {
			getCollectionWidget().setItems(null);
			getCollectionWidget().forceSelection(null);
		}
	}
	
}
