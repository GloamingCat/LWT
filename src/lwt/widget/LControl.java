package lwt.widget;

import java.util.ArrayList;

import lwt.action.LControlAction;
import lwt.event.LControlEvent;
import lwt.event.listener.LControlListener;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

public abstract class LControl<T> extends LWidget {

	public static Object clipboard = null;
	
	protected ArrayList<LControlListener<T>> modifyListeners = new ArrayList<>();
	protected T currentValue;
	
	public LControl(Composite parent, int style) {
		super(parent, style);
	}
	
	public void modify(T newValue) {
		setValue(newValue);
		newModifyAction(currentValue, newValue);
	}
	
	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		currentValue = (T) value;
	}
	
	public T getValue() {
		return currentValue;
	}
	
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
		clipboard = currentValue;
	}
	
	public void onPasteButton(Menu menu) {
		setValue(clipboard);
	}
	
}
