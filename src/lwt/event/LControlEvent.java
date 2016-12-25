package lwt.event;

public class LControlEvent<T> {
	
	public T oldValue;
	public T newValue;
	public int detail;
	
	public LControlEvent(T oldValue, T newValue) {
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
}