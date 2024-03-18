package lwt.container;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import lwt.LMenuInterface;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;

public class LViewFolder extends LView {
	
	public TabFolder tabFolder;
	protected int currentTab = 0;
	
	/**
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter parent new lwt.dialog.LShell()
	 */
	public LViewFolder(LContainer parent, boolean doubleBuffered) {
		super(parent, doubleBuffered);
		setFillLayout(true);
		tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setLayout(new FillLayout());
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int i = tabFolder.getSelectionIndex();
				if (children != null && currentTab != i && i >= 0 && i < children.size()) {
					currentTab = i;
					children.get(i).onVisible();
				}
			}
		});
	}
	
	public void addTab(String name, LView child) {
		addChild(child);
		TabItem tbtm = new TabItem(tabFolder, SWT.NONE);
		tbtm.setText(name);
		tbtm.setControl(child);
		//tabFolder.layout();
	}
	
	public void addTab(String name, LContainer child) {
		TabItem tbtm = new TabItem(tabFolder, SWT.NONE);
		tbtm.setText(name);
		//tbtm.setControl(child.getComposite());
		//tabFolder.layout();
	}

	public LMenuInterface getMenuInterface() {
		return children.get(currentTab).getMenuInterface();
	}
	
	public Composite getContentComposite() {
		return tabFolder;
	}

	@Override
	public Composite getTopComposite() {
		return this;
	}

}
