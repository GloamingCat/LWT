package lwt.editor;

import lwt.LContainer;
import lwt.widget.LControlWidget;

public abstract class LControlView<T> extends LView {
	
	public LControlView(LContainer parent) {
		super(parent, true, false);
	}
	
	public abstract LControlWidget<T> getControl();

}
