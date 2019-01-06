package lwt.widget;

import lwt.LVocab;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LSelectionEvent;
import lwt.event.listener.LSelectionListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridData;

public class LNodeSelector<T> extends LControl<Integer> {

	protected LDataTree<T> collection;
	protected LTree<T, T> tree;
	protected Button btnNull;
	
	public LNodeSelector(Composite parent, int style) {
		this(parent, style, true);
	}
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LNodeSelector(Composite parent, int style, boolean optional) {
		super(parent, style);
		tree = new LTree<T, T>(this, SWT.NONE) {
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
			public LDataTree<T> duplicateNode(LPath nodePath) {
				return null;
			}
			@Override
			public LDataTree<T> toNode(LPath path) {
				return collection.getNode(path);
			}
		};
		tree.addSelectionListener(new LSelectionListener() {
			@Override
			public void onSelect(LSelectionEvent event) {
				LPath path = tree.getSelectedPath();
				int id = path == null ? -1 : collection.getNode(path).id;
				if (id == (Integer)currentValue)
					return;
				currentValue = id;
				newModifyAction(currentValue, id);
			}
		});
		tree.dragEnabled = false;
		
		if (!optional)
			return;
		
		setLayout(new GridLayout(1, false));
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		btnNull = new Button(this, SWT.NONE);
		btnNull.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		btnNull.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				tree.select(null);
			}
		});
		btnNull.setText(LVocab.instance.DESELECT);
		
	}
	
	public void setValue(Object obj) {
		tree.select(null);
		if (obj != null) {
			Integer i = (Integer) obj;
			if (i >= 0) {
				LDataTree<?> node = collection.findNode(i);
				if (node != null) {
					tree.select(node.toPath());
				}
			}
			currentValue = i;
		} else {
			currentValue = null;
		}
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
	
}
