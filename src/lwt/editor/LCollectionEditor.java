package lwt.editor;

import java.util.ArrayList;

import lwt.action.LActionStack;
import lwt.dataestructure.LDataCollection;
import lwt.dataestructure.LPath;
import lwt.dialog.LObjectDialog;
import lwt.event.LDeleteEvent;
import lwt.event.LEditEvent;
import lwt.event.LInsertEvent;
import lwt.event.LMoveEvent;
import lwt.event.LSelectionEvent;
import lwt.event.listener.LCollectionListener;
import lwt.event.listener.LSelectionListener;
import lwt.widget.LMenuCollection;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Holds common functionalities for LTreeEditor and LListEditor.
 * 
 * @author Luisa
 *
 */

public abstract class LCollectionEditor<T, ST> extends LEditor {
	
	public LMenuCollection<T, ST> collection;
	public String fieldName = "";
	private LObjectDialog<ST> editDialog = null;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LCollectionEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
	}
	
	protected void setEditListeners() {
		collection.addInsertListener(new LCollectionListener<T>() {
			public void onInsert(LInsertEvent<T> event) {
				getDataCollection().insert(event.parentPath, event.index, event.node);
			}
		});
		
		collection.addDeleteListener(new LCollectionListener<T>() {
			public void onDelete(LDeleteEvent<T> event) {
				getDataCollection().delete(event.parentPath, event.index);
			}
		});
		
		collection.addMoveListener(new LCollectionListener<T>() {
			public void onMove(LMoveEvent<T> event) {
				getDataCollection().move(event.sourceParent, event.sourceIndex, 
						event.destParent, event.destIndex);
			}
		});
		
		collection.addEditListener(new LCollectionListener<ST>() {
			public void onEdit(LEditEvent<ST> event) {
				setEditableData(event.path, event.newData);
			}
		});
		
	}
	
	public void setObjectDialog(LObjectDialog<ST> dialog) {
		this.editDialog = dialog;
	}
	
	public LEditEvent<ST> onEditItem(LPath path) {
		ST oldData = getEditableData(path);
		ST newData = editDialog.open(oldData);
		if (newData != null) {
			return new LEditEvent<ST>(path, oldData, newData);
		}
		return null;
	}
	
	protected abstract LDataCollection<T> getDataCollection(); 
	protected abstract ST getEditableData(LPath path);
	protected abstract void setEditableData(LPath path, ST newData);
	
	public void setEditEnabled(boolean value) {
		collection.setEditEnabled(value);
	}
	
	public void setInsertNewEnabled(boolean value) {
		collection.setInsertNewEnabled(value);
	}
	
	public void setDuplicateEnabled(boolean value) {
		collection.setDuplicateEnabled(value);
	}
	
	public void setDeleteEnabled(boolean value) {
		collection.setDeleteEnabled(value);
	}
	
	public void setDragEnabled(boolean value) {
		collection.setDragEnabled(value);
	}
	
	public void addChild(LObjectEditor editor) {
		collection.addSelectionListener(new LSelectionListener() {
			@Override
			public void onSelect(LSelectionEvent e) {
				LPath path = collection.getSelectedPath();
				editor.setObject(collection.toObject(path), path);
			}
		});
		editor.collectionEditor = this;
		addChild((LEditor) editor);
	}

	public void setCollection(LMenuCollection<T, ST> collection) {
		this.collection = collection;
		collection.setActionStack(actionStack);
	}
	
	public void renameItem(LPath path) {
		collection.renameItem(path);
	}
	
	@Override
	public void onVisible() {
		collection.refresh();
	}

	@Override
	public LState getState() {
		final LPath currentPath = collection.getSelectedPath();
		final ArrayList<LState> states = getChildrenStates();
		return new LState() {
			@Override
			public void reset() {
				LSelectionEvent e = collection.select(currentPath);
				collection.notifySelectionListeners(e);
				resetStates(states);
			}
		};
	}
	
	public void setActionStack(LActionStack stack) {
		super.setActionStack(stack);
		collection.setActionStack(stack);
	}
	
}
