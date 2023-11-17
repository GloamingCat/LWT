package gson.editor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lwt.LContainer;
import lwt.dataestructure.LDataList;
import lwt.editor.LGridForm;

public abstract class GGridForm<T> extends LGridForm<T> {

	protected static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public GGridForm(LContainer parent, int columns) {
		super(parent, columns);
	}

	@Override
	@SuppressWarnings("unchecked")
	public LDataList<T> duplicateData(Object obj) {
		LDataList<T> copy = new LDataList<>();		
		if (!copy.getClass().isInstance(obj))
			throw new ClassCastException("Object cannot be cast to " + copy.getClass().getTypeName());
		LDataList<T> list = (LDataList<T>) obj;
		for (T original : list) {
			String json = gson.toJson(original, original.getClass());
			copy.add((T) gson.fromJson(json, original.getClass()));
		}
		return copy;
	}
	

}
