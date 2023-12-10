package gson.project;

import java.lang.reflect.Type;

import lwt.LGlobals;
import lwt.dataserialization.LObjectSerializer;

public class GObjectSerializer<T> extends LObjectSerializer<T> {
	
	protected Type type;
	
	public GObjectSerializer(String path, Type type) {
		super(path + ".json");
		this.type = type;
	}

	@Override
	protected byte[] toByteArray(Object data) {
		return LGlobals.prettyGson.toJson(data, type).getBytes();
	}

	@Override
	protected T fromByteArray(byte[] bytes) {
		return LGlobals.prettyGson.fromJson(new String(bytes), type);
	}
	
	public Type getType() {
		return type;
	}

	@Override
	public void initialize() {
		data = LGlobals.prettyGson.fromJson("{}", type);
	}
	
}
