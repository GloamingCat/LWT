package lwt.event;

public class LControlEvent {
	public Object oldValue;
	public Object newValue;
	public int detail;
	
	public LControlEvent (Object oldValue, Object newValue) {
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
}