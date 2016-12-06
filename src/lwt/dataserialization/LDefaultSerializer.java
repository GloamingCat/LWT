package lwt.dataserialization;

import java.io.File;

public abstract class LDefaultSerializer implements LSerializer {

	protected String path;
	protected Class<?> type;
	
	public LDefaultSerializer(String path, Class<?> type) {
		this.path = path;
		this.type = type;
	}
	
	public Class<?> getType() {
		return type;
	}
	
	@Override
	public boolean isDataFolder(String path) {
		File folder = new File(path);
		for(File entry : folder.listFiles()) {
			if (entry.isFile() && entry.getPath().equals(this.path)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean save() {
		try {
			byte[] bytes = serialize();
			LFileManager.save(path, bytes);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean load() {
		try {
			byte[] bytes = LFileManager.load(path);
			deserialize(bytes);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	protected abstract byte[] serialize();
	protected abstract void deserialize(byte[] bytes);
	
}