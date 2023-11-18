package lwt.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lwt.LVocab;
import lwt.action.LActionStack;
import lwt.action.LControlAction;
import lwt.container.LContainer;
import lwt.container.LControlView;
import lwt.container.LView;
import lwt.dataestructure.LPath;
import lwt.event.LControlEvent;
import lwt.event.LSelectionEvent;
import lwt.event.listener.LControlListener;
import lwt.event.listener.LSelectionListener;
import lwt.widget.LControl;
import lwt.widget.LControlWidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

/**
 * A specific type of Editor that edits a single object.
 * It has a collection of different Controls to edit the
 * object's fields.
 *
 */

public abstract class LObjectEditor<T> extends LEditor implements LControl<T> {

	protected HashMap<LControlWidget<?>, String> controlMap = new HashMap<>();
	protected HashMap<LEditor, String> editorMap = new HashMap<>();
	public LCollectionEditor<?, ?> collectionEditor;
	protected T currentObject;
	protected LPath currentPath;
	protected ArrayList<LSelectionListener> selectionListeners = new ArrayList<>();
	protected ArrayList<LControlListener<T>> modifyListeners = new ArrayList<>();
	
	/**
	 * No layout.
	 * @param parent
	 * @param doubleBuffered
	 */
	public LObjectEditor(LContainer parent, boolean doubleBuffered) {
		super(parent, doubleBuffered);
	}

	/**
	 * Fill/row layout.
	 * @param parent
	 * @param horizontal
	 * @param equalCells
	 * @param doubleBuffered
	 */
	public LObjectEditor(LContainer parent, boolean horizontal, boolean equalCells, boolean doubleBuffered) {
		super(parent, horizontal, equalCells, doubleBuffered);
	}
	
	/**
	 * Fill layout with no margin.
	 * @param parent
	 * @param horizontal
	 * @param doubleBuffered
	 */
	public LObjectEditor(LContainer parent, boolean horizontal, boolean doubleBuffered) {
		super(parent, horizontal, doubleBuffered);
	}
	
	/**
	 * Grid layout.
	 * @param parent
	 * @param columns
	 * @param equalCols
	 * @param doubleBuffered
	 */
	public LObjectEditor(LContainer parent, int columns, boolean equalCols, boolean doubleBuffered) {
		super(parent, columns, equalCols, doubleBuffered);
	}

