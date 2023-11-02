package lwt.dataestructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashSet;
import java.util.Stack;
import java.util.TreeMap;

public class LDataTree<T> implements Serializable, LDataCollection<T> {

	private static final long serialVersionUID = 2898742643380172905L;

	public transient LDataTree<T> parent;
	public transient Map<Integer, T> dataMap = new TreeMap<>();
	public transient Map<String, HashSet<Integer>> idMap = new TreeMap<>();

	public int id = -1;
	public T data;
	public LDataList<LDataTree<T>> children = new LDataList<>();

	public LDataTree() {
	}

	public LDataTree(T data) {
		this.data = data;
	}

	public LDataTree(int id, T data, LDataTree<T> parent) {
		this.id = id;
		this.data = data;
		this.parent = parent;
		parent.addChild(this);
	}

	public LDataTree(T data, LDataTree<T> parent) {
		this.data = data;
		this.parent = parent;
		parent.addChild(this);
	}

	public LDataTree(T data, LDataTree<T> parent, int index) {
		this.data = data;
		this.parent = parent;
		parent.addChild(this, index);
	}

	public void initID(int id) {
		this.id = id;
		if (parent != null) {
			parent.dataMap.put(id, data);
		}
	}

	public void setParent(LDataTree<T> parent) {
		if (this.parent != null) {
			this.parent.removeChild(this);
		}
		if (parent != null) {
			parent.addChild(this);
		}
		this.parent = parent;
	}

	public void setParent(LDataTree<T> parent, int index) {
		if (index == -1) {
			setParent(parent);
		} else {
			if (this.parent != null) {
				this.parent.removeChild(this);
			}
			if (parent != null) {
				parent.addChild(this, index);
			}
			this.parent = parent;
		}
	}

	protected void addChild(LDataTree<T> child) {
		children.add(child);
		dataMap.put(child.id, child.data);
	}

	protected void addChild(LDataTree<T> child, int pos) {
		children.add(pos, child);
		dataMap.put(child.id, child.data);
	}

	protected void removeChild(LDataTree<T> child) {
		children.remove(child);
		dataMap.remove(child.id);
		for (Map.Entry<String, HashSet<Integer>> entry : idMap.entrySet())
			entry.getValue().remove(child.id);
	}
	
	public void setKeyID(String key, int id) {
		HashSet<Integer> set = idMap.get(key);
		if (set == null) {
			set = new HashSet<Integer>();
			idMap.put(key, set);
		}
		set.add(id);
	}

	public void restoreParents() {
		for (LDataTree<T> child : children) {
			child.parent = this;
			child.restoreParents();
			if (child.id >= 0)
				dataMap.put(child.id, child.data);
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
		while (path.child != null) {
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
		LDataTree<T> current = this;
		LPath path = null;
		while (current.parent != null) {
			int pos = current.parent.children.indexOf(current);
			LPath tail = path;
			path = new LPath(pos);
			path.child = tail;
			current = current.parent;
		}
		return path;
	}

	@Override
	public LDataTree<T> toTree() {
		return this;
	}

	public T get(int id) {
		T obj = dataMap.get(id);
		if (obj != null)
			return obj;
		for (LDataTree<T> child : children) {
			obj = child.get(id);
			if (obj != null)
				return obj;
		}
		return null;
	}

	public int get(String key) {
		HashSet<Integer> set = idMap.get(key);
		if (set != null && !set.isEmpty())
			return set.iterator().next();
		for (LDataTree<T> child : children) {
			int i = child.get(key);
			if (i != -1)
				return i;
		}
		return -1;
	}

	// -------------------------------------------------------------------------------------
	// Auxiliary
	// -------------------------------------------------------------------------------------

	public LDataTree<T> findNode(int id) {
		for (LDataTree<T> child : children) {
			if (child.id == id)
				return child;
			else {
				LDataTree<T> node = child.findNode(id);
				if (node != null)
					return node;
			}
		}
		return null;
	}

	public LDataTree<T> findNode(Object data) {
		for (LDataTree<T> child : children) {
			if (child.data == data)
				return child;
			else {
				LDataTree<T> node = child.findNode(data);
				if (node != null)
					return node;
			}
		}
		return null;
	}

	public int findID() {
		Stack<LDataTree<T>> nodeStack = new Stack<>();
		ArrayList<Integer> usedIDs = new ArrayList<>();
		nodeStack.push(this);
		while (nodeStack.isEmpty() == false) {
			LDataTree<T> node = nodeStack.pop();
			for (Integer id : node.dataMap.keySet()) {
				if (id >= 0)
					usedIDs.add(id);
			}
			for (LDataTree<T> child : node.children) {
				nodeStack.push(child);
			}
		}
		Collections.sort(usedIDs);
		int chosenID = 0;
		for (Integer id : usedIDs) {
			if (chosenID == id) {
				chosenID++;
			} else {
				break;
			}
		}
		return chosenID;
	}

}
