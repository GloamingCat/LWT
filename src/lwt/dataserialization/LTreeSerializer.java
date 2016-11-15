package lwt.dataserialization;

import java.util.HashMap;

import lwt.dataestructure.LDataTree;

public abstract class LTreeSerializer<Node> extends LObjectSerializer {

	protected LDataTree<Node> root = new LDataTree<Node>();
	
	protected HashMap<Object, LObjectSerializer> serializers = new HashMap<>();	
	
	public LTreeSerializer(String path, Class<?> type) {
		super(path, type);
	}
	
	public LDataTree<Node> getTree() {
		return root;
	}

	@Override
	public boolean save() {
		if (!super.save()) {
			return false;
		}
		for(LObjectSerializer os : serializers.values()) {
			if (!os.save())
				return false;
		}
		return true;
	}
	
	public LObjectSerializer load(Node node) {
		LObjectSerializer s = serializers.get(node);
		if (s == null) {
			s = createNodeSerializer(node);
			serializers.put(node, s);
		}
		return s;
	}
	
	protected abstract LObjectSerializer createNodeSerializer(Node node);

}
