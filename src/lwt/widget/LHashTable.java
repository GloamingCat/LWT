package lwt.widget;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import lwt.LVocab;
import lwt.action.LHashDeleteAction;
import lwt.action.LHashEditAction;
import lwt.action.LHashInsertAction;
import lwt.dialog.LObjectDialog;
import lwt.dialog.LObjectShell;
import lwt.dialog.LShellFactory;
import lwt.dialog.LStringShell;
import lwt.event.LHashEditEvent;
import lwt.event.LHashKeyEvent;
import lwt.event.listener.LHashListener;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.custom.ScrolledComposite;

public class LHashTable<T> extends LWidget {
	
	protected ScrolledComposite scrolledComposite;
	protected Composite content;
	protected LObjectDialog<T> valueDialog;
	protected LObjectDialog<String> keyDialog;
	protected ArrayList<LHashListener<T>> editListeners = new ArrayList<>();
	protected ArrayList<LHashListener<T>> insertListeners = new ArrayList<>();
	protected ArrayList<LHashListener<T>> deleteListeners = new ArrayList<>();
	
	protected ArrayList<Entry<String, T>> entryList = new ArrayList<>();
	protected HashMap<String, Integer> keyMap = new HashMap<>();
	
	protected HashMap<String, Label> labelMap = new HashMap<>();

	public LHashTable(Composite parent, int style) {
		super(parent, style);
		
		LHashTable<T> self = this;
		valueDialog = new LObjectDialog<>(getShell(), getShell().getStyle());
		keyDialog = new LObjectDialog<>(getShell(), getShell().getStyle());
		keyDialog.setFactory(new LShellFactory<String>() {
			@Override
			public LObjectShell<String> createShell(Shell parent) {
				return new LStringShell(parent);
			}
		});
		
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		scrolledComposite = new ScrolledComposite(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		content = new Composite(scrolledComposite, SWT.NONE);
		content.setLayout(new GridLayout(3, false));
		
		scrolledComposite.setContent(content);
		
		Composite insert = new Composite(this, SWT.NONE);
		insert.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		GridLayout gl_insert = new GridLayout(1, false);
		gl_insert.marginWidth = 0;
		gl_insert.marginHeight = 0;
		insert.setLayout(gl_insert);
		
		Button btnInsert = new Button(insert, SWT.NONE);
		btnInsert.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String newKey = keyDialog.open("");
				if (newKey == null)
					return;
				if (labelMap.containsKey(newKey))
					return;
				T newValue = createNewValue();
				LHashKeyEvent<T> event = new LHashKeyEvent<T>(newKey, newValue);
				newAction(new LHashInsertAction<T>(self, event));
				insertKey(newKey, newValue);
				notifyInsertListeners(event);
			}
		});
		btnInsert.setBounds(0, 0, 75, 25);
		btnInsert.setText(LVocab.instance.INSERTNEW);
	}
	
	private void addKeyLabel(final String key, final T initialValue) {
		LHashTable<T> self = this;
		
		Composite buttons = new Composite(content, SWT.NONE);
		GridLayout gl_buttons = new GridLayout(2, true);
		gl_buttons.marginHeight = 0;
		gl_buttons.marginWidth = 0;
		buttons.setLayout(gl_buttons);
		
		Label lblKey = new Label(content, SWT.NONE);
		lblKey.setText(key + ":");
		
		Label lblValue = new Label(content, SWT.NONE);
		lblValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblValue.setText(initialValue.toString());
		
		Button btnEdit = new Button(buttons, SWT.NONE);
		btnEdit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnEdit.setSize(32, 25);
		btnEdit.setText(LVocab.instance.EDIT);
		btnEdit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				T oldValue = getValue(key);
				T newValue = valueDialog.open(oldValue);
				if (newValue != null) {
					LHashEditEvent<T> event = new LHashEditEvent<T>(key, oldValue, newValue);
					newAction(new LHashEditAction<T>(self, event));
					setValue(key, newValue);
					notifyEditListeners(event);
				}
			}
		});
		
		Button btnRemove = new Button(buttons, SWT.NONE);
		btnRemove.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnRemove.setSize(55, 25);
		btnRemove.setText(LVocab.instance.DELETE);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				T oldValue = getValue(key);
				LHashKeyEvent<T> event = new LHashKeyEvent<T>(key, oldValue);
				newAction(new LHashDeleteAction<T>(self, event));
				deleteKey(key);
				notifyDeleteListeners(event);
			}
		});
		
		labelMap.put(key, lblValue);
	}
	
	private T getValue(String key) {
		return entryList.get(keyMap.get(key)).getValue();
	}
	
	public void setMap(Map<String, T> map) {
		entryList.clear();
		keyMap.clear();
		int i = 0;
		for(Entry<String, T> e : map.entrySet()) {
			keyMap.put(e.getKey(), i++);
			entryList.add(e);
		}
		refreshAll();
	}
	
	public void setValue(String key, T value) {
		labelMap.get(key).setText(value.toString());
	}
	
	public void insertKey(String key, T initialValue) {
		keyMap.put(key, entryList.size());
		entryList.add(new SimpleEntry<String, T>(key, initialValue));
		addKeyLabel(key, initialValue);
		scrolledComposite.setContent(content);
		scrolledComposite.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		layout();
	}

	public void deleteKey(String key) {
		Integer i = keyMap.remove(key);
		entryList.remove((int) i);
		refreshAll();
	}
	
	public void refreshAll() {
		content.dispose();
		content = new Composite(scrolledComposite, SWT.NONE);
		content.setLayout(new GridLayout(3, false));
		content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		labelMap.clear();
		
		for(Entry<String, T> i : entryList) {
			addKeyLabel(i.getKey(), i.getValue());
		}
		
		scrolledComposite.setContent(content);
		scrolledComposite.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		layout();
	}
	
	public void setShellFactory(LShellFactory<T> factory) {
		valueDialog.setFactory(factory);
	}
	
	public void notifyEditListeners(LHashEditEvent<T> event) {
		System.out.println("lalala2");
		for(LHashListener<T> listener : editListeners) {
			listener.onEdit(event);
		}
	}
	
	public void notifyInsertListeners(LHashKeyEvent<T> event) {
		for(LHashListener<T> listener : insertListeners) {
			listener.onInsert(event);
		}
	}
	
	public void notifyDeleteListeners(LHashKeyEvent<T> event) {
		for(LHashListener<T> listener : deleteListeners) {
			listener.onDelete(event);
		}
	}
	
	public void addEditListener(LHashListener<T> listener) {
		editListeners.add(listener);
	}
	
	public void addInsertListener(LHashListener<T> listener) {
		insertListeners.add(listener);
	}
	
	public void addDeleteListener(LHashListener<T> listener) {
		deleteListeners.add(listener);
	}

	public T createNewValue() {
		return null;
	}
	
}
