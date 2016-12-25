package lwt.editor;

import lwt.widget.LControl;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public abstract class LControlView<T> extends LView {
	
	public LControlView(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
	}
	
	public abstract LControl<T> getControl();

}
