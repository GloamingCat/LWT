package lwt.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import gson.GGlobals;
import lbase.data.LDataList;
import lwt.container.LContainer;
import lwt.editor.LGridForm;

public abstract class GGridForm<T> extends LGridForm<T> {

	public GGridForm(LContainer parent, int columns) {
		super(parent, columns);
	}

	@SuppressWarnings("unchecked")
	public LDataList<T> duplicateData(LDataList<T> obj) {
		LDataList<T> copy = new LDataList<>();		
		if (!copy.getClass().isInstance(obj))
			throw new ClassCastException("Object cannot be cast to " + copy.getClass().getTypeName());
		LDataList<T> list = (LDataList<T>) obj;
		for (T original : list) {
			String json = GGlobals.gson.toJson(original, original.getClass());
			copy.add((T) GGlobals.gson.fromJson(json, original.getClass()));
		}
		return copy;
	}
	
	@Override
	public String encodeData(LDataList<T> data) {
		return GGlobals.gson.toJson(data, data.getClass());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public LDataList<T> decodeData(String str) {
		LDataList<T> list = new LDataList<>();
		JsonArray array = GGlobals.json.parse(str).getAsJsonArray();
		for (JsonElement je : array) {
			list.add((T) GGlobals.gson.fromJson(je, getType()));
		}
		return list;
	}

	@Override
	public boolean canDecode(String str) {
		return true;
	}
	
	public abstract Class<?> getType();
	
}
