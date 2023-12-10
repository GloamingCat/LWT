package lwt.container;

import org.eclipse.swt.widgets.Composite;

public interface LContainer {

	Composite getComposite();
	Object getChild(int i);
	int getChildCount();
	
}
