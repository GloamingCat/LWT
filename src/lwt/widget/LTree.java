package lwt.widget;

import lwt.LGlobals;
import lwt.container.LContainer;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LDeleteEvent;
import lwt.event.LEditEvent;
import lwt.event.LInsertEvent;

import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeItem;

public abstract class LTree<T, ST> extends LTreeBase<T, ST> {
	
	protected Menu menu;
	protected boolean includeID = false;
	protected boolean editEnabled = false;
	
	public LTree(LContainer parent) {
		this(parent, false);
	}
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LTree(LContainer parent, boolean check) {
		super(parent, check);
		menu = new Menu(tree);
		tree.setMenu(menu);
		tree.addMouseListener(new MouseListener() {
			public void mouseUp(MouseEvent arg0) {}
			public void mouseDown(MouseEvent arg0) {}
			public void mouseDoubleClick(MouseEvent arg0) {
				onEditButton(menu);
			}
		});
	}
	
	public void setIncludeID(boolean value) {
		includeID = value;
	}
	
	protected String stringID(int i) {
		return String.format("[%03d] ", i);
	}
	
	//-------------------------------------------------------------------------------------
	// Modify
	//-------------------------------------------------------------------------------------
	
	public LInsertEvent<T> insert(LPath parentPath, int index, LDataTree<T> node) {
		TreeItem parent = toTreeItem(parentPath);
		createTreeItem(parent, index, node);
		//refreshAll();
		return new LInsertEvent<T>(parentPath, index, node);
	}
	
	public LDeleteEvent<T> delete(LPath parentPath, int index) {
		TreeItem item = toTreeItem(parentPath, index);
		LDataTree<T> node = disposeTreeItem(item);
		//refreshAll();
		return new LDeleteEvent<T>(parentPath, index, node);
	}
	
	public LEditEvent<ST> edit(LPath path) {
		//refreshAll();
		return null;
	}
	
	protected abstract LDataTree<T> emptyNode();
	protected abstract LDataTree<T> duplicateNode(LDataTree<T> node);
	protected abstract String encodeNode(LDataTree<T> node);
	protected abstract LDataTree<T> decodeNode(String node);
	
	//-------------------------------------------------------------------------------------
	// Modify Menu
	//-------------------------------------------------------------------------------------
	
	public void setCopyEnabled(boolean value) {
		super.setCopyEnabled(menu, value);
	}
	
	public void setPasteEnabled(boolean value) {
		super.setPasteEnabled(menu, value);
	}
	
	public void setEditEnabled(boolean value) {
		editEnabled = value;
		super.setEditEnabled(menu, value);
	}
	
	public void setInsertNewEnabled(boolean value) {
		super.setInsertNewEnabled(menu, value);
	}
	
	public void setDuplicateEnabled(boolean value) {
		super.setDuplicateEnabled(menu, value);
	}
	
	public void setDeleteEnabled(boolean value) {
		super.setDeleteEnabled(menu, value);
	}
	
	//-------------------------------------------------------------------------------------
	// Menu Handlers
	//-------------------------------------------------------------------------------------
	
	protected void onEditButton(Menu menu) {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			LPath path = toPath(item);
			LEditEvent<ST> event = newEditAction(path);
			if (event != null) {
				setItemNode(item, toNode(path));
			}
		}
	}
	
	protected void onInsertNewButton(Menu menu) {
		LPath parentPath = null;
		int index = -1;
		LDataTree<T> newNode = emptyNode();
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			parentPath = toPath(item.getParentItem());
			index = indexOf(item) + 1;
		}
		newInsertAction(parentPath, index, newNode);
	}
	
	protected void onDuplicateButton(Menu menu) {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			LPath itemPath = toPath(item);
			LDataTree<T> node = duplicateNode(toNode(itemPath));
			LPath parentPath = toPath(item.getParentItem());
			int i = indexOf(item) + 1;
			newInsertAction(parentPath, i, node);
		}
	}
	
	protected void onDeleteButton(Menu menu) {
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			TreeItem parentItem = item.getParentItem();
			LPath parentPath = toPath(parentItem);
			int i = indexOf(item);
			newDeleteAction(parentPath, i);
		}
	}
	
	protected void onCopyButton(Menu menu) {
		LPath path = getSelectedPath();
		if (path != null) {
			LDataTree<T> node = toNode(path);
			LGlobals.clipboard.setContents(new Object[] { encodeNode(node) },
					new Transfer[] { TextTransfer.getInstance() });
		}
	}
	
	protected void onPasteButton(Menu menu) {
		String str = (String) LGlobals.clipboard.getContents(TextTransfer.getInstance());
		if (str == null)
			return;
		LDataTree<T> newNode = decodeNode(str);
		LPath parentPath = null;
		int index = -1;
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			parentPath = toPath(item.getParentItem());
			index = indexOf(item) + 1;
		}
		newInsertAction(parentPath, index, newNode);
	}
	
}
