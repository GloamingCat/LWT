package lwt.widget;

import lwt.action.LAction;
import lwt.action.LActionStack;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public abstract class LWidget extends Composite {

	protected LActionStack actionStack;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LWidget(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
	}
	
	public void setActionStack(LActionStack stack) {
		this.actionStack = stack;
	}
	
	public void newAction(LAction action) {
		if (actionStack != null) {
			actionStack.newAction(action);
		}
	}

}
