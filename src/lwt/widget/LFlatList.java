package lwt.widget;

import java.lang.reflect.Type;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.List;

import lwt.container.LContainer;

public class LFlatList extends LControlWidget<Integer> {

	protected List list;
	protected boolean optional;
	
	public LFlatList(LContainer parent, boolean optional) {
		super(parent);
		this.optional = optional;
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int current = currentValue == null ? -1 : currentValue;
				if (list.getSelectionIndex() == current)
					return;
				newModifyAction(currentValue, list.getSelectionIndex());
				currentValue = list.getSelectionIndex();
			}
		});
		if (optional)
			currentValue = -1;
		else
			currentValue = 0;
	}

	@Override
	protected void createContent(int flags) {
		list = new List(getParent(), SWT.BORDER | SWT.V_SCROLL);
	}
	
	public String getSelectedText() {
		return list.getSelection()[0];
	}
	
	public void setValue(Object obj) {
		if (obj != null) {
			currentValue = (Integer) obj;
			list.setEnabled(true);
			list.select(currentValue);
		} else {
			list.setEnabled(false);
			list.deselectAll();
			currentValue = null;
		}
	}
	
	public void setItems(Object[] items) {
		if (items == null) {
			list.setItems();
			return;
		}
		int off = optional ? 1 : 0;
		String[] strs = new String[items.length + off];
		for (int i = 0; i < items.length; i++) {
			strs[i + off] = items[i] == null ? "NULL" : items[i].toString();
		}
		list.setItems(strs);
	}
	
	public void setItems(String[] items) {
		list.setItems(items);
	}
	
	public int indexOf(String item) {
		int i = 0;
		if (item == null) {
			System.out.println("Null item");
			return -1;
		}
		for(String s : list.getItems()) {
			if (item.equals(s)) {
				return i;
			}
			i++;
		}
		return -1;
	}

	@Override
	protected Type getType() {
		return Integer.class;
	}

}
