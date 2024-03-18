package lwt.editor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lwt.LGlobals;
import lwt.LMenuInterface;
import lwt.container.LContainer;
import lwt.container.LControlView;
import lwt.container.LView;
import lwt.widget.LControlWidget;

import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import lbase.action.LControlAction;
import lbase.data.LPath;
import lbase.event.LControlEvent;
import lbase.event.LSelectionEvent;
import lbase.event.listener.LControlListener;
import lbase.event.listener.LSelectionListener;
import lbase.gui.LMenu;

/**
 * A specific type of Editor that edits a single object.
 * It has a collection of different Controls to edit the
 * object's fields.
 *
 */
public abstract class LObjectEditor<T> extends LEditor implements lbase.gui.LControl<T> {

	protected HashMap<LControlWidget<?>, String> controlMap = new HashMap<>();
	protected HashMap<LEditor, String> editorMap = new HashMap<>();
	public LCollectionEditor<?, ?> collectionEditor;
	protected T currentObject;
	protected LPath currentPath;
	protected ArrayList<LSelectionListener> selectionListeners = new ArrayList<>();
	protected ArrayList<LControlListener<T>> modifyListeners = new ArrayList<>();
	
	//////////////////////////////////////////////////
	// {{ Constructors
	
	/**
	 * No layout.
	 * @param parent
	 * @param doubleBuffered
	 */
	public LObjectEditor(LContainer parent, boolean doubleBuffered) {
		super(parent, doubleBuffered);
		addMenu();
	}

	// }}

	//////////////////////////////////////////////////
	// {{ Children
	
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
		LObjectEditor<T> self = this;
		control.setMenuInterface(getMenuInterface());
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
					self.notifyListeners(new LControlEvent<T>(currentObject, currentObject));
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
	
	// }}
	
	public void refresh() {}
	
	public void setMenuInterface(LMenuInterface mi) {
		super.setMenuInterface(mi);
		for(LControlWidget<?> control : controlMap.keySet()) {
			control.setMenuInterface(mi);
		}
	}
	
	//////////////////////////////////////////////////
	// {{ Object
	
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
		if (getObject() == null)
			return;
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

	public void setValue(Object value) {
		T oldValue = currentObject;
		setObject(value);
		currentObject = oldValue;
		saveObjectValues();
		currentObject = null;
		setObject(oldValue);
	}
	
	protected Object getFieldValue(Object object, String name) {
		try {
			Field field = object.getClass().getField(name);
			return field.get(object);
		} catch (NoSuchFieldException e) {
			System.out.println(name + " not found in " + object.getClass().toString());
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
	
	// }}
	
	//////////////////////////////////////////////////
	// {{ Events
	
	protected void newModifyAction(T oldValue, T newValue) {
		LControlEvent<T> event = new LControlEvent<T>(oldValue, newValue);
		if (getActionStack() != null) {
			getActionStack().newAction(new LControlAction<T>(this, event));
		}
		notifyListeners(event);
	}
	
	public void notifyListeners(LControlEvent<T> event) {
		for(LControlListener<T> listener : modifyListeners) {
			listener.onModify(event);
		}
	}
	
	public void addModifyListener(LControlListener<T> listener) {
		modifyListeners.add(listener);
	}

	public void modify(T newValue) {
		T oldValue = duplicateData(currentObject);
		setValue(newValue);
		newModifyAction(oldValue, newValue);
	}
	
	// }}
	
	//////////////////////////////////////////////////
	// {{ Clipboard
	
	public void onCopyButton(LMenu menu) {
		LGlobals.clipboard.setContents(new Object[] { encodeData(currentObject) },
				new Transfer[] { TextTransfer.getInstance() });
	}
	
	public void onPasteButton(LMenu menu) {
		String str = (String) LGlobals.clipboard.getContents(TextTransfer.getInstance());
		if (str == null)
			return;
		try {
			T newValue = decodeData(str);
			if (newValue != null && !newValue.equals(currentObject))
				modify(newValue);	
		} catch (ClassCastException e) {
			System.err.println(e.getMessage());
			return;
		}
	}
	
	public abstract T duplicateData(T obj);
	public abstract String encodeData(T obj);
	public abstract T decodeData(String str);
	
	// }}

}
