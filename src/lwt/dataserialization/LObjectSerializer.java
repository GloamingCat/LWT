package lwt.dataserialization;

public abstract class LObjectSerializer extends LDefaultSerializer {

	protected Object data;
	
	public LObjectSerializer(String path, Class<?> type) {
		super(path, type);
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
	
	protected byte[] serialize() {
		return toByteArray(data);
	}
	
	protected void deserialize(byte[] bytes) {
		data = fromByteArray(bytes);
	}
	
	protected abstract byte[] toByteArray(Object obj);
	protected abstract Object fromByteArray(byte[] bytes);

}
