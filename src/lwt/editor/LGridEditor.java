package lwt.editor;

import lwt.dataestructure.LDataList;
import lwt.dataestructure.LPath;
import lwt.event.LEditEvent;
import lwt.widget.LGrid;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public abstract class LGridEditor<T, ST> extends LCollectionEditor<T, ST> {

	protected LGrid<T, ST> grid;
	
	public LGridEditor(Composite parent, int style) {
		super(parent, style);

		grid = new LGrid<T, ST>(this, SWT.NONE) {
			@Override
			public LEditEvent<ST> edit(LPath path) {
				return onEditItem(path);
			}
			@Override
			public T toObject(LPath path) {
				if (path == null)
					return null;
				return getList().get(path.index);
			}
			@Override
			protected String getImagePath(int i) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		setListeners();
		grid.setActionStack(getActionStack());
	}

	@Override
	public void setObject(Object obj) {
		@SuppressWarnings("unchecked")
		LDataList<T> db = (LDataList<T>) obj;
		getCollection().setList(db);
	}
	
	@Override
	public LGrid<T, ST> getCollection() {
		return grid;
	}

	protected LDataList<T> getDataCollection() {
		return getList();
	}
	
	public void setList(LDataList<T> list) {}
	public abstract LDataList<T> getList();

}
