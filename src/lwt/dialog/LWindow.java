package lwt.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import lwt.container.LContainer;

public class LWindow extends Shell implements LContainer, lbase.gui.LWindow {

	/**
	 * Root shell with selection size.
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter width 300
	 * @wbp.eval.method.parameter height 200
	 */
	public LWindow(int width, int height) {
		this();
		setMinimumSize(width, height);
		setSize(width, height);
	}
	
	/**
	 * Root shell.
	 */
	public LWindow() {
		super(Display.getDefault(), SWT.SHELL_TRIM | SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND);
		setLayout(new FillLayout());
	}
	
	/**
	 * @wbp.parser.constructor
	 * Sub-shell.
	 */
	public LWindow(LWindow parent) {
		super(parent, SWT.CLOSE | SWT.RESIZE | SWT.APPLICATION_MODAL);
		setLayout(new FillLayout());
	}
	
	public void setMinimumSize(int width, int height) {
		super.setMinimumSize(width, height);
	}

	@Override
	public LWindow getParent() {
		return (LWindow) super.getParent();
	}

	@Override
	public void pack() {
		super.pack();
	}
	
	public void setTitle(String title) {
		setText(title);
	}

	@Override
	public Composite getContentComposite() {
		return this;
	}

	@Override
	protected void checkSubclass() { }

}
