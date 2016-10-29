package lwt.dataestructure;

import java.io.Serializable;
import java.util.ArrayList;

public class LDataTree<T> implements Serializable {

	private static final long serialVersionUID = 2898742643380172905L;
	
	public transient LDataTree<T> parent;
	public ArrayList<LDataTree<T>> children = new ArrayList<>();
	public T data;
	
	public LDataTree() {}
	
	public LDataTree(T data) {
		this.data = data;
	}
	
	public LDataTree(T data, LDataTree<T> parent) {
		this.data = data;
		this.parent = parent;
		parent.children.add(this);
	}
	
	public void setParent(LDataTree<T> parent) {
		if (this.parent != null) {
			this.parent.children.remove(this);
		}
		parent.children.add(this);
		this.parent = parent;
	}
	
	public void restoreParents() {
		for(LDataTree<T> child : children) {
			child.parent = this;
			child.restoreParents();
		}
	}
	
	public LDataTree<T> getNode(LPath path) {
		LDataTree<T> child = children.get(path.index);
		while(path.child != null) {
			path = path.child;
			child = child.children.get(path.index);
		}
		return child;
	}
	
	public LPath toPath() {
		if (parent == null) {
			return null;
		}
		LPath parentPath = parent.toPath();
		LPath path = new LPath(parent.children.indexOf(this), parentPath);
		return path;
	}
	
	public LDataTree<String> toStringNode() {
		LDataTree<String> node = new LDataTree<>(data.toString());
		for(LDataTree<T> child : children) {
			child.toStringNode().setParent(node);
		}
		return node;
	}
	
}
