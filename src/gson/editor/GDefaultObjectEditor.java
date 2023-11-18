package gson.editor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lwt.container.LContainer;
import lwt.editor.LObjectEditor;

public class GDefaultObjectEditor<T> extends LObjectEditor<T> {
	
	protected static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	/**
	 * No layout.
	 * @param parent
	 * @param doubleBuffered
	 */
	public GDefaultObjectEditor(LContainer parent, boolean doubleBuffered) {
		super(parent, doubleBuffered);
	}

	/**
	 * Fill/row layout.
	 * @param parent
	 * @param horizontal
	 * @param equalCells
	 * @param doubleBuffered
	 */
	public GDefaultObjectEditor(LContainer parent, boolean horizontal, boolean equalCells, boolean doubleBuffered) {
		super(parent, horizontal, equalCells, doubleBuffered);
	}
	
	/**
	 * Fill layout with no margin.
	 * @param parent
	 * @param horizontal
	 * @param doubleBuffered
	 */
	public GDefaultObjectEditor(LContainer parent, boolean horizontal, boolean doubleBuffered) {
		super(parent, horizontal, doubleBuffered);
	}
	
	/**
	 * Grid layout.
	 * @param parent
	 * @param columns
	 * @param equalCols
	 * @param doubleBuffered
	 */
	public GDefaultObjectEditor(LContainer parent, int columns, boolean equalCols, boolean doubleBuffered) {
		super(parent, columns, equalCols, doubleBuffered);
	}

	@SuppressWarnings("unchecked")
	public T duplicateData(Object original) {
		T data = (T) original;
		String json = gson.toJson(data, data.getClass());
		return (T) gson.fromJson(json, data.getClass());
	}

}
