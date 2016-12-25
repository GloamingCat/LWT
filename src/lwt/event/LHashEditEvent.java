package lwt.event;

public class LHashEditEvent<T> {

	public String key;
	public T oldValue;
	public T newValue;
	
	public LHashEditEvent(String key, T oldValue, T newValue) {
		this.key = key;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
}
