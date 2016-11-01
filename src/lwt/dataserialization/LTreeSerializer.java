package lwt.dataserialization;

import lwt.dataestructure.LDataTree;

public class LTreeSerializer<N, T> extends LObjectSerializer<LDataTree<N>> {

	protected LDataTree<LObjectSerializer<T>> root = new LDataTree<LObjectSerializer<T>>();
	
	public LTreeSerializer(String fileName, Class<LDataTree<N>> type) {
		super(fileName, type);
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
