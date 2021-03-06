package lwt.widget;

import java.util.ArrayList;

import lwt.LImageHelper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

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
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		combo = new Combo(this, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_combo = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		if (!LImageHelper.onWindows)
			gd_combo.heightHint = 28;
		combo.setLayoutData(gd_combo);
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
	
	public LCombo(Composite parent) {
		this(parent, SWT.NONE);
	}

	public int getSelectionIndex() {
		if (optional) {
			return combo.getSelectionIndex() - 1;
		} else {
			return combo.getSelectionIndex();
		}
	}
	
	public void setSelectionIndex(int i) {
		if (optional) 
			i++;
		if (i >= combo.getItemCount())
			combo.select(0);
		else
			combo.select(i);
	}
	
	public void setItem(int i, Object obj) {
		if (optional)
			i++;
		combo.setItem(i, obj.toString());
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
		if (items != null) {
			for(Object item : items) {
				array.add(item);
			}
		}
		setItems(array);
	}
	
	public void setItems(ArrayList<?> array) {
		if (array == null)
			array = new ArrayList<Object>();
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
			String item = includeID ? String.format("[%03d] ", id) : "";
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
