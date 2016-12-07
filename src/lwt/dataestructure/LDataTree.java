package lwt.dataestructure;

import java.io.Serializable;

public class LDataTree<T> implements Serializable, LDataCollection<T> {

	private static final long serialVersionUID = 2898742643380172905L;
	
	public transient LDataTree<T> parent;
	public LDataList<LDataTree<T>> children = new LDataList<>();
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
	
	public LDataTree(T data, LDataTree<T> parent, int index) {
		this.data = data;
		this.parent = parent;
		parent.children.add(index, this);
	}

	public void setParent(LDataTree<T> parent) {
		if (this.parent != null) {
			this.parent.children.remove(this);
		}
		if (parent != null) {
			parent.children.add(this);
		}
		this.parent = parent;
	}
	
	public void setParent(LDataTree<T> parent, int index) {
		if (index == -1) {
			setParent(parent);
		} else {
			if (this.parent != null) {
				this.parent.children.remove(this);
			}
			if (parent != null) {
				parent.children.add(index, this);
			}
			this.parent = parent;
		}
	}
	
	public void restoreParents() {
		for(LDataTree<T> child : children) {
			child.parent = this;
			child.restoreParents();
		}
	}
	
	public void insert(LPath path, int index, LDataTree<T> node) {
		LDataTree<T> parentNode = getNode(path);
		node.setParent(parentNode, index);
	}
	
	public void move(LPath sourcePath, int sourceIndex, LPath destPath, int destIndex) {
		LDataTree<T> sourceNode = getNode(sourcePath, sourceIndex);
		sourceNode.setParent(null);
		LDataTree<T> parentNode = getNode(destPath);
		sourceNode.setParent(parentNode, destIndex);
	}
	
	public void delete(LPath path, int index) {
		LDataTree<T> node = getNode(path, index);
		node.setParent(null);
	}
	
	public LDataTree<T> getNode(LPath path) {
		if (path == null)
			return this;
		LDataTree<T> child = children.get(path.index);
		while(path.child != null) {
			path = path.child;
			child = child.children.get(path.index);
		}
		return child;
	}
	
	public LDataTree<T> getNode(LPath parentPath, int index) {
		LDataTree<T> parentNode = getNode(parentPath);
		if (index == -1)
			index = parentNode.children.size() - 1;
		return parentNode.children.get(index);
	}
	
	public LPath toPath() {
		if (parent == null) {
			return null;
		}
		LPath parentPath = parent.toPath();
		parentPath.addLast(parent.children.indexOf(this));
		return parentPath;
	}

	@Override
	public LDataTree<T> toTree() {
		return this;
	}
	
}
