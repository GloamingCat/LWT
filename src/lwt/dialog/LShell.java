package lwt.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import lwt.container.LContainer;

public class LShell extends Shell implements LContainer {

	/**
	 * Root shell with selection size.
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter width 300
	 * @wbp.eval.method.parameter height 200
	 */
	public LShell(int width, int height) {
		super(Display.getDefault(), SWT.SHELL_TRIM | SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND);
		setMinimumSize(new Point(width, height));
		setSize(new Point(width, height));
	}
	
	/**
	 * Root shell.
	 */
	public LShell() {
		super(Display.getDefault(), SWT.SHELL_TRIM | SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND);
	}
	
	
	/**
	 * @wbp.parser.constructor
	 * Sub-shell.
	 */
	public LShell(LShell parent) {
		super(parent, SWT.CLOSE | SWT.RESIZE | SWT.APPLICATION_MODAL);
	}

	@Override
	public LShell getParent() {
		return (LShell) super.getParent();
	}

	@Override
	public Composite getComposite() {
		return this;
	}
	
	@Override
	protected void checkSubclass() { }

}
