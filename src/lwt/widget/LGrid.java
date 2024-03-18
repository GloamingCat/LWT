package lwt.widget;

import lwt.container.LContainer;
import lwt.container.LImage;
import lwt.editor.LPopupMenu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import lbase.action.LActionStack;
import lbase.data.LDataCollection;
import lbase.data.LDataList;
import lbase.data.LDataTree;
import lbase.data.LPath;
import lbase.event.LDeleteEvent;
import lbase.event.LEditEvent;
import lbase.event.LInsertEvent;
import lbase.event.LMoveEvent;
import lbase.event.LSelectionEvent;

public abstract class LGrid<T, ST> extends LSelectableCollection<T, ST> implements LContainer {

	protected LActionStack actionStack;
	protected Layout layout;
	protected FillLayout fillLayout;
	protected int selectedIndex = -1;
	protected T selectedObj = null;

	private boolean editEnabled = false;
	private boolean insertEnabled = false;
	private boolean duplicateEnabled = false;
	private boolean deleteEnabled = false;
	
	public int cellWidth = 24;
	public int cellHeight = 24;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LGrid(LContainer parent) {
		super(parent);
		fillLayout = new FillLayout();
		setColumns(-1);
		setLayout(fillLayout);
	}
	
	@Override
	protected void createContent(int flags) {
		LImage label = new LImage(this);
		Menu menu = new Menu(label);
		label.setMenu(menu);
		MenuItem mntmNewItem = new MenuItem(menu, SWT.NONE);
		mntmNewItem.setText("New Item");
	}

	public void setColumns(int columns) {
		if (columns <= 0) {
			RowLayout rowLayout = new RowLayout(SWT.HORIZONTAL);
			rowLayout.fill = true;
			rowLayout.pack = true;
			rowLayout.wrap = true;
			rowLayout.spacing = 5;
			rowLayout.marginWidth = 0;
			rowLayout.marginHeight = 0;
			layout = rowLayout;
		} else {
			GridLayout gridLayout = new GridLayout(columns, true);
			gridLayout.marginWidth = 0;
			gridLayout.marginHeight = 0;
			gridLayout.horizontalSpacing = 5;
			gridLayout.verticalSpacing = 5;
			layout = gridLayout;
		}
	}

	public void setDataCollection(LDataCollection<T> collection) {
		LDataList<T> list = (LDataList<T>) collection;
		setList(list);
	}
	
	@SuppressWarnings("unchecked")
	public LDataList<T> getDataCollection() {
		LDataList<T> list = new LDataList<T>();
		for(Control c : getChildren()) {
			list.add((T) c.getData());
		}
		return list;
	}
	
	public void clear() {
		for(Control c : getChildren()) {
			c.dispose();
		}
	}
	
	private void emptyLayout() {
		setLayout(fillLayout);
		addLabel(0, null, true);
		layout();
	}
	
	private int indexOf(LImage label) {
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
		if (list != null) {
			if (list.size() > 0) {
				setLayout(layout);
				int i = 0;
				for(T data : list) {
					LImage label = addLabel(i, data, false);
					setImage(label, i);
					i++;
				}
				layout();
			} else {
				emptyLayout();
			}
		}
	}
	
