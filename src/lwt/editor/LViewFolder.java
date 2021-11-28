package lwt.editor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;

public class LViewFolder extends LView {
	
	protected TabFolder tabFolder;
	protected int currentTab = 0;
	
	public LViewFolder(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		tabFolder = new TabFolder(this, SWT.BORDER);
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
	}

	public TabFolder getTabFolder() {
		return tabFolder;
	}
	
	public void undo() {
		children.get(currentTab).undo();
	}
	
	public void redo() {
		children.get(currentTab).redo();
	}
	
	public boolean canUndo() {
		return children.get(currentTab).canUndo();
	}

	public boolean canRedo() {
		return children.get(currentTab).canRedo();
	}

}
