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
	
	public LPath lastChild() {
		LPath p = this;
		while(p.child != null) {
			p = p.child;
		}
		return p;
	}
	
	public void addLast(int index) {
		LPath parent = this;
		while (parent.child != null) {
			parent = parent.child;
		}
		parent.child = new LPath(index);
	}
	
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof LPath) {
			LPath path = (LPath) obj;
			if (path.index != index) {
				return false;
			}
			if (child == null) {
				if (path.child == null) {
					return true;
				} else {
					return false;
				}
			}
			return child.equals(path.child);
		} else {
			return false;
		}
	}
	
	public void print() {
		LPath path = this;
		while(path != null) {
			System.out.print(path.index + " ");
			path = path.child;
		}
		System.out.println();
	}
	
}