	private LImage addLabel(int i, T data, boolean placeholder) {
		LImage label = new LImage(this);
		if (layout instanceof GridLayout) {
			//label.setExpand(true, true);
			label.setMinimumWidth(cellWidth);
			label.setMinimumHeight(cellHeight);
		} else {
			label.setLayoutData(new RowData(cellWidth, cellHeight));
		}
		LPopupMenu menu = new LPopupMenu(label);
		menu.setData("label", label);
		
		if (placeholder) {
			if (insertEnabled)
				setInsertNewEnabled(menu, true);
		} else {
			label.setData(data);
			label.addPaintListener(new PaintListener() {
				@Override
				public void paintControl(PaintEvent e) {
					if (indexOf(label) == selectedIndex) {
						e.gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
						e.gc.drawRectangle(0, 0, label.getBounds().width - 1, label.getBounds().height - 1);
					}
				}
			});
			label.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent arg0) {
					if (label.isDisposed())
						return;
					int i = indexOf(label);
					select(data, i);
					LSelectionEvent e = new LSelectionEvent(new LPath(i), data, i);
					notifySelectionListeners(e);
				}
			});
			if (!isEditable())
				return label;
			setEditEnabled(menu, editEnabled);
			setInsertNewEnabled(menu, insertEnabled);
			setDuplicateEnabled(menu, duplicateEnabled);
			setDeleteEnabled(menu, deleteEnabled);
		}
		
		return label;
	}
	
	private boolean isEditable() {
		return editEnabled || insertEnabled || duplicateEnabled || deleteEnabled;
	}

	protected abstract void setImage(LImage label, int i);
	
	public LImage getSelectedCell() {
		if (selectedIndex == -1)
			return null;
		return (LImage) getChildren()[selectedIndex];
	}
	
	//-------------------------------------------------------------------------------------
	// Button Handler
	//-------------------------------------------------------------------------------------
	
	@Override
	protected void onEditButton(lbase.gui.LMenu menu) {
		LImage label = (LImage) ((LPopupMenu) menu).getData("label");
		int i = indexOf(label);
		LPath path = new LPath(i);
		newEditAction(path);
	}
	
	@Override
	protected void onInsertNewButton(lbase.gui.LMenu menu) {
		LImage label = (LImage) ((LPopupMenu) menu).getData("label");
		int i = indexOf(label) + 1;
		LDataTree<T> newNode = emptyNode();
		newInsertAction(null, i, newNode);
	}
	
	@Override
	protected void onDuplicateButton(lbase.gui.LMenu menu) {
		LImage label = (LImage) ((LPopupMenu) menu).getData("label");
		int i = indexOf(label);
		LDataTree<T> newNode = duplicateNode(new LPath(i));
		newInsertAction(null, i, newNode);
	}
	
	@Override
	protected void onDeleteButton(lbase.gui.LMenu menu) {
		LImage label = (LImage) ((LPopupMenu) menu).getData("label");
		int i = indexOf(label);
		newDeleteAction(null, i);
	}
	
	public void setEditEnabled(boolean value) {
		editEnabled = value;
	}
	
	public void setInsertNewEnabled(boolean value) {
		insertEnabled = value;
	}
	
	public void setDuplicateEnabled(boolean value) {
		duplicateEnabled = value;
	}
	
	public void setDeleteEnabled(boolean value) {
		deleteEnabled = value;
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
		if (getLayout() == fillLayout) {
			clear();
			setLayout(layout);
		}
		addLabel(index, node.data, false);
		return new LInsertEvent<T>(parentPath, index, node);
	}

	@Override
	public LDeleteEvent<T> delete(LPath parentPath, int index) {
		Control c = getChildren()[index];
		@SuppressWarnings("unchecked")
		T data = (T) c.getData();
		c.dispose();
		if (getChildren().length == 0) {
			emptyLayout();
		}
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
		LImage label = (LImage) getChildren()[path.index];
		T data = toObject(path);
		label.setData(data);
		if (path.index == selectedIndex)
			label.reskin(SWT.BORDER);
		else
			label.reskin(SWT.NONE);
		setImage(label, path.index);
	}

	@Override
	public void refreshAll() {
		if (getLayout() == layout) {
			LPath p = new LPath(0);
			Control[] c = getChildren();
			for(p.index = 0; p.index < c.length; p.index++) {
				refreshObject(p);
			}
		}
	}
	
	//-------------------------------------------------------------------------------------
	// Selection
	//-------------------------------------------------------------------------------------
	
	public LSelectionEvent select(LPath path) {
		if (path.index >= 0) {
			LImage l = (LImage) getChildren()[path.index];
			@SuppressWarnings("unchecked")
			T data = (T) l.getData("data");
			select(data, path.index);
			return new LSelectionEvent(path, data, path.index);
		} else {
			select((T) null, -1);
			return new LSelectionEvent(path, null, path.index);
		}
	}
	
	public T getSelectedObject() {
		return selectedObj;
	}
	
	public LPath getSelectedPath() {
		if (selectedIndex == -1) {
			return null;
		} else {
			return new LPath(selectedIndex);
		}
	}
	
	protected void select(T obj, int i) {
		if (i != selectedIndex) {
			if (selectedIndex != -1) {
				LPath path = new LPath(selectedIndex);
				selectedIndex = i;
				refreshObject(path);
			}
			selectedIndex = i;
			selectedObj = obj;
			if (i >= 0) {
				refreshObject(new LPath(i));
			}
			layout();
		}
	}
	
	@Override
	public void onCopyButton(lbase.gui.LMenu menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPasteButton(lbase.gui.LMenu menu) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Composite getContentComposite() {
		return this;
	}
	
	@Override
	public Object getChild(int i) {
		return getChildren()[i];
	}
	
	@Override
	public int getChildCount() {
		return this.getChildren().length;
	}

}
