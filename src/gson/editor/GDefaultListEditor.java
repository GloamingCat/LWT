package gson.editor;

import java.lang.reflect.Type;

import lwt.LGlobals;
import lwt.container.LContainer;
import lwt.editor.LDefaultListEditor;

public abstract class GDefaultListEditor<T> extends LDefaultListEditor<T> {

	public GDefaultListEditor(LContainer parent) {
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
			return (T) LGlobals.gson.fromJson("{}", getType());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T duplicateData(Object original) {
		if (getType() != original)
			throw new ClassCastException("Object cannot be cast to " + getType().getTypeName());
		String json = LGlobals.gson.toJson(original, original.getClass());
		return (T) LGlobals.gson.fromJson(json, getType());
	}
	
	@Override
	protected String encodeData(Object data) {
		return LGlobals.gson.toJson(data, getType());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected T decodeData(String str) {
		return (T) LGlobals.gson.fromJson(str, getType());
	}
	
	public abstract Type getType();

}
