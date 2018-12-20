package gson.project;

import java.lang.reflect.Type;

import lwt.dataserialization.LObjectSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GObjectSerializer<T> extends LObjectSerializer<T> {
	
	protected static Gson gson = new GsonBuilder().
			setPrettyPrinting().
			disableHtmlEscaping().
			create();
	protected Type type;
	
	public GObjectSerializer(String path, Type type) {
		super(path + ".json");
		this.type = type;
	}

	@Override
	protected byte[] toByteArray(Object data) {
		return gson.toJson(data, type).getBytes();
	}

	@Override
	protected T fromByteArray(byte[] bytes) {
		return gson.fromJson(new String(bytes), type);
	}
	
	public Type getType() {
		return type;
	}

	@Override
	public void initialize() {
		data = gson.fromJson("{}", type);
	}
	
}
