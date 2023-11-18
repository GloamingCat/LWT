package lwt.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import lwt.LContainer;
import lwt.widget.LWidget;

public class LFrame extends Group implements LContainer {
	
	public LFrame(LContainer parent, String name) {
		this(parent, name, true);
	}
	
	public LFrame(LContainer parent, String name, int columns) {
		this(parent, name, columns, false);
	}
	
	public LFrame(LContainer parent, String name, int columns, boolean equalColumns) {
		super(parent.getComposite(), SWT.NONE);
		setText(name);
		if (columns == 0) {
			boolean vertical = !equalColumns;
			FillLayout layout = new FillLayout(vertical ? SWT.VERTICAL : SWT.HORIZONTAL);
			layout.spacing = 5;
			layout.marginWidth = 5;
			layout.marginHeight = 5;
			setLayout(layout);
		} else {
			setLayout(new GridLayout(columns, equalColumns));
		}
	}
	
	public LFrame(LContainer parent, String name, boolean horizontal) {
		super(parent.getComposite(), SWT.NONE);
		setText(name);
		if (horizontal) {
			setLayout(new FillLayout(SWT.HORIZONTAL));
		} else {
			setLayout(new FillLayout(SWT.VERTICAL));
		}
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
