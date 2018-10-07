package gson.project;

import java.lang.reflect.Type;

import lwt.dataestructure.LDataList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class GObjectListSerializer extends GObjectSerializer<LDataList<Object>>
		implements GListSerializer<Object> {

	private JsonParser parser = new JsonParser();
	private Type dataType;
	
	public GObjectListSerializer(String path, Class<?> type) {
		super(path, new TypeToken<LDataList<Object>>(){}.getType());
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
			JsonElement je = gson.toJsonTree(obj, getDataType());
			array.add(je);
		}
		return gson.toJson(array).getBytes();
	}
	
	@Override
	protected void deserialize(byte[] bytes) {
		String string = new String(bytes);
		JsonArray array = parser.parse(string).getAsJsonArray();
		data = new LDataList<>();
		for (JsonElement je : array) {
			Object obj = gson.fromJson(je, getDataType());
			data.add(obj);
		}
	}

	@Override
	public void initialize() {
		data = new LDataList<>();
	}

}
