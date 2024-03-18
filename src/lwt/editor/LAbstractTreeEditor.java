package lwt.editor;

import java.util.ArrayList;

import lwt.container.LContainer;
import lwt.container.LView;
import lwt.widget.LTree;

import org.eclipse.swt.layout.FillLayout;

import lbase.action.LState;
import lbase.data.LDataCollection;
import lbase.data.LDataTree;
import lbase.data.LPath;
import lbase.event.LDeleteEvent;
import lbase.event.LEditEvent;
import lbase.event.LInsertEvent;
import lbase.event.LMoveEvent;
import lbase.event.LSelectionEvent;
import lbase.event.listener.LCollectionListener;
import lbase.event.listener.LSelectionListener;

/**
 * Holds common functionalities for LTreeEditor and LListEditor.
 *
 */
public abstract class LAbstractTreeEditor<T, ST> extends LCollectionEditor<T, ST> {
	
	protected ArrayList<LEditor> contentEditors = new ArrayList<>();
	
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
		contentEditors.add(editor);
		addChild((LView) editor);
	}
	
	public abstract LTree<T, ST> getCollectionWidget();
	protected abstract T createNewElement();
	protected abstract T duplicateElement(T original);
	protected abstract String encodeElement(T data);
	protected abstract T decodeElement(String str);

	@Override
	public LDataTree<T> duplicateData(LDataCollection<T> collection) {
		LDataTree<T> node = (LDataTree<T>) collection;
		LDataTree<T> copy = new LDataTree<T>(duplicateElement(node.data));
		for(LDataTree<T> child : node.children) {
			LDataTree<T> childCopy = duplicateData(child);
			childCopy.setParent(copy);
		}
		return copy;
	}
	
	@Override
	public String encodeData(LDataCollection<T> collection) {
		LDataTree<T> node = (LDataTree<T>) collection;
		return node.encode((e) -> encodeElement(e));
	}
	
	@Override
	public LDataTree<T> decodeData(String str) {
		return LDataTree.decode(str, (s) -> decodeElement(s));
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
		refreshDataCollection();
		if (selectedPath != null) {
			getCollectionWidget().forceSelection(selectedPath);
		} else {  
			forceFirstSelection();
		}
	}
	
	public void forceFirstSelection() {
		if (getObject() != null) {
			LDataTree<T> tree = getObject().toTree();
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
	
	public void refreshDataCollection() {
		setObject(getDataCollection());
	}
	
	protected abstract LDataCollection<T> getDataCollection();

}
