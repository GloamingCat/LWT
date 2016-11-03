package lwt.dataserialization;

import java.io.File;

public abstract class LObjectSerializer<T> implements LSerializer {

	protected String path;
	protected Class<T> type;
	protected T data;
	
	public LObjectSerializer(String path, Class<T> type) {
		this.path = path;
		this.type = type;
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
			byte[] bytes = toByteArray();
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
			fromByteArray(bytes);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	protected abstract byte[] toByteArray();
	protected abstract void fromByteArray(byte[] bytes);

}
