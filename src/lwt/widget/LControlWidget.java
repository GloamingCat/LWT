package lwt.widget;

import java.util.ArrayList;
import java.util.IllegalFormatConversionException;

import lwt.action.LControlAction;
import lwt.event.LControlEvent;
import lwt.event.listener.LControlListener;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

public abstract class LControlWidget<T> extends LWidget implements LControl<T> {
	
	public static Object clipboard = null;
	protected ArrayList<LControlListener<T>> modifyListeners = new ArrayList<>();
	protected T currentValue;
	
	public LControlWidget(Composite parent, int style) {
		super(parent, style);
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
		clipboard = currentValue;
	}
	
	@SuppressWarnings("unchecked")
	public void onPasteButton(Menu menu) {
		try {
			if (clipboard != null && !clipboard.equals(currentValue))
				modify((T) clipboard);	
		} catch (IllegalFormatConversionException e) {}
	}
	
}
