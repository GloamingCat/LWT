package lwt.widget;

import lwt.LGlobals;
import lwt.LVocab;
import lwt.container.LContainer;
import lwt.dialog.LShellFactory;

import java.lang.reflect.Type;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;

public abstract class LObjectButton<T> extends LControlWidget<T> {
	
	protected LShellFactory<T> shellFactory;
	private Button button;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LObjectButton(LContainer parent) {
		super(parent);
		setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				T newValue = shellFactory.openShell(getShell(), currentValue);
				if (newValue != null) {
					newModifyAction(currentValue, newValue);
					setValue(newValue);
				}
			}
		});
		button.setText(LVocab.instance.SELECT);
	}

	@Override
	protected void createContent(int flags) {
		button = new Button(this, SWT.NONE);
	}

	public void setShellFactory(LShellFactory<T> factory) {
		shellFactory = factory;
	}
	
	public void setText(String text) {
		button.setText(text);
	}
	
	@Override
	public void setValue(Object obj) {
		if (obj != null) {
			button.setEnabled(true);
			@SuppressWarnings("unchecked")
			T value = (T) obj;
			currentValue = value;
		} else {
			button.setEnabled(false);
			currentValue = null;
		}
	}
	
	@Override
	protected Control getControl() {
		return button;
	}

	@Override
	public String encodeData(T value) {
		return LGlobals.gson.toJson(value, value.getClass());
	}
	
	@Override
	public T decodeData(String str) {
		@SuppressWarnings("unchecked")
		T fromJson = (T) LGlobals.gson.fromJson(str, getType());
		return fromJson;
	}
	
	@Override
	public boolean canDecode(String str) {
		try {
			@SuppressWarnings("unchecked")
			T newValue = (T) LGlobals.gson.fromJson(str, getType());	
			return newValue != null;
		} catch (ClassCastException e) {
			System.err.println(e.getMessage());
			return false;
		}
	}
	
	protected abstract Type getType();

}
