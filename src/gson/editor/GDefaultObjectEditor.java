package gson.editor;

import org.eclipse.swt.widgets.Composite;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lwt.editor.LObjectEditor;

public class GDefaultObjectEditor<T> extends LObjectEditor<T> {
	
	protected static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public GDefaultObjectEditor(Composite parent) {
		super(parent, 0);
	}
	
	public GDefaultObjectEditor(Composite parent, int style) {
		super(parent, style);
	}

	@SuppressWarnings("unchecked")
	public T duplicateData(Object original) {
		T data = (T) original;
		String json = gson.toJson(data, data.getClass());
		return (T) gson.fromJson(json, data.getClass());
	}

}
