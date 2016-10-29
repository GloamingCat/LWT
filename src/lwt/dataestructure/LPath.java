package lwt.dataestructure;

public class LPath {

	public int index;
	public LPath child;
	
	/**
	 * Create a path to the collection's tree with the given index.
	 * @param collection
	 * @param index
	 */
	public LPath(int index) {
		this.index = index;
	}
	
	public LPath(int index, LPath parent) {
		this.index = index;
		if (parent != null)
			parent.child = this;
	}
	
	public LPath lastChild() {
		LPath p = this;
		while(p.child != null) {
			p = p.child;
		}
		return p;
		
	}
	
}
