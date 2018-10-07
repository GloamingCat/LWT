package gson.project;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public abstract class GMultiSerializer<NodeT, DataT, CollectionType> 
		extends GObjectSerializer<CollectionType> {
	
	protected String folder;
	protected HashMap<NodeT, DataT> loadedData = new HashMap<>();
	protected GObjectSerializer<DataT> nodeSerializer;
	
	public <T> GMultiSerializer(String folder, String fileName, Type collectionType, Type dataType) {
		super(folder + fileName, collectionType);
		this.folder = folder;
		nodeSerializer = new GObjectSerializer<DataT>("", dataType);
	}
	
	@Override
	public boolean save() {
		if (!super.save())
			return false;
		for(Map.Entry<NodeT, DataT> entry : loadedData.entrySet()) {
			nodeSerializer.setData(entry.getValue());
			nodeSerializer.setPath(folder + toFileName(entry.getKey()) + ".json");
			if (!nodeSerializer.save()) {
				return false;
			}
		}
		return true;
	}
	
	public DataT loadData(NodeT node) {
		DataT data = loadedData.get(node);
		if (data == null) {
			nodeSerializer.setPath(folder + toFileName(node) + ".json");
			if (nodeSerializer.load()) {
				data = nodeSerializer.getData();
				loadedData.put(node, data);
			}
		}
		return data;
	}
	
	public abstract String toFileName(NodeT node);

}
