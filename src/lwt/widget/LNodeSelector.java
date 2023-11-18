package lwt.widget;

import lwt.LVocab;
import lwt.container.LContainer;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LSelectionEvent;
import lwt.event.listener.LSelectionListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.GridData;

public class LNodeSelector<T> extends LControlWidget<Integer> {

	protected LDataTree<T> collection;
	protected LTree<T, T> tree;
	protected Button btnNull;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LNodeSelector(LContainer parent, boolean optional) {
		super(parent);
		setLayout(new GridLayout(1, false));
		tree = new LTree<T, T>(this) {
			@Override
			public T toObject(LPath path) {
				LDataTree<T> node = collection.getNode(path);
				if (node == null)
					return null;
				return node.data;
			}
			@Override
			public LDataTree<T> emptyNode() {
				return null;
			}
			@Override
			public LDataTree<T> duplicateNode(LDataTree<T> node) {
				return null;
			}
			@Override
			public LDataTree<T> toNode(LPath path) {
				return collection.getNode(path);
			}
		};
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tree.addSelectionListener(new LSelectionListener() {
			@Override
			public void onSelect(LSelectionEvent event) {
				LPath path = tree.getSelectedPath();
				int id = path == null ? -1 : collection.getNode(path).id;
				if ((Integer)id == currentValue)
					return;
				currentValue = id;
				newModifyAction(currentValue, id);
			}
		});
		tree.dragEnabled = false;
		
		if (!optional)
			return;
		
		btnNull = new Button(this, SWT.NONE);
		btnNull.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		btnNull.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				selectNone();
			}
		});
		btnNull.setText(LVocab.instance.DESELECT);
		
	}
	
	public void selectNone() {
		tree.notifySelectionListeners(tree.select(null));
	}
	
	public void addSelectionListener(LSelectionListener l) {
		tree.addSelectionListener(l);
	}
	
	public void setValue(LPath path) {
		tree.select(null);
		if (path != null) {
			tree.select(path);
			currentValue = collection.getNode(path).id;
		} else {
			currentValue = null;
		}
	}
	
	public void setValue(Object obj) {
		if (obj != null) {
			Integer i = (Integer) obj;
			currentValue = i;
			if (i >= 0) {
				LDataTree<?> node = collection.findNode((int) i);
				if (node != null) {
					tree.select(node.toPath());
					return;
				}
			}
		} else {
			currentValue = null;
		}
		tree.select(null);
	}
	
	public void setCollection(LDataTree<T> collection) {
		this.collection = collection;
		tree.setDataCollection(collection);
	}
	
	public T getSelectedObject() {
		if (currentValue == null)
			return null;
		return collection.get(currentValue);
	}
	
	public LPath getSelectedPath() {
		return tree.getSelectedPath();
	}
	
	public LDataTree<T> getSelectedNode() {
		LPath path = getSelectedPath();
		if (path == null)
			return null;
		return collection.getNode(path);
	}
	
	public void forceFirstSelection() {
		if (collection.children.size() > 0)
			tree.forceSelection(new LPath(0));
		else 
			tree.select(null);
	}
	
}
