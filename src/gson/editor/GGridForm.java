package gson.editor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import lwt.LGlobals;
import lwt.container.LContainer;
import lwt.dataestructure.LDataList;
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
			String json = LGlobals.gson.toJson(original, original.getClass());
			copy.add((T) LGlobals.gson.fromJson(json, original.getClass()));
		}
		return copy;
	}
	
	@Override
	public String encodeData(LDataList<T> data) {
		return LGlobals.gson.toJson(data, data.getClass());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public LDataList<T> decodeData(String str) {
		LDataList<T> list = new LDataList<>();
		JsonArray array = LGlobals.json.parse(str).getAsJsonArray();
		for (JsonElement je : array) {
			list.add((T) LGlobals.gson.fromJson(je, getType()));
		}
		return (LDataList<T>) LGlobals.gson.fromJson(str, getType());
	}
	
	public abstract Class<?> getType();
	
}
