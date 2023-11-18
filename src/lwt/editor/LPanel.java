package lwt.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import lwt.LContainer;
import lwt.dialog.LShell;

public class LPanel extends Composite implements LContainer {

	/*
	 * Internal, no layout.
	 */
	LPanel(Composite parent, int style) {
		super(parent, style);
	}
	
	/*
	 * Internal, with fill layout.
	 */
	LPanel(Composite parent, boolean horizontal, int style) {
		super(parent, style);
		if (horizontal) {
			setLayout(new FillLayout(SWT.HORIZONTAL));
		} else {
			setLayout(new FillLayout(SWT.VERTICAL));
		}
	}
	
	/*
	 * Internal, with grid or fill layout.
	 */
	LPanel(Composite parent, int columns, boolean equalCols, int style) {
		super(parent, style);
		if (columns == 0) {
			boolean vertical = !equalCols;
			FillLayout layout = new FillLayout(vertical ? SWT.VERTICAL : SWT.HORIZONTAL);
			layout.spacing = 5;
			setLayout(layout);
		} else {
			GridLayout gl = new GridLayout(columns, equalCols);
			gl.marginWidth = 0;
			gl.marginHeight = 0;
			setLayout(gl);
		}
	}
	
	public LPanel(LContainer parent, int columns, boolean equalCols) {
		this(parent.getComposite(), columns, equalCols, SWT.NONE);
	}
	
	public LPanel(LContainer parent, boolean horizontal, int style) {
		this(parent.getComposite(), horizontal, style);
	}
	
	public LPanel(LContainer parent, boolean horizontal) {
		this(parent, horizontal, SWT.NONE);
	}
	
	public LPanel(LContainer parent) {
		this(parent, SWT.NONE);
	}
	
	public LPanel(LContainer parent, int columns) {
		this(parent, columns, false);
	}

	@Override
	protected void checkSubclass() { }
	
	public Composite getComposite() {
		return this;
	}
	
	public LShell getShell() {
		return (LShell) super.getShell();
	}
	
}
