package lwt.dataestructure;

public interface LDataCollection<T> {

	void insert(LPath parentPath, int index, LDataTree<T> node);
	void delete(LPath parentPath, int index);
	void move(LPath sourceParent, int sourceIndex, LPath destParent, int destIndex);
	LDataTree<T> toTree();
	
}
