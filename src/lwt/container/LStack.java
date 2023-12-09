package lwt.container;

import org.eclipse.swt.custom.StackLayout;

public class LStack extends LPanel {

	private StackLayout stack;
	
	public LStack(LContainer parent) {
		super(parent);
		stack = new StackLayout();
		setLayout(stack);
	}
	
	public void setTop(LContainer container) {
		stack.topControl = container.getComposite();
		layout();
	}

}
