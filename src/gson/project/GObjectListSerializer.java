package gson.project;

import java.lang.reflect.Type;

import lwt.LGlobals;
import lwt.dataestructure.LDataList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class GObjectListSerializer extends GObjectSerializer<LDataList<Object>>
		implements GListSerializer<Object> {

	private Type dataType;
	
	public GObjectListSerializer(String path, Class<?> type) {
		super(path, LDataList.class);
		this.dataType = type;
	}
	
	public LDataList<Object> getList() {
		return data;
	}
	
	@Override
	public Type getDataType() {
		return dataType;
	}
	
	protected byte[] serialize() {
		JsonArray array = new JsonArray();
		for (Object obj : this.data) {
			JsonElement je = LGlobals.prettyGson.toJsonTree(obj, getDataType());
			array.add(je);
		}
		return LGlobals.prettyGson.toJson(array).getBytes();
	}
	
	@Override
	protected void deserialize(byte[] bytes) {
		String string = new String(bytes);
		JsonArray array = LGlobals.json.parse(string).getAsJsonArray();
		data = new LDataList<>();
		for (JsonElement je : array) {
			Object obj = LGlobals.prettyGson.fromJson(je, getDataType());
			data.add(obj);
		}
	}

	@Override
	public void initialize() {
		data = new LDataList<>();
	}

}
