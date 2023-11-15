package lwt.widget;

import org.eclipse.swt.widgets.Menu;

import lwt.event.LControlEvent;

public interface LControl<T> {

	public void modify(T newValue);
	public void setValue(Object value);
	
	public void notifyListeners(LControlEvent<T> event);
	
	public void onCopyButton(Menu menu);
	public void onPasteButton(Menu menu);
	
}
