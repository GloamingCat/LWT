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
			System.out.println("oi");
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
