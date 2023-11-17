package lwt.editor;

import java.util.Map;

import lwt.LContainer;
import lwt.action.LActionStack;
import lwt.event.LHashEditEvent;
import lwt.event.LHashKeyEvent;
import lwt.event.listener.LHashListener;
import lwt.widget.LHashTable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;

public class LHashTableEditor<T> extends LEditor {

	protected LHashTable<T> table;
	protected Map<String, T> map;
	
	/**
	 * Horizontal fill layout.
	 * @param parent
	 * @param doubleBuffered
	 */
	public LHashTableEditor(LContainer parent) {
		super(parent, false);
		LHashTableEditor<T> self = this;
		setLayout(new FillLayout(SWT.HORIZONTAL));
		table = new LHashTable<T>(this) {
			public T createNewValue() {
				return self.createNewValue();
			}
		};
		table.addEditListener(new LHashListener<T> () {
			@Override
			public void onEdit(LHashEditEvent<T> event) {
				map.put(event.key, event.newValue);
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
	
	/**
	 * Grid or fill layout.
	 * @param parent
	 * @param columns
	 * @param equalCols
	 * @param doubleBuffered
	 */
	public LHashTableEditor(LContainer parent, int columns, boolean equalCols) {
		super(parent, columns, equalCols, false);
	}
	
	/**
	 * Fill layout.
	 * @param parent
	 * @param horizontal
	 * @param doubleBuffered
	 */
	public LHashTableEditor(LContainer parent, boolean horizontal) {
		super(parent, horizontal, false);
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

	@Override
	public void saveObjectValues() {
		// TODO Auto-generated method stub
		
	}

}
