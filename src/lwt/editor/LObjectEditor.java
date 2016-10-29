package lwt.editor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import lwt.widget.LControl;

import org.eclipse.swt.widgets.Composite;

/**
 * A specific type of Editor that edits a single object.
 * It has a collection of different Controls to edit the
 * object's fields.
 * 
 * @author Luisa
 *
 */

public class LObjectEditor extends LEditor {

	protected HashMap<String, LControl> controlMap = new HashMap<>();
	public LCollectionEditor collectionEditor;
	protected Object currentObject;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LObjectEditor(Composite parent, int style) {
		super(parent, style);
	}

	protected void addControl(String key, LControl control) {
		controlMap.put(key, control);
		control.setActionStack(actionStack);
	}
	
	public void setObject(Object obj) {
		for(Map.Entry<String, LControl> entry : controlMap.entrySet()) {
			Object value = getFieldValue(obj, entry.getKey());
			entry.getValue().setValue(value);
		}
		for(LObjectEditor subEditor : objectEditors) {
			subEditor.setObject(obj);
		}
		for(LCollectionEditor subEditor : collectionEditors) {
			subEditor.setObject(obj);
		}
	}
	
	protected Object getFieldValue(Object object, String name) {
		try {
			Field field = object.getClass().getField(name);
			return field.get(object);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected void setFieldValue(Object object, String name, Object value) {
		try {
			Field field = object.getClass().getField(name);
			field.set(object, value);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}


}