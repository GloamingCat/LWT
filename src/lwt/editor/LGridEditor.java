package lwt.editor;

import lwt.container.LContainer;
import lwt.dataestructure.LDataList;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LEditEvent;
import lwt.widget.LGrid;
import lwt.widget.LImage;

public abstract class LGridEditor<T, ST> extends LCollectionEditor<T, ST> {

	protected LGrid<T, ST> grid;
	
	public LGridEditor(LContainer parent) {
		super(parent);

		LGridEditor<T, ST> self = this;
		grid = new LGrid<T, ST>(this) {
			@Override
			public LEditEvent<ST> edit(LPath path) {
				return onEditItem(path);
			}
			@Override
			public T toObject(LPath path) {
				if (path == null)
					return null;
				return self.getDataCollection().get(path.index);
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
			protected void setImage(LImage img, int i) {
				self.setImage(img, i);
			}
		};
		setListeners();
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
	protected abstract void setImage(LImage label, int i);
	
	protected abstract LDataList<T> getDataCollection();

}
