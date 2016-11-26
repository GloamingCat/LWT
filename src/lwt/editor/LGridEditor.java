package lwt.editor;

import lwt.dataestructure.LDataList;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LEditEvent;
import lwt.widget.LGrid;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public abstract class LGridEditor<T, ST> extends LCollectionEditor<T, ST> {

	protected LGrid<T, ST> grid;
	
	public LGridEditor(Composite parent, int style) {
		super(parent, style);

		LGridEditor<T, ST> self = this;
		grid = new LGrid<T, ST>(this, SWT.NONE) {
			@Override
			public LEditEvent<ST> edit(LPath path) {
				return onEditItem(path);
			}
			@Override
			public T toObject(LPath path) {
				if (path == null)
					return null;
				return getDataCollection().get(path.index);
			}
			@Override
			public LDataTree<T> emptyNode() {
				return new LDataTree<T>(createNewData());
			}
			@Override
			public LDataTree<T> duplicateNode(LPath path) {
				return new LDataTree<T> (duplicateData(getDataCollection().get(path.index)));
			}
			@Override
			protected String getImagePath(int i) {
				return self.getImagePath(i);
			}
		};
		setListeners();
	}

	@Override
	public void setObject(Object obj) {
		@SuppressWarnings("unchecked")
		LDataList<T> db = (LDataList<T>) obj;
		getCollectionWidget().setList(db);
	}
	
	
	public void onVisible() {
		onChildVisible();
		grid.setDataCollection(getDataCollection());
	}
		
	@Override
	public LGrid<T, ST> getCollectionWidget() {
		return grid;
	}
	protected abstract T createNewData();
	protected abstract T duplicateData(T original);
	protected abstract String getImagePath(int i);
	
	protected abstract LDataList<T> getDataCollection();

}
