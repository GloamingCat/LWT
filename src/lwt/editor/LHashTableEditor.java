package lwt.editor;

import java.util.Map;

import lwt.action.LActionStack;
import lwt.event.LHashEditEvent;
import lwt.event.LHashKeyEvent;
import lwt.event.listener.LHashListener;
import lwt.widget.LHashTable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;

public class LHashTableEditor<T> extends LEditor {

	protected LHashTable<T> table;
	protected Map<String, T> map;
	
	public LHashTableEditor(Composite parent, int style) {
		super(parent, style);

		LHashTableEditor<T> self = this;
		setLayout(new FillLayout(SWT.HORIZONTAL));
		table = new LHashTable<T>(this, SWT.NONE) {
			public T createNewValue() {
				return self.createNewValue();
			}
		};
		table.addEditListener(new LHashListener<T> () {
			@Override
			public void onEdit(LHashEditEvent<T> event) {
				map.put(event.key, event.newValue);
				System.out.println("lalala");
			}
		});
		table.addInsertListener(new LHashListener<T> () {
			@Override
			public void onInsert(LHashKeyEvent<T> event) {
				map.put(event.key, event.value);
			}
		});
		table.addDeleteListener(new LHashListener<T> () {
			@Override
			public void onDelete(LHashKeyEvent<T> event) {
				map.remove(event.key);
			}
		});
	}
	
	public void setActionStack(LActionStack stack) {
		super.setActionStack(stack);
		table.setActionStack(stack);
	}
	
	@SuppressWarnings("unchecked")
	public void setObject(Object obj) {
		if (getFieldName() != null)
			obj = this.getFieldValue(obj, getFieldName());
		map = (Map<String, T>) obj;
		table.setMap(map);
	}
	
	public LHashTable<T> getTable() {
		return table;
	}

	public T createNewValue() { return null; }
	public String getFieldName() { return null; }

}
