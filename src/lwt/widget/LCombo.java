package lwt.widget;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class LCombo extends LControl<Integer> {

	private Combo combo;
	private boolean includeID = true;
	private boolean optional = true;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LCombo(Composite parent, int style) {
		super(parent, style);
		combo = new Combo(this, SWT.BORDER | SWT.READ_ONLY);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (getSelectionIndex() == (Integer)currentValue)
					return;
				newModifyAction(currentValue, getSelectionIndex());
				currentValue = getSelectionIndex();
			}
		});
	}
	
	public int getSelectionIndex() {
		if (optional) {
			return combo.getSelectionIndex() - 1;
		} else {
			return combo.getSelectionIndex();
		}
	}
	
	public void setSelectionIndex(int i) {
		if (optional) {
			combo.select(i + 1);
		} else {
			combo.select(i);
		}
	}
	
	public void setValue(Object obj) {
		if (obj != null) {
			Integer i = (Integer) obj;
			combo.setEnabled(true);
			setSelectionIndex(i);
			currentValue = i;
		} else {
			combo.setEnabled(false);
			combo.clearSelection();
			currentValue = null;
		}
	}
	
	public void setItems(Object[] items) {
		ArrayList<Object> array = new ArrayList<>();
		for(Object item : items) {
			array.add(item);
		}
		setItems(array);
	}
	
	public void setItems(ArrayList<?> array) {
		int d = 0;
		String[] items;
		if (optional) {
			items = new String[array.size() + 1];
			items[0] = "";
			d = 1;
		} else {
			items = new String[array.size()];
		}
		int id = 0;
		for(Object obj : array) {
			String item = includeID ? String.format("[%03d]", id) : "";
			items[id + d] = item + obj.toString();
			id++;
		}
		combo.setItems(items);
	}
	
	public void setIncludeID(boolean value) {
		includeID = value;
	}
	
	public void setOptional(boolean value) {
		optional = value;
	}
	
}
