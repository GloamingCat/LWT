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

	protected HashMap<String, LControl<?>> controlMap = new HashMap<>();
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
	
	public <T> void addControl(LControlView<T> view, String key) {
		addControl(view.getControl(), key);
		addChild(view);
	}

	public <T> void addControl(LControl<T> control, String key) {
		controlMap.put(key, control);
		control.setActionStack(actionStack);
		control.addModifyListener(new LControlListener<T>() {
			@Override
			public void onModify(LControlEvent<T> event) {
				if (currentObject != null) {
					if (key != null && !key.isEmpty()) {
						setFieldValue(currentObject, key, event.newValue);
						if (collectionEditor != null && currentPath != null && event.detail >= 0)
							collectionEditor.refreshObject(currentPath);
					} else {
						setObject(event.newValue);
					}
				}
			}
		});
	}
	
	public void setActionStack(LActionStack stack) {
		super.setActionStack(stack);
		for(LControl<?> control : controlMap.values()) {
			control.setActionStack(stack);
		}
	}
	
	public void setObject(Object obj) {
		if (currentObject != null)
			saveObjectValues();
		currentObject = obj;
		if (obj != null) {
			for(Map.Entry<String, LControl<?>> entry : controlMap.entrySet()) {
				if (entry.getKey().isEmpty()) {
					entry.getValue().setValue(obj);
				} else {
					Object value = getFieldValue(obj, entry.getKey());
					entry.getValue().setValue(value);
				}
			}
		} else {
			for(Map.Entry<String, LControl<?>> entry : controlMap.entrySet()) {
				entry.getValue().setValue(null);
			}
		}
		for(LEditor subEditor : subEditors) {
			subEditor.setObject(obj);
		}
		for(LSelectionListener listener : selectionListeners) {
			listener.onSelect(new LSelectionEvent(currentPath, obj));
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
		for(Map.Entry<String, LControl<?>> entry : controlMap.entrySet()) {
			LControl<?> control = entry.getValue();
			control.notifyEmpty();
		}
	}
	
	public void addSelectionListener(LSelectionListener listener) {
		selectionListeners.add(listener);
	}

}
