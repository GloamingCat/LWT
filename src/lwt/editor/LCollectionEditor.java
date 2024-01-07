package lwt.editor;

import lwt.LGlobals;
import lwt.LMenuInterface;
import lwt.container.LContainer;
import lwt.dataestructure.LDataCollection;
import lwt.dataestructure.LPath;
import lwt.dialog.LShellFactory;
import lwt.event.LDeleteEvent;
import lwt.event.LEditEvent;
import lwt.event.LInsertEvent;
import lwt.event.LMoveEvent;
import lwt.event.listener.LCollectionListener;
import lwt.widget.LCollection;

import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Menu;

/**
 * Edits the items in a list.
 *
 */

public abstract class LCollectionEditor<T, ST> extends LObjectEditor<LDataCollection<T>> {

	public String fieldName = "";
	protected LShellFactory<ST> shellFactory;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LCollectionEditor(LContainer parent) {
		super(parent, false);
		setLayout(new FillLayout());
	}
	
	protected void setListeners() {
		getCollectionWidget().addInsertListener(new LCollectionListener<T>() {
			public void onInsert(LInsertEvent<T> event) {
				getObject().insert(event.parentPath, event.index, event.node);
			}
		});
		getCollectionWidget().addDeleteListener(new LCollectionListener<T>() {
			public void onDelete(LDeleteEvent<T> event) {
				getObject().delete(event.parentPath, event.index);
			}
		});
		getCollectionWidget().addMoveListener(new LCollectionListener<T>() {
			public void onMove(LMoveEvent<T> event) {
				getObject().move(event.sourceParent, event.sourceIndex, 
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
		super.setObject(obj);
		@SuppressWarnings("unchecked")
		LDataCollection<T> db = (LDataCollection<T>) obj;
		getCollectionWidget().setDataCollection(db);
	}
	
	public void setShellFactory(LShellFactory<ST> factory) {
		shellFactory = factory;
	}
	
	public LEditEvent<ST> onEditItem(LPath path) {
		if (shellFactory == null)
			return null;
		ST oldData = getEditableData(path);
		ST newData = shellFactory.openShell(getShell(), oldData);
		if (newData != null) {
			return new LEditEvent<ST>(path, oldData, newData);
		}
		return null;
	}
	
	public void refreshObject(LPath path) {
		getCollectionWidget().refreshObject(path);
	}

	@Override
	public void setMenuInterface(LMenuInterface mi) {
		super.setMenuInterface(mi);
		getCollectionWidget().setMenuInterface(mi);
	}

	@Override
	public void restart() {
		getCollectionWidget().setDataCollection(getObject());
	}

	@Override
	public void saveObjectValues() {
		LDataCollection<T> obj = getObject();
		if (obj != null)
			obj.set(getCollectionWidget().getDataCollection());
	}
	
	@Override
	public void onCopyButton(Menu menu) {
		LGlobals.clipboard.setContents(new Object[] { encodeData(getObject()) },
				new Transfer[] { TextTransfer.getInstance() });
	}
	
	@Override
	public void onPasteButton(Menu menu) {
		String str = (String) LGlobals.clipboard.getContents(TextTransfer.getInstance());
		if (str == null)
			return;
		try {
			LDataCollection<T> newValue = decodeData(str);
			LDataCollection<T> oldValue = getObject();
			if (newValue != null && !newValue.equals(oldValue)) {
				oldValue = oldValue.clone();
				getCollectionWidget().setDataCollection(newValue);
				newModifyAction(oldValue, newValue);
			}
		} catch (ClassCastException e) {
			System.err.println(e.getMessage());
			return;
		}
	}
	
	// Widget
	public abstract LCollection<T, ST> getCollectionWidget();

	// Editable Data
	protected abstract ST getEditableData(LPath path);
	protected abstract void setEditableData(LPath path, ST newData);
	
}
