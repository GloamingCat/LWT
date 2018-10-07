package gson.project;

import java.lang.reflect.Type;

import lwt.dataestructure.LDataTree;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class GObjectTreeSerializer extends GObjectSerializer<LDataTree<Object>>
		implements GTreeSerializer<Object> {

	private JsonParser parser = new JsonParser();
	private Type dataType;
	
	public GObjectTreeSerializer(String path, Class<?> type) {
		super(path, new TypeToken<LDataTree<Object>>(){}.getType());
		this.dataType = type;
	}
	
	public LDataTree<Object> getTree() {
		return data;
	}
	
	@Override
	public Type getDataType() {
		return dataType;
	}
	
	protected byte[] serialize() {
		JsonArray array = new JsonArray();
		for (LDataTree<?> child : this.data.children) {
			JsonElement je = serialize(child);
			array.add(je);
		}
		return gson.toJson(array).getBytes();
	}
	
	protected JsonElement serialize(LDataTree<?> node) {
		JsonObject obj = new JsonObject();
		if (node.data != null) {
			JsonElement je = gson.toJsonTree(node.data, getDataType());
			obj.add("data", je);
		}
		obj.addProperty("id", node.id);
		JsonArray children = new JsonArray();
		for (LDataTree<?> child : node.children) {
			JsonElement je = serialize(child);
			children.add(je);
		}
		obj.add("children", children);
		return obj;
	}
	
	@Override
	protected void deserialize(byte[] bytes) {
		data = new LDataTree<>();
		String string = new String(bytes);
		JsonArray array = parser.parse(string).getAsJsonArray();
		for (JsonElement je : array) {
			deserialize(je).setParent(data);
		}
	}
	
	protected LDataTree<Object> deserialize(JsonElement je) {
		LDataTree<Object> node = new LDataTree<>();
		JsonObject obj = je.getAsJsonObject();
		if (obj.has("id")) {
			int id = obj.get("id").getAsInt();
			node.id = id;
		}
		if (obj.has("data")) {
			JsonObject dat = obj.get("data").getAsJsonObject();
			node.data = gson.fromJson(dat, getDataType());
		}
		if (obj.has("children")) {
			JsonArray arr = obj.get("children").getAsJsonArray();
			for (JsonElement child : arr) {
				deserialize(child).setParent(node);
			}
		}
		return node;
	}

	@Override
	public void initialize() {
		data = new LDataTree<>();
	}

}
