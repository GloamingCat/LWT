package lwt.container;

import lwt.widget.LControlWidget;

public abstract class LControlView<T> extends LView {
	
	public LControlView(LContainer parent) {
		super(parent, false);
		setFillLayout(true);
	}
	
	public abstract LControlWidget<T> getControl();

}
