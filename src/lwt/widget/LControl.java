package lwt.widget;

import java.util.ArrayList;

import lwt.action.LControlAction;
import lwt.event.LControlEvent;
import lwt.event.listener.LControlListener;

import org.eclipse.swt.widgets.Composite;

public abstract class LControl<T> extends LWidget {

	protected ArrayList<LControlListener<T>> modifyListeners = new ArrayList<>();
	protected T currentValue;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LControl(Composite parent, int style) {
		super(parent, style);
	}
	
	public void modify(T newValue) {
		setValue(newValue);
		newModifyAction(currentValue, newValue);
	}
	
	protected void newModifyAction(T oldValue, T newValue) {
		LControlEvent<T> event = new LControlEvent<T>(oldValue, newValue);
		newAction(new LControlAction<T>(this, event));
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
	
	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		currentValue = (T) value;
	}
	
	public void notifyEmpty() {
		LControlEvent<T> e = createEvent();
		notifyListeners(e);
	}
	
	public LControlEvent<T> createEvent() {
		LControlEvent<T> e = new LControlEvent<T>(null, currentValue);
		e.detail = -1;
		return e;
	}
	
	public T getValue() {
		return currentValue;
	}

}
