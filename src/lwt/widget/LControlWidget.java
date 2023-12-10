package lwt.widget;

import java.util.ArrayList;

import lwt.LGlobals;
import lwt.action.LControlAction;
import lwt.container.LContainer;
import lwt.event.LControlEvent;
import lwt.event.listener.LControlListener;

import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Menu;

import com.google.gson.reflect.TypeToken;

public abstract class LControlWidget<T> extends LWidget implements LControl<T> {
	
	protected ArrayList<LControlListener<T>> modifyListeners = new ArrayList<>();
	protected T currentValue;
	
	public LControlWidget(LContainer parent) {
		super(parent);
	}
	
	public void modify(T newValue) {
		T oldValue = currentValue;
		setValue(newValue);
		newModifyAction(oldValue, newValue);
	}
	
	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		currentValue = (T) value;
	}
	
	public T getValue() {
		return currentValue;
	}
	
	public boolean isEnabled() {
		return currentValue != null;
	}
	
	public void setEnabled(boolean value) {}
	
	//-------------------------------------------------------------------------------------
	// Modify Events
	//-------------------------------------------------------------------------------------

	protected void newModifyAction(T oldValue, T newValue) {
		LControlEvent<T> event = new LControlEvent<T>(oldValue, newValue);
		newAction(new LControlAction<T>(this, event));
		notifyListeners(event);
	}
	
	public LControlEvent<T> createEvent() {
		LControlEvent<T> e = new LControlEvent<T>(null, currentValue);
		e.detail = -1;
		return e;
	}
	
	public void addModifyListener(LControlListener<T> listener) {
		modifyListeners.add(listener);
	}
	
	public void removeModifyListener(LControlListener<T> listener) {
		modifyListeners.remove(listener);
	}
	
	public void notifyListeners(LControlEvent<T> event) {
		for(LControlListener<T> listener : modifyListeners) {
			listener.onModify(event);
		}
	}
	
	public void notifyEmpty() {
		LControlEvent<T> e = createEvent();
		notifyListeners(e);
	}
	
	//-------------------------------------------------------------------------------------
	// Copy / Paste
	//-------------------------------------------------------------------------------------
	
	public void onCopyButton(Menu menu) {
		//clipboard = currentValue;
		String json = LGlobals.gson.toJson(currentValue, currentValue.getClass());
		LGlobals.clipboard.setContents(new Object[] { json },
				new Transfer[] { TextTransfer.getInstance() });
	}
	
	@SuppressWarnings("unchecked")
	public void onPasteButton(Menu menu) {
		String json = (String) LGlobals.clipboard.getContents(TextTransfer.getInstance());
		if (json == null)
			return;
		try {
			T newValue = (T) LGlobals.gson.fromJson(json, new TypeToken<T>(){}.getType());
			if (!newValue.equals(currentValue))
				modify(newValue);	
		} catch (ClassCastException e) {
			System.err.println(e.getMessage());
			return;
		}
	}
	
}
