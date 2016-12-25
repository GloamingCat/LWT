package lwt.event;

public class LHashKeyEvent<T> {

	public String key;
	public T value;
	
	public LHashKeyEvent(String key, T value) {
		this.key = key;
		this.value = value;
	}
	
}
