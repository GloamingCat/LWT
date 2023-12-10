package lwt.widget;

import lwt.event.LControlEvent;

public interface LControl<T> {

	public void modify(T newValue);
	public void setValue(Object value);
	
	public void notifyListeners(LControlEvent<T> event);

}
