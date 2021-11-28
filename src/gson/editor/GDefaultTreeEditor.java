package gson.editor;

import java.lang.reflect.Type;

import org.eclipse.swt.widgets.Composite;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lwt.editor.LDefaultTreeEditor;

public abstract class GDefaultTreeEditor<T> extends LDefaultTreeEditor<T> {

	protected static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public GDefaultTreeEditor(Composite parent, int style) {
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
	public T duplicateData(T original) {
		String json = gson.toJson(original, getType());
		return (T) gson.fromJson(json, getType());
	}
	
	public abstract Type getType();
	
}
