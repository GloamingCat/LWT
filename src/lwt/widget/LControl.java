package lwt.widget;

import java.util.ArrayList;

import lwt.event.LControlEvent;
import lwt.event.listener.LControlListener;

import org.eclipse.swt.widgets.Composite;

public abstract class LControl extends LWidget {

	protected ArrayList<LControlListener> modifyListeners = new ArrayList<>();
	protected Object currentValue;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LControl(Composite parent, int style) {
		super(parent, style);
	}
	
	public void notifyListeners(LControlEvent event) {
		for(LControlListener listener : modifyListeners) {
			listener.onModify(event);
		}
	}
	
	public void addModifyListener(LControlListener listener) {
		modifyListeners.add(listener);
	}
	
	public void setValue(Object value) {
		currentValue = value;
	}
	
	public Object getValue() {
		return currentValue;
	}

}
