package lwt.widget;

import lwt.container.LContainer;
import lwt.dialog.LWindowFactory;

import java.lang.reflect.Type;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

import gson.GGlobals;
import lbase.LVocab;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;

public abstract class LObjectButton<T> extends LControlWidget<T> {
	
	protected LWindowFactory<T> shellFactory;
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
				T newValue = shellFactory.openWindow(getWindow(), currentValue);
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

	public void setShellFactory(LWindowFactory<T> factory) {
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
		return GGlobals.gson.toJson(value, value.getClass());
	}
	
	@Override
	public T decodeData(String str) {
		@SuppressWarnings("unchecked")
		T fromJson = (T) GGlobals.gson.fromJson(str, getType());
		return fromJson;
	}
	
	@Override
	public boolean canDecode(String str) {
		try {
			@SuppressWarnings("unchecked")
			T newValue = (T) GGlobals.gson.fromJson(str, getType());	
			return newValue != null;
		} catch (ClassCastException e) {
			System.err.println(e.getMessage());
			return false;
		}
	}
	
	protected abstract Type getType();

}
