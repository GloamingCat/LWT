package lwt.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;

import lwt.dialog.LShell;

public class LPanel extends Composite implements LContainer {

	/**
	 * Internal, no layout.
	 */
	LPanel(Composite parent, int style) {
		super(parent, style);
	}
	
	/**
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
	
	/**
	 * Internal, with grid layout.
	 */
	LPanel(Composite parent, int columns, boolean equalCols, int style) {
		super(parent, style);
		GridLayout gl = new GridLayout(columns, equalCols);
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		setLayout(gl);
	}
	
	/**
	 * Internal, with fill or row layout.
	 */
	LPanel(Composite parent, boolean horizontal, boolean equalCells, int style) {
		super(parent, style);
		int dir = horizontal ? SWT.HORIZONTAL : SWT.VERTICAL;
		if (equalCells) {
			FillLayout layout = new FillLayout(dir);
			layout.spacing = 5;
			setLayout(layout);
		} else {
			RowLayout layout = new RowLayout(dir);
			layout.spacing = 5;
			setLayout(layout);
		}
	}
	
	/** Grid layout.
	 * @param parent
	 * @param columns
	 * @param equalCols
	 */
	public LPanel(LContainer parent, int columns, boolean equalCols) {
		this(parent.getComposite(), columns, equalCols, SWT.NONE);
	}
	
	/** Fill/row layout.
	 * @param parent
	 * @param horizontal
	 */
	public LPanel(LContainer parent, boolean horizontal, boolean equalCells) {
		this(parent.getComposite(), horizontal, equalCells, SWT.NONE);
	}
	
	/** Fill layout with no margin.
	 * @param parent
	 * @param horizontal
	 */
	public LPanel(LContainer parent, boolean horizontal) {
		this(parent.getComposite(), horizontal, SWT.NONE);
	}

	/** No layout.
	 * @param parent
	 */
	public LPanel(LContainer parent) {
		this(parent.getComposite(), SWT.NONE);
	}
	
	/** Grid layout.
	 * @param parent
	 */
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
