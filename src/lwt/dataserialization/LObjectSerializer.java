package lwt.dataserialization;

public abstract class LObjectSerializer<T> extends LDefaultSerializer {

	protected T data;
	
	public LObjectSerializer(String path, Class<?> type) {
		super(path, type);
	}
	
	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
	
	protected byte[] serialize() {
		return toByteArray(data);
	}
	
	protected void deserialize(byte[] bytes) {
		data = fromByteArray(bytes);
	}
	
	protected abstract byte[] toByteArray(T obj);
	protected abstract T fromByteArray(byte[] bytes);

}
