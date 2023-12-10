package gson.project;

import java.lang.reflect.Type;

import lwt.LGlobals;
import lwt.dataestructure.LDataTree;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GObjectTreeSerializer extends GObjectSerializer<LDataTree<Object>>
		implements GTreeSerializer<Object> {

	private Type dataType;
	
	public GObjectTreeSerializer(String path, Class<?> type) {
		super(path, LDataTree.class);
		this.dataType = type;
	}
	
	public LDataTree<Object> getTree() {
		return data;
	}
	
	public String serialize(LDataTree<?> node) {
		return LGlobals.gson.toJson(encode());
	}
	
	public LDataTree<Object> deserialize(String str) {
		JsonArray array = LGlobals.json.parse(str).getAsJsonArray();
		return decode(array);
	}
	
	@Override
	public Type getDataType() {
		return dataType;
	}
	
	public JsonElement encode() {
		JsonArray array = new JsonArray();
		for (LDataTree<?> child : this.data.children) {
			JsonElement je = encode(child);
			array.add(je);
		}
		return array;
	}
	
	protected JsonElement encode(LDataTree<?> node) {
		JsonObject obj = new JsonObject();
		if (node.data != null) {
			JsonElement je = LGlobals.prettyGson.toJsonTree(node.data, getDataType());
			obj.add("data", je);
		}
		obj.addProperty("id", node.id);
		JsonArray children = new JsonArray();
		for (LDataTree<?> child : node.children) {
			JsonElement je = encode(child);
			children.add(je);
		}
		obj.add("children", children);
		return obj;
	}
	
	protected LDataTree<Object> decode(JsonElement je) {
		LDataTree<Object> node = new LDataTree<>();
		JsonObject obj = je.getAsJsonObject();
		if (obj.has("id")) {
			int id = obj.get("id").getAsInt();
			node.id = id;
		}
		if (obj.has("data")) {
			JsonObject dat = obj.get("data").getAsJsonObject();
			node.data = LGlobals.prettyGson.fromJson(dat, getDataType());
		}
		if (obj.has("children")) {
			JsonArray arr = obj.get("children").getAsJsonArray();
			for (JsonElement child : arr) {
				decode(child).setParent(node);
			}
		}
		return node;
	}

	protected LDataTree<Object> decode(JsonArray array) {
		LDataTree<Object> data = new LDataTree<>();
		for (JsonElement je : array) {
			decode(je).setParent(data);
		}
		return data;
	}

	@Override
	public void initialize() {
		data = new LDataTree<>();
	}	
	
	@Override
	protected byte[] serialize() {
		return LGlobals.prettyGson.toJson(encode()).getBytes();
	}
	
	@Override
	public void deserialize(byte[] bytes) {
		String string = new String(bytes);
		JsonArray array = LGlobals.json.parse(string).getAsJsonArray();
		data = decode(array);
	}

}
