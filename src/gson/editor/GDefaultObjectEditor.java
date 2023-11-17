package gson.editor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lwt.LContainer;
import lwt.editor.LObjectEditor;

public class GDefaultObjectEditor<T> extends LObjectEditor<T> {
	
	protected static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	/**
	 * Horizontal fill layout.
	 * @param parent
	 * @param doubleBuffered
	 */
	public GDefaultObjectEditor(LContainer parent, boolean doubleBuffered) {
		super(parent, doubleBuffered);
	}
	
	/**
	 * Grid or fill layout.
	 * @param parent
	 * @param columns
	 * @param equalCols
	 * @param doubleBuffered
	 */
	public GDefaultObjectEditor(LContainer parent, int columns, boolean equalCols, boolean doubleBuffered) {
		super(parent, columns, equalCols, doubleBuffered);
	}
	
	/**
	 * Fill layout.
	 * @param parent
	 * @param horizontal
	 * @param doubleBuffered
	 */
	public GDefaultObjectEditor(LContainer parent, boolean horizontal, boolean doubleBuffered) {
		super(parent, horizontal, doubleBuffered);
	}

	@SuppressWarnings("unchecked")
	public T duplicateData(Object original) {
		T data = (T) original;
		String json = gson.toJson(data, data.getClass());
		return (T) gson.fromJson(json, data.getClass());
	}

}
