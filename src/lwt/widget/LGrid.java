package lwt.widget;

import lwt.LVocab;
import lwt.action.LActionStack;
import lwt.action.collection.LDeleteAction;
import lwt.action.collection.LEditAction;
import lwt.dataestructure.LDataList;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LDeleteEvent;
import lwt.event.LEditEvent;
import lwt.event.LInsertEvent;
import lwt.event.LMoveEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.wb.swt.SWTResourceManager;

public abstract class LGrid<T, ST> extends LCollection<T, ST> {

	protected LActionStack actionStack;
	protected Composite gridComposite;
	
	protected Menu menu;
	protected MenuItem mntmEdit;
	protected MenuItem mntmDelete;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LGrid(Composite parent, int style) {
		super(parent, style);
		gridComposite = new Composite(this, SWT.NONE);
		gridComposite.setLayout(new RowLayout());
	}

	private int indexOf(Label label) {
		int i = 0;
		for(Control c : gridComposite.getChildren()) {
			if (c == label)
				return i;
			else
				i++;
		}
		return -1;
	}
	
	public void setList(LDataList<T> list) {
		gridComposite.dispose();
		gridComposite = new Composite(this, SWT.NONE);
		gridComposite.setLayout(new RowLayout());
		int i = 0;
		for(T data : list) {
			addLabel(i++, data);
		}
	}
	
	private void addLabel(int i, T data) {
		Label label = new Label(gridComposite, SWT.NONE);
		Menu menu = new Menu(label);
		label.setMenu(menu);
		label.setData(data);
		
		MenuItem edit = new MenuItem(menu, SWT.NONE);
		edit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				onEditButton(label);
			}
		});
		edit.setText(LVocab.instance.EDIT);
		
		MenuItem delete = new MenuItem(menu, SWT.NONE);
		delete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				onDeleteButton(label);
			}
		});
		delete.setText(LVocab.instance.DELETE);
		
		String path = getImagePath(i);
		label.setImage(SWTResourceManager.getImage(path));
	}
	
	protected abstract String getImagePath(int i);

	//-------------------------------------------------------------------------------------
	// Button Handler
	//-------------------------------------------------------------------------------------
	
	protected void onEditButton(Label label) {
		int i = indexOf(label);
		LPath path = new LPath(i);
		LEditEvent<ST> e = edit(path);
		if (e != null) {
			if (actionStack != null) {
				LEditAction<ST> action = new LEditAction<ST>(this, path, e.oldData, e.newData);
				actionStack.newAction(action);
			}
			notifyEditListeners(e);
			refreshObject(path);
		}
	}
	
	protected void onDeleteButton(Label label) {
		int i = indexOf(label);
		@SuppressWarnings("unchecked")
		T data = (T) label.getData();
		if (actionStack != null) {
			LDeleteAction<T> action = new LDeleteAction<T>(this, null, i, new LDataTree<T>(data));
			actionStack.newAction(action);
		}
		label.dispose();
		LDeleteEvent<T> e = new LDeleteEvent<T>(null, i, new LDataTree<T>(data));
		notifyDeleteListeners(e);
	}
	
	//-------------------------------------------------------------------------------------
	// Modify
	//-------------------------------------------------------------------------------------
	
	@Override
	public LMoveEvent<T> move(LPath sourceParent, int sourceIndex,
			LPath destParent, int destIndex) { return null; }

	@Override
	public LInsertEvent<T> insert(LPath parentPath, int index, LDataTree<T> node) {
		addLabel(index, node.data);
		return new LInsertEvent<T>(parentPath, index, node);
	}

	@Override
	public LDeleteEvent<T> delete(LPath parentPath, int index) {
		Control c = gridComposite.getChildren()[index];
		@SuppressWarnings("unchecked")
		T data = (T) c.getData();
		c.dispose();
		return new LDeleteEvent<T>(parentPath, index, new LDataTree<T>(data));
	}

	//-------------------------------------------------------------------------------------
	// Refresh
	//-------------------------------------------------------------------------------------
	
	@Override
	public void refreshObject(LPath path) { 
		Label label = (Label) gridComposite.getChildren()[path.index];
		String p = getImagePath(path.index);
		label.setImage(SWTResourceManager.getImage(p));
	}

	@Override
	public void refreshAll() {
		LPath p = new LPath(0);
		Control[] c = gridComposite.getChildren();
		for(p.index = 0; p.index < c.length; p.index++) {
			refreshObject(p);
		}
	}
	
}
