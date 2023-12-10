package lwt.editor;

import java.util.ArrayList;

import lwt.container.LContainer;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LDeleteEvent;
import lwt.event.LEditEvent;
import lwt.event.LInsertEvent;
import lwt.event.LMoveEvent;
import lwt.event.LSelectionEvent;
import lwt.event.listener.LCollectionListener;
import lwt.event.listener.LSelectionListener;
import lwt.widget.LTree;

import org.eclipse.swt.layout.FillLayout;

/**
 * Holds common functionalities for LTreeEditor and LListEditor.
 *
 */
public abstract class LAbstractTreeEditor<T, ST> extends LCollectionEditor<T, ST> {
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LAbstractTreeEditor(LContainer parent) {
		super(parent);
		setLayout(new FillLayout());
	}
	
	protected void setListeners() {
		super.setListeners();
		getCollectionWidget().addInsertListener(new LCollectionListener<T>() {
			public void onInsert(LInsertEvent<T> event) {
				getCollectionWidget().forceSelection(event.parentPath, event.index);
			}
		});
		getCollectionWidget().addDeleteListener(new LCollectionListener<T>() {
			public void onDelete(LDeleteEvent<T> event) {
				try {
					getCollectionWidget().forceSelection(event.parentPath, event.index);
				} catch(IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
					try {
						getCollectionWidget().forceSelection(event.parentPath, event.index - 1);
					} catch(IllegalArgumentException | ArrayIndexOutOfBoundsException e2) {
						forceFirstSelection();
					}
				}
			}
		});
		getCollectionWidget().addMoveListener(new LCollectionListener<T>() {
			public void onMove(LMoveEvent<T> event) {
				getCollectionWidget().forceSelection(event.destParent, event.destIndex);
			}
		});
		getCollectionWidget().addEditListener(new LCollectionListener<ST>() {
			public void onEdit(LEditEvent<ST> event) {
				getCollectionWidget().forceSelection(event.path);
			}
		});
	}
	
	public void addChild(LObjectEditor<?> editor) {
		getCollectionWidget().addSelectionListener(new LSelectionListener() {
			@Override
			public void onSelect(LSelectionEvent event) {
				LPath path = getCollectionWidget().getSelectedPath();
				editor.setObject(getCollectionWidget().toObject(path), path);
			}
		});
		editor.collectionEditor = this;
		addChild((LEditor) editor);
	}
	
	public abstract LTree<T, ST> getCollectionWidget();
	protected abstract T createNewData();
	protected abstract T duplicateData(T original);
	protected abstract String encodeData(T data);
	protected abstract T decodeData(String str);
	
	public LDataTree<T> duplicateNode(LDataTree<T> node) {
		LDataTree<T> copy = new LDataTree<T>(duplicateData(node.data));
		for(LDataTree<T> child : node.children) {
			LDataTree<T> childCopy = duplicateNode(child);
			childCopy.setParent(copy);
		}
		return copy;
	}
	
	public String encodeNode(LDataTree<T> node) {
		String str = node.id + " | " + node.children.size() + " | "
				+ encodeData(node.data) + " | ";
		for (LDataTree<T> child : node.children) {
			str = str + encodeNode(child);
		}
		return str;
	}
	
	public LDataTree<T> decodeNode(String str) {
		// Get ID
		int i = str.indexOf(" | ");
		int id = Integer.parseInt(str.substring(0, i));
		str = str.substring(i + 3);
		// Get number of children
		i = str.indexOf(" | ");
		int children = Integer.parseInt(str.substring(0, i));
		str = str.substring(i + 3);
		// Get data
		i = str.indexOf(" | ");
		T data = decodeData(str.substring(0, i));
		str = str.substring(i + 3);
		// Get children
		LDataTree<T> node = new LDataTree<T>(data);
		node.id = id;
		for (i = 0; i < children; i++) {
			LDataTree<T> child = decodeNode(str);
			child.setParent(node);
		}
		return node;
	}
	
	public void setObject(Object obj) {
		LPath selectedPath = getCollectionWidget().getSelectedPath();
		super.setObject(obj);
		if (selectedPath != null) {
			getCollectionWidget().forceSelection(selectedPath);
		} else {  
			forceFirstSelection();
		}
	}
	
	public void onVisible() {
		LPath selectedPath = getCollectionWidget().getSelectedPath();
		onChildVisible();
		if (selectedPath != null) {
			getCollectionWidget().forceSelection(selectedPath);
		} else {  
			forceFirstSelection();
		}
	}
	
	public void forceFirstSelection() {
		if (getDataCollection() != null) {
			LDataTree<T> tree = getDataCollection().toTree();
			getCollectionWidget().setItems(tree);
			if (tree.children.size() > 0) {
				getCollectionWidget().forceSelection(new LPath(0));
			} else {
				getCollectionWidget().forceSelection(null);
			}
		} else {
			getCollectionWidget().setItems(null);
			getCollectionWidget().forceSelection(null);
		}
	}
	
	@Override
	public LState getState() {
		final LPath currentPath = getCollectionWidget().getSelectedPath();
		final ArrayList<LState> states = getChildrenStates();
		return new LState() {
			@Override
			public void reset() {
				LSelectionEvent e = getCollectionWidget().select(currentPath);
				getCollectionWidget().notifySelectionListeners(e);
				resetStates(states);
			}
		};
	}
	
}
