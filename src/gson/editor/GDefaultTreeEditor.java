package gson.editor;

import java.lang.reflect.Type;

import lwt.LGlobals;
import lwt.container.LContainer;
import lwt.editor.LDefaultTreeEditor;

public abstract class GDefaultTreeEditor<T> extends LDefaultTreeEditor<T> {

	public GDefaultTreeEditor(LContainer parent) {
		super(parent);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T createNewElement() {
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
	public T duplicateElement(T original) {
		if (getType() != original.getClass())
			throw new ClassCastException("Object cannot be cast to " + getType().getTypeName());
		String json = LGlobals.gson.toJson(original, getType());
		return (T) LGlobals.gson.fromJson(json, getType());
	}
	
	@Override
	protected String encodeElement(T data) {
		return LGlobals.gson.toJson(data, getType());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected T decodeElement(String str) {
		return (T) LGlobals.gson.fromJson(str, getType());
	}

	@Override
	public boolean canDecode(String str) {
		return true;
	}
	
	public abstract Type getType();
	
}
