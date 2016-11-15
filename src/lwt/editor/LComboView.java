package lwt.editor;

import java.util.ArrayList;

import lwt.widget.LCombo;
import lwt.widget.LControl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public abstract class LComboView extends LControlView {

	protected LCombo combo;
	
	public LComboView(Composite parent, int style) {
		super(parent, style);
		combo = new LCombo(this, SWT.NONE);
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
	
	public LControl getControl() {
		return combo;
	}
	
	protected abstract ArrayList<?> getArray();
	
}
