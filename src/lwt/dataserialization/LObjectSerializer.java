package lwt.dataserialization;

public class LObjectSerializer<T> implements LSerializer {

	protected String fileName;
	protected Class<T> type;
	protected T data;
	
	public LObjectSerializer(String fileName, Class<T> type) {
		this.fileName = fileName;
		this.type = type;
	}

	@Override
	public boolean save() {
		try {
			String jsonString = toString();
			LFileManager.save(fileName + ".json", jsonString);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean load() {
		try {
			String jsonString = LFileManager.load(fileName + ".json");
			fromString(jsonString);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String toString() {
		return gson.toJson(data, type);
	}
	
	public void fromString(String jsonString) {
		data = gson.fromJson(jsonString, type);
	}

}
