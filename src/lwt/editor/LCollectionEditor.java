package lwt.editor;

import lwt.action.LActionStack;
import lwt.dataestructure.LDataCollection;
import lwt.dataestructure.LPath;
import lwt.dialog.LObjectDialog;
import lwt.dialog.LShellFactory;
import lwt.event.LDeleteEvent;
import lwt.event.LEditEvent;
import lwt.event.LInsertEvent;
import lwt.event.LMoveEvent;
import lwt.event.listener.LCollectionListener;
import lwt.widget.LCollection;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Holds common functionalities for LTreeEditor and LListEditor.
 *
 */

public abstract class LCollectionEditor<T, ST> extends LEditor {

	public String fieldName = "";
	protected LObjectDialog<ST> editDialog = null;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LCollectionEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
	}
	
	protected void setListeners() {
		getCollectionWidget().addInsertListener(new LCollectionListener<T>() {
			public void onInsert(LInsertEvent<T> event) {
				getDataCollection().insert(event.parentPath, event.index, event.node);
			}
		});
		getCollectionWidget().addDeleteListener(new LCollectionListener<T>() {
			public void onDelete(LDeleteEvent<T> event) {
				getDataCollection().delete(event.parentPath, event.index);
			}
		});
		getCollectionWidget().addMoveListener(new LCollectionListener<T>() {
			public void onMove(LMoveEvent<T> event) {
				getDataCollection().move(event.sourceParent, event.sourceIndex, 
						event.destParent, event.destIndex);
			}
		});
		getCollectionWidget().addEditListener(new LCollectionListener<ST>() {
			public void onEdit(LEditEvent<ST> event) {
				setEditableData(event.path, event.newData);
			}
		});
	}
	
	public void setObject(Object obj) {
		@SuppressWarnings("unchecked")
		LDataCollection<T> db = (LDataCollection<T>) obj;
		setDataCollection(db);
	}
	
	public void setDataCollection(LDataCollection<T> db) {
		getCollectionWidget().setDataCollection(db);
	}
	
	public void setShellFactory(LShellFactory<ST> factory) {
		editDialog = new LObjectDialog<ST>(getShell(), getShell().getStyle());
		editDialog.setFactory(factory);
	}
	
	public LEditEvent<ST> onEditItem(LPath path) {
		ST oldData = getEditableData(path);
		ST newData = editDialog.open(oldData);
		if (newData != null) {
			return new LEditEvent<ST>(path, oldData, newData);
		}
		return null;
	}
	
	public void refreshObject(LPath path) {
		getCollectionWidget().refreshObject(path);
	}
	
	public void setActionStack(LActionStack stack) {
		super.setActionStack(stack);
		getCollectionWidget().setActionStack(stack);
	}
	
	// Widget
	public abstract LCollection<T, ST> getCollectionWidget();
	
	// Data Collection
	protected abstract LDataCollection<T> getDataCollection(); 
	
	// Editable Data
	protected abstract ST getEditableData(LPath path);
	protected abstract void setEditableData(LPath path, ST newData);
	
}
