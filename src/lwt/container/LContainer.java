package lwt.container;

import org.eclipse.swt.widgets.Composite;

import lwt.dialog.LWindow;

public interface LContainer {

	Composite getContentComposite();
	default Composite getTopComposite() {
		return getContentComposite();
	}
	
	default Object getChild(int i) {
		return getContentComposite().getChildren()[i];
	}
	
	default int getChildCount() {
		return getContentComposite().getChildren().length;
	}
	
	default LWindow getWindow() {
		return (LWindow) getTopComposite().getShell();
	}

	default void dispose() {
		getTopComposite().dispose();
	}
	
}
