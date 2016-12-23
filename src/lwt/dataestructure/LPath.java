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
	
	public LPath addLast(int index) {
		LPath newPath = new LPath(this.index);
		LPath newLast = newPath;
		LPath last = this;
		while (last.child != null) {
			last = last.child;
			newLast.child = new LPath(last.index);
			newLast = newLast.child;
		}
		newLast.child = new LPath(index);
		return newPath;
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
	
	public String toString() {
		String s = "";
		LPath path = this;
		while(path != null) {
			s += path.index + " ";
			path = path.child;
		}
		return s;
	}
	
}