	public Composite addHeader() {
		Composite header = new Composite(this, 0); 
		header.setLayout(new RowLayout());
		Button copyButton = new Button(header, SWT.NONE);
		copyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				onCopyButton(null);
			}
		});
		copyButton.setText(LVocab.instance.COPY);
		Button pasteButton = new Button(header, SWT.NONE);
		pasteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				onPasteButton(null);
			}
		});
		pasteButton.setText(LVocab.instance.PASTE);
		return header;
	}
	
	public <CT> void addChild(LEditor editor, String key) {
		if (key.isEmpty()) {
			addChild(editor);
		} else {
			addChild((LView) editor);
			editorMap.put(editor, key);
		}
	}
	
	public <CT> void addControl(LControlView<CT> view, String key) {
		addControl(view.getControl(), key);
		addChild(view);
	}

	public <CT> void addControl(LControlWidget<CT> control, String key) {
		controlMap.put(control, key);
		control.setActionStack(actionStack);
		control.addModifyListener(new LControlListener<CT>() {
			@SuppressWarnings("unchecked")
			@Override
			public void onModify(LControlEvent<CT> event) {
				if (!controlMap.containsKey(control)) {
					control.removeModifyListener(this);
					return;
				}
				if (key.isEmpty()) {
					currentObject = (T) event.newValue;
				} else if (currentObject != null && key != null) {
					setFieldValue(currentObject, key, event.newValue);
					if (collectionEditor != null && currentPath != null && event.detail >= 0)
						collectionEditor.refreshObject(currentPath);
				}
				refresh();
			}
		});
	}
	
	public <CT> void removeControl(LControlView<CT> view) {
		removeControl(view.getControl());
		removeChild(view);
	}
	
	public <CT> void removeControl(LControlWidget<CT> control) {
		controlMap.remove(control);
	}
	
	public void refresh() {}
	
	public void setActionStack(LActionStack stack) {
		super.setActionStack(stack);
		for(LControlWidget<?> control : controlMap.keySet()) {
			control.setActionStack(stack);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setObject(Object obj) {
		try {
			if (currentObject != null)
				saveObjectValues();
			currentObject = (T) obj;
			for(LEditor subEditor : subEditors) {
				subEditor.setObject(obj);
			}
			if (obj != null) {
				for(Map.Entry<LEditor, String> entry : editorMap.entrySet()) {
					Object value = getFieldValue(obj, entry.getValue());
					entry.getKey().setObject(value);
				}
				for(Map.Entry<LControlWidget<?>, String> entry : controlMap.entrySet()) {
					if (entry.getValue().isEmpty()) {
						entry.getKey().setValue(obj);
					} else {
						Object value = getFieldValue(obj, entry.getValue());
						try {
							entry.getKey().setValue(value);
						} catch (Exception e) {
							System.err.println(this.getClass() + ": " + entry.getValue());
							throw e;
						}
					}
				}
			} else {
				for(Map.Entry<LEditor, String> entry : editorMap.entrySet()) {
					entry.getKey().setObject(null);
				}
				for(Map.Entry<LControlWidget<?>, String> entry : controlMap.entrySet()) {
					entry.getKey().setValue(null);
				}
			}
			for(LSelectionListener listener : selectionListeners) {
				listener.onSelect(new LSelectionEvent(currentPath, obj, -1));
			}
		} catch (Exception e) {
			System.err.println(this.getClass());
			throw e;
		}
	}
	
	public T getObject() {
		return currentObject;
	}
	
	public void setObject(Object obj, LPath path) {
		setObject(obj);
		currentPath = path;
	}
	
	public void saveObjectValues() {
		for(Map.Entry<LControlWidget<?>, String> entry : controlMap.entrySet()) {
			LControlWidget<?> control = entry.getKey();
			control.notifyEmpty();
		}
		for(Map.Entry<LEditor, String> entry : editorMap.entrySet()) {
			LEditor editor = entry.getKey();
			editor.saveObjectValues();
		}
	}
	
	public void addSelectionListener(LSelectionListener listener) {
		selectionListeners.add(listener);
	}
	
	public void addModifyListener(LControlListener<T> listener) {
		modifyListeners.add(listener);
	}

	public void modify(T newValue) {
		T oldValue = duplicateData(currentObject);
		setValue(newValue);
		newModifyAction(oldValue, newValue);
	}
	
	public void setValue(Object value) {
		T oldValue = currentObject;
		setObject(value);
		currentObject = oldValue;
		saveObjectValues();
		currentObject = null;
		setObject(oldValue);
	}
	
	//-------------------------------------------------------------------------------------
	// Modify Events
	//-------------------------------------------------------------------------------------
	
	protected void newModifyAction(T oldValue, T newValue) {
		LControlEvent<T> event = new LControlEvent<T>(oldValue, newValue);
		if (actionStack != null) {
			actionStack.newAction(new LControlAction<T>(this, event));
		}
		notifyListeners(event);
	}
	
	public void notifyListeners(LControlEvent<T> event) {
		for(LControlListener<T> listener : modifyListeners) {
			listener.onModify(event);
		}
	}
	
	//-------------------------------------------------------------------------------------
	// Copy / Paste
	//-------------------------------------------------------------------------------------
	
	public void onCopyButton(Menu menu) {
		LControlWidget.clipboard = duplicateData(currentObject);
	}
	
	@SuppressWarnings("unchecked")
	public void onPasteButton(Menu menu) {
		T obj = null;
		try {
			if (LControlWidget.clipboard != null && !LControlWidget.clipboard.equals(currentObject))
				obj = (T) LControlWidget.clipboard;	
			else
				return;
		} catch (ClassCastException e) {
			System.err.println(e.getMessage());
			return;
		}
		modify(obj);
	}
	
	public abstract T duplicateData(Object obj);

}
