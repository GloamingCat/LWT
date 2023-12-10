package lwt.editor;

import lwt.container.LContainer;
import lwt.container.LImage;
import lwt.dataestructure.LDataCollection;
import lwt.dataestructure.LDataList;
import lwt.dataestructure.LDataTree;
import lwt.dataestructure.LPath;
import lwt.event.LEditEvent;
import lwt.widget.LGrid;

public abstract class LGridEditor<T, ST> extends LCollectionEditor<T, ST> {

	protected LGrid<T, ST> grid;
	
	public LGridEditor(LContainer parent) {
		super(parent);
		grid = createGrid();
		setListeners();
	}
	
	protected LGrid<T, ST> createGrid() {
		LGridEditor<T, ST> self = this;
		return new LGrid<T, ST>(this) {
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
				return new LDataTree<T>(createNewElement());
			}
			@Override
			public LDataTree<T> duplicateNode(LPath path) {
				return new LDataTree<T> (duplicateElement(getDataCollection().get(path.index)));
			}
			@Override
			protected void setImage(LImage img, int i) {
				self.setImage(img, i);
			}
			@Override
			public boolean canDecode(String str) {
				return true;
			}
		};
	}
	
	protected abstract LDataList<T> getDataCollection();
	
	public void onVisible() {
		onChildVisible();
		grid.setDataCollection(getDataCollection());
	}
		
	@Override
	public LGrid<T, ST> getCollectionWidget() {
		return grid;
	}
	
	protected abstract T createNewElement();
	protected abstract T duplicateElement(T original);
	protected abstract String encodeElement(T data);
	protected abstract T decodeElement(String str);
	protected abstract void setImage(LImage label, int i);

	@Override
	public LDataList<T> duplicateData(LDataCollection<T> collection) {
		LDataList<T> list = (LDataList<T>) collection;
		LDataList<T> copy = new LDataList<T>();
		for(T child : list) {
			T childCopy = duplicateElement(child);
			copy.add(childCopy);
		}
		return copy;
	}
	
	@Override
	public String encodeData(LDataCollection<T> collection) {
		LDataList<T> list = (LDataList<T>) collection;
		LDataList<String> text = new LDataList<String>();
		for (T obj : list)
			text.add(obj.toString());
		String str = String.join( " | ", text);
		return str;
	}
	
	@Override
	public LDataList<T> decodeData(String str) {
		String[] elements = str.split(" | ");
		// Get children
		LDataList<T> list = new LDataList<T>();
		for (String element : elements) {
			list.add(decodeElement(element));
		}
		return list;
	}

}
