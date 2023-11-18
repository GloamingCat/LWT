package lwt.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class LScrollPanel extends ScrolledComposite implements LContainer {

	/**
	 * Internal, no layout.
	 */
	LScrollPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
	}
	
	/**
	 * Internal, with fill layout.
	 */
	LScrollPanel(Composite parent, boolean horizontal, int style) {
		super(parent, style);
	}

	/** Fill layout with no margin.
	 * @param parent
	 * @param horizontal
	 */
	public LScrollPanel(LContainer parent, boolean large) {
		this(parent.getComposite(), SWT.V_SCROLL | SWT.H_SCROLL);
		if (large) {
			setExpandVertical(true);
			setExpandHorizontal(true);
		}
	}

	/** No layout.
	 * @param parent
	 */
	public LScrollPanel(LContainer parent) {
		this(parent, false);
	}

	@Override
	protected void checkSubclass() { }
	
	public Composite getComposite() {
		return this;
	}
	
}
