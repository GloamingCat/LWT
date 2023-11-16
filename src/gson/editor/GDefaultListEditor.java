package gson.editor;

import org.eclipse.swt.widgets.Composite;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lwt.editor.LDefaultListEditor;

public abstract class GDefaultListEditor<T> extends LDefaultListEditor<T> {

	protected static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public GDefaultListEditor(Composite parent) {
		super(parent, 0);
	}
	
	public GDefaultListEditor(Composite parent, int style) {
		super(parent, style);
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
	public T duplicateData(Object original) {
		if (!getType().isInstance(original))
			throw new ClassCastException("Object cannot be cast to " + getType().getTypeName());
		String json = gson.toJson(original, original.getClass());
		return (T) gson.fromJson(json, getType());
	}
	
	public abstract Class<?> getType();

}
