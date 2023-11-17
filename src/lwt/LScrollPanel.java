package lwt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class LScrollPanel extends ScrolledComposite implements LContainer {
	
	/*
	 * Internal, no layout.
	 */
	LScrollPanel(Composite parent, int style) {
		super(parent, style);
	}
	
	/*
	 * Internal, with fill layout.
	 */
	LScrollPanel(Composite parent, boolean horizontal, int style) {
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
	LScrollPanel(Composite parent, int columns, boolean equalCols, int style) {
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
	
	/**
	 * Horizontal fill layout.
	 * @param parent
	 * @param large
	 */
	public LScrollPanel(LContainer parent, boolean large) {
		this(parent.getComposite(), 
				large ? SWT.V_SCROLL | SWT.H_SCROLL : SWT.NONE);
		if (!large) {
			setExpandVertical(true);
			setExpandHorizontal(true);
		}
	}
	
	/**
	 * Grid or fill layout.
	 * @param parent
	 * @param columns
	 * @param equalCols
	 * @param large
	 */
	public LScrollPanel(LContainer parent, int columns, boolean equalCols, boolean large) {
		this(parent.getComposite(), columns, equalCols, 
				large ? SWT.V_SCROLL | SWT.H_SCROLL : SWT.NONE);
		if (!large) {
			setExpandVertical(true);
			setExpandHorizontal(true);
		}
	}
	
	/**
	 * Fill layout.
	 * @param parent
	 * @param horizontal
	 * @param large
	 */
	public LScrollPanel(LContainer parent, boolean horizontal, boolean large) {
		this(parent.getComposite(), horizontal,		
				large ? SWT.V_SCROLL | SWT.H_SCROLL : SWT.NONE);
		if (!large) {
			setExpandVertical(true);
			setExpandHorizontal(true);
		}
	}
	
	@Override
	protected void checkSubclass() { }
	
	public Composite getComposite() {
		return this;
	}
	
}
