package gson.editor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lwt.container.LContainer;
import lwt.editor.LDefaultTreeEditor;

public abstract class GDefaultTreeEditor<T> extends LDefaultTreeEditor<T> {

	protected static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public GDefaultTreeEditor(LContainer parent) {
		super(parent);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T createNewData() {
		if (getType() == String.class) {
			return (T) "";
		} else if (getType() == Integer.class) {
			return (T) (Integer) 0;
		} else {
			return (T) gson.fromJson("{}", getType());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T duplicateData(T original) {
		if (!getType().isInstance(original))
			throw new ClassCastException("Object cannot be cast to " + getType().getTypeName());
		String json = gson.toJson(original, getType());
		return (T) gson.fromJson(json, getType());
	}
	
	public abstract Class<?> getType();
	
}
