package lwt.widget;

import lwt.action.LActionStack;
import lwt.dataestructure.LDataCollection;
import lwt.dataestructure.LDataList;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LDeleteEvent;
import lwt.event.LEditEvent;
import lwt.event.LInsertEvent;
import lwt.event.LMoveEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.wb.swt.SWTResourceManager;

public abstract class LGrid<T, ST> extends LCollection<T, ST> {

	protected LActionStack actionStack;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LGrid(Composite parent, int style) {
		super(parent, style);
		RowLayout rl = new RowLayout(SWT.HORIZONTAL);
		rl.fill = true;
		rl.pack = true;
		rl.wrap = true;
		setLayout(rl);
	}

	public void setDataCollection(LDataCollection<T> collection) {
		LDataList<T> list = (LDataList<T>) collection;
		setList(list);
	}
	
	public void clear() {
		for(Control c : getChildren()) {
			c.dispose();
		}
	}
	
	private int indexOf(Label label) {
		int i = 0;
		for(Control c : getChildren()) {
			if (c == label)
				return i;
			else
				i++;
		}
		return -1;
	}
	
	public void setList(LDataList<T> list) {
		clear();
		int i = 0;
		for(T data : list) {
			Label label = addLabel(i, data);
			String path = getImagePath(i);
			label.setImage(SWTResourceManager.getImage(path));
			i++;
		}
		layout();
	}
	
	private Label addLabel(int i, T data) {
		Label label = new Label(this, SWT.NONE);
		Menu menu = new Menu(label);
		label.setMenu(menu);
		label.setData(data);
		menu.setData("label", label);
		
		setEditEnabled(menu, true);
		setInsertNewEnabled(menu, true);
		setDuplicateEnabled(menu, true);
		setDeleteEnabled(menu, true);
		
		return label;
	}

	protected abstract String getImagePath(int i);

	//-------------------------------------------------------------------------------------
	// Button Handler
	//-------------------------------------------------------------------------------------
	
	protected void onEditButton(Menu menu) {
		Label label = (Label) menu.getData("label");
		int i = indexOf(label);
		LPath path = new LPath(i);
		newEditAction(path);
	}
	
	protected void onInsertNewButton(Menu menu) {
		Label label = (Label) menu.getData("label");
		int i = indexOf(label);
		LDataTree<T> newNode = emptyNode();
		newInsertAction(null, i, newNode);
	}
	
	protected void onDuplicateButton(Menu menu) {
		Label label = (Label) menu.getData("label");
		int i = indexOf(label);
		LDataTree<T> newNode = duplicateNode(new LPath(i));
		newInsertAction(null, i, newNode);
	}
	
	protected void onDeleteButton(Menu menu) {
		Label label = (Label) menu.getData("label");
		int i = indexOf(label);
		newDeleteAction(null, i);
	}
	
	//-------------------------------------------------------------------------------------
	// Modify
	//-------------------------------------------------------------------------------------
	
	@Override
	public LMoveEvent<T> move(LPath sourceParent, int sourceIndex,
			LPath destParent, int destIndex) { 
		// Not supported.
		return null; 
	}

	@Override
	public LInsertEvent<T> insert(LPath parentPath, int index, LDataTree<T> node) {
		addLabel(index, node.data);
		System.out.println("bla2");
		return new LInsertEvent<T>(parentPath, index, node);
	}

	@Override
	public LDeleteEvent<T> delete(LPath parentPath, int index) {
		Control c = getChildren()[index];
		@SuppressWarnings("unchecked")
		T data = (T) c.getData();
		c.dispose();
		return new LDeleteEvent<T>(parentPath, index, new LDataTree<T>(data));
	}
	
	protected abstract LDataTree<T> emptyNode();
	
	protected abstract LDataTree<T> duplicateNode(LPath nodePath);

	//-------------------------------------------------------------------------------------
	// Refresh
	//-------------------------------------------------------------------------------------
	
	@Override
	public void notifyEditListeners(LEditEvent<ST> event) {
		super.notifyEditListeners(event);
		refreshObject(event.path);
		layout();
	}
	
	@Override
	public void notifyInsertListeners(LInsertEvent<T> event) {
		super.notifyInsertListeners(event);
		refreshAll();
		layout();
	}
	
	@Override
	public void notifyDeleteListeners(LDeleteEvent<T> event) {
		super.notifyDeleteListeners(event);
		refreshAll();
		layout();
	}
	
	@Override
	public void refreshObject(LPath path) { 
		Label label = (Label) getChildren()[path.index];
		T data = toObject(path);
		label.setData(data);
		String p = getImagePath(path.index);
		label.setImage(SWTResourceManager.getImage(p));
	}

	@Override
	public void refreshAll() {
		LPath p = new LPath(0);
		Control[] c = getChildren();
		for(p.index = 0; p.index < c.length; p.index++) {
			System.out.println("refresh" + p.index);
			refreshObject(p);
		}
	}
	
}
