package lwt.dataestructure;

import java.util.ArrayList;

public class LDataList<T> extends ArrayList<T> implements LDataCollection<T> {

	private static final long serialVersionUID = -6494822823659233992L;

	public LDataList() {
		super();
	}
	
	public LDataList(int i) {
		super(i);
	}

	@Override
	public void insert(LPath parentPath, int index, LDataTree<T> node) {
		if (index == -1)  {
			add(node.data);
		} else {
			add(index, node.data);
		}
	}

	@Override
	public void delete(LPath parentPath, int index) {
		remove(index);
	}

	@Override
	public void move(LPath sourceParent, int sourceIndex, LPath destParent,
			int destIndex) {
		T data;
		if (sourceIndex == -1) {
			data = remove(size() - 1);
		} else {
			data = remove(sourceIndex);
		}
		if (destIndex == -1) {
			add(data);
		} else {
			add(destIndex, data);
		}
	}

	public LDataTree<T> toTree() {
		LDataTree<T> root = new LDataTree<T>();
		for(T element : this) {
			new LDataTree<T>(element, root);
		}
		return root;
	}
	
}
