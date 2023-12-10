package lwt.dialog;

import org.eclipse.swt.SWT;
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
		setMinimumSize(width, height);
		setSize(width, height);
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
	
	public void setMinimumSize(int width, int height) {
		super.setMinimumSize(width, height);
	}

	@Override
	public LShell getParent() {
		return (LShell) super.getParent();
	}
	
	@Override
	public void pack() {
		super.pack();
	}
	
	public void setTitle(String title) {
		setText(title);
	}

	@Override
	public Composite getComposite() {
		return this;
	}
	
	@Override
	public Object getChild(int i) {
		return getChildren()[i];
	}
	
	@Override
	public int getChildCount() {
		return this.getChildren().length;
	}

	@Override
	protected void checkSubclass() { }

}
