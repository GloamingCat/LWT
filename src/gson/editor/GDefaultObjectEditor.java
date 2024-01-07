package gson.editor;

import java.lang.reflect.Type;

import com.google.gson.JsonParseException;

import lwt.LGlobals;
import lwt.container.LContainer;
import lwt.editor.LObjectEditor;

public abstract class GDefaultObjectEditor<T> extends LObjectEditor<T> {
	
	/**
	 * No layout.
	 * @param parent
	 * @param doubleBuffered
	 */
	public GDefaultObjectEditor(LContainer parent, boolean doubleBuffered) {
		super(parent, doubleBuffered);
	}

	@SuppressWarnings("unchecked")
	public T duplicateData(Object original) {
		T data = (T) original;
		String json = LGlobals.gson.toJson(data, data.getClass());
		return (T) LGlobals.gson.fromJson(json, data.getClass());
	}
	
	@Override
	public String encodeData(T data) {
		return LGlobals.gson.toJson(data, getType());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T decodeData(String str) {
		try {
			return (T) LGlobals.gson.fromJson(str, getType());
		} catch(JsonParseException e) {
			return null;
		}
	}
	
	@Override
	public boolean canDecode(String str) {
		return true;
	}
	
	public abstract Type getType();

}
