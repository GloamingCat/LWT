package lwt.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import lwt.widget.LWidget;

public class LFrame extends Group implements LContainer {
	

	/**
	 * Internal, no layout.
	 */
	LFrame(Composite parent, int style) {
		super(parent, style);
	}
	
	/**
	 * Internal, with fill layout.
	 */
	LFrame(Composite parent, boolean horizontal, int style) {
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
	LFrame(Composite parent, int columns, boolean equalCols, int style) {
		super(parent, style);
		setLayout(new GridLayout(columns, equalCols));
	}
	
	/**
	 * Internal, with fill or row layout.
	 */
	LFrame(Composite parent, boolean horizontal, boolean equalCells, int style) {
		super(parent, style);
		int dir = horizontal ? SWT.HORIZONTAL : SWT.VERTICAL;
		if (equalCells) {
			FillLayout layout = new FillLayout(dir);
			layout.spacing = 5;
			layout.marginWidth = 5;
			layout.marginHeight = 5;
			setLayout(layout);
		} else {
			RowLayout layout = new RowLayout(dir);
			layout.spacing = 5;
			layout.marginWidth = 5;
			layout.marginHeight = 5;
			setLayout(layout);
		}
	}
	
	/** Grid layout.
	 * @param parent
	 * @param columns
	 * @param equalCols
	 */
	public LFrame(LContainer parent, String title, int columns, boolean equalCols) {
		this(parent.getComposite(), columns, equalCols, SWT.NONE);
		setText(title);
	}
	
	/** Fill/row layout.
	 * @param parent
	 * @param horizontal
	 */
	public LFrame(LContainer parent, String title, boolean horizontal, boolean equalCells) {
		this(parent.getComposite(), horizontal, equalCells, SWT.NONE);
		setText(title);
	}
	
	/** Fill layout with no margin.
	 * @param parent
	 * @param horizontal
	 */
	public LFrame(LContainer parent, String title, boolean horizontal) {
		this(parent.getComposite(), horizontal, SWT.NONE);
		setText(title);
	}
	
	/** No layout.
	 * @param parent
	 */
	public LFrame(LContainer parent, String title) {
		this(parent.getComposite(), SWT.NONE);
		setText(title);
	}
	
	/** Grid layout.
	 * @param parent
	 */
	public LFrame(LContainer parent, String title, int columns) {
		this(parent, title, columns, false);
	}
	
	public void addWidget(LWidget widget, boolean hspace, boolean vspace, int cols, int rows) {
		GridData gd = new GridData(SWT.FILL, SWT.FILL, hspace, vspace, cols, rows);
		widget.setLayoutData(gd);
	}
	
	@Override
	protected void checkSubclass() { }
	
	public Composite getComposite() {
		return this;
	}
	
}
