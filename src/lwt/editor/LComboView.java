package lwt.editor;

import java.util.ArrayList;

import lwt.LContainer;
import lwt.widget.LCombo;
import lwt.widget.LControlWidget;

public class LComboView extends LControlView<Integer> {

	protected LCombo combo;
	
	public LComboView(LContainer parent) {
		super(parent);
		combo = new LCombo(this);
	}
	
	public void setIncludeID(boolean value) {
		combo.setIncludeID(value);
	}
	
	public void setOptional(boolean value) {
		combo.setOptional(value);
	}
	
	public void onVisible() {
		combo.setItems(getArray());
		super.onVisible();
	}
	
	public LControlWidget<Integer> getControl() {
		return combo;
	}
	
	protected ArrayList<?> getArray() { return new ArrayList<Object>(); }
	
}
