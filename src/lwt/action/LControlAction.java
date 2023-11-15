package lwt.action;

import lwt.event.LControlEvent;
import lwt.widget.LControl;

public class LControlAction<T> implements LAction {
	
	private LControl<T> control;
	private LControlEvent<T> event;
	
	public LControlAction(LControl<T> control, LControlEvent<T> event) {
		this.control = control;
		this.event = event;
	}
	
	private void apply() {
		control.setValue(event.newValue);
		control.notifyListeners(event);
	}
	
	private void swap() {
		T oldv = event.oldValue;
		T newv = event.newValue;
		event.oldValue = newv;
		event.newValue = oldv;
	}
	
	@Override
	public void undo() {
		swap();
		apply();
	}

	@Override
	public void redo() {
		swap();
		apply();
	}

}
