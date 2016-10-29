package lwt.action;

import lwt.event.LControlEvent;
import lwt.widget.LControl;

public class LControlAction implements LAction {
	
	private LControl control;
	private LControlEvent event;
	
	public LControlAction(LControl control, LControlEvent event) {
		this.control = control;
		this.event = event;
	}
	
	private void apply() {
		control.setValue(event.newValue);
		control.notifyListeners(event);
	}
	
	private void swap() {
		Object oldv = event.oldValue;
		Object newv = event.newValue;
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
