package lwt.editor;

import org.eclipse.swt.SWT;

import lwt.dialog.LWindow;

public class LMenuBar extends LSubMenu {

	public LMenuBar(LWindow w) {
		super(w, SWT.BAR);
		w.setMenuBar(this);
	}

}
