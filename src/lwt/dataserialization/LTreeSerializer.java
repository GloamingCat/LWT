package lwt.dataserialization;

import lwt.dataestructure.LDataTree;

public abstract class LTreeSerializer<N, T> extends LObjectSerializer<LDataTree<N>> {

	protected LDataTree<LObjectSerializer<T>> root = new LDataTree<LObjectSerializer<T>>();
	
	public LTreeSerializer(String path, Class<LDataTree<N>> type) {
		super(path, type);
	}
	
	@Override
	public boolean isDataFolder(String path) {
		return super.isDataFolder(path) || isDataFolder(root, path);
	}
	
	private boolean isDataFolder(LDataTree<LObjectSerializer<T>> node, String path) {
		for(LDataTree<LObjectSerializer<T>> child : root.children) {
			if (child.data.path.equals(path) || isDataFolder(child, path)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean save() {
		if (!super.save()) {
			return false;
		}
		return saveNode(root);
	}
	
	private boolean saveNode(LDataTree<LObjectSerializer<T>> node) {
		for(LDataTree<LObjectSerializer<T>> child : node.children) {
			if (!child.data.save() || !saveNode(node)) {
				return false;
			}
		}
		return true;
	}

}
