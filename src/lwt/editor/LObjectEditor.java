package lwt.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lwt.action.LActionStack;
import lwt.dataestructure.LPath;
import lwt.event.LControlEvent;
import lwt.event.LSelectionEvent;
import lwt.event.listener.LControlListener;
import lwt.event.listener.LSelectionListener;
import lwt.widget.LControl;

import org.eclipse.swt.widgets.Composite;

/**
 * A specific type of Editor that edits a single object.
 * It has a collection of different Controls to edit the
 * object's fields.
 *
 */

public class LObjectEditor extends LEditor {

	protected HashMap<LControl<?>, String> controlMap = new HashMap<>();
	protected HashMap<LEditor, String> editorMap = new HashMap<>();
	public LCollectionEditor<?, ?> collectionEditor;
	protected Object currentObject;
	protected LPath currentPath;
	protected ArrayList<LSelectionListener> selectionListeners = new ArrayList<>();
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LObjectEditor(Composite parent, int style) {
		super(parent, style);
	}
	
	public <T> void addChild(LEditor editor, String key) {
		if (key.isEmpty()) {
			addChild(editor);
		} else {
			addChild((LView) editor);
			editorMap.put(editor, key);
		}
	}
	
	public <T> void addControl(LControlView<T> view, String key) {
		addControl(view.getControl(), key);
		addChild(view);
	}

	public <T> void addControl(LControl<T> control, String key) {
		controlMap.put(control, key);
		control.setActionStack(actionStack);
		control.addModifyListener(new LControlListener<T>() {
			@Override
			public void onModify(LControlEvent<T> event) {
				if (key.isEmpty()) {
					currentObject = event.newValue;
				} else if (currentObject != null && key != null) {
					setFieldValue(currentObject, key, event.newValue);
					if (collectionEditor != null && currentPath != null && event.detail >= 0)
						collectionEditor.refreshObject(currentPath);
				}
			}
		});
	}
	
	public void setActionStack(LActionStack stack) {
		super.setActionStack(stack);
		for(LControl<?> control : controlMap.keySet()) {
			control.setActionStack(stack);
		}
	}
	
	public void setObject(Object obj) {
		try {
			if (currentObject != null)
				saveObjectValues();
			currentObject = obj;
			for(LEditor subEditor : subEditors) {
				subEditor.setObject(obj);
			}
			if (obj != null) {
				for(Map.Entry<LEditor, String> entry : editorMap.entrySet()) {
					Object value = getFieldValue(obj, entry.getValue());
					entry.getKey().setObject(value);
				}
				for(Map.Entry<LControl<?>, String> entry : controlMap.entrySet()) {
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
				for(Map.Entry<LControl<?>, String> entry : controlMap.entrySet()) {
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
	
	public Object getObject() {
		return currentObject;
	}
	
	public void setObject(Object obj, LPath path) {
		setObject(obj);
		currentPath = path;
	}
	
	public void saveObjectValues() {
		for(Map.Entry<LControl<?>, String> entry : controlMap.entrySet()) {
			LControl<?> control = entry.getKey();
			control.notifyEmpty();
		}
	}
	
	public void addSelectionListener(LSelectionListener listener) {
		selectionListeners.add(listener);
	}

}
