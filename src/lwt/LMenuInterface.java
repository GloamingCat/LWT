package lwt;

import org.eclipse.swt.dnd.TextTransfer;

import lwt.container.LView;

public class LMenuInterface extends lbase.LMenuInterface {

	public LMenuInterface(LView root) {
		super(root);
	}
	
	public boolean canPaste() {
		Object obj = LGlobals.clipboard.getContents(TextTransfer.getInstance());
		return super.canPaste(obj);
	}

}
