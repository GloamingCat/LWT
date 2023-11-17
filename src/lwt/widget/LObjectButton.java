package lwt.widget;

import lwt.LContainer;
import lwt.LVocab;
import lwt.dialog.LObjectDialog;
import lwt.dialog.LShellFactory;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionEvent;

public class LObjectButton<T> extends LControlWidget<T> {
	
	public String name = "";
	protected Button button;
	protected LObjectDialog<T> dialog;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LObjectButton(LContainer parent) {
		super(parent);
		dialog = new LObjectDialog<T>(getShell(), getShell().getStyle());
		button = new Button(this, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				dialog.setText(name);
				T newValue = dialog.open(currentValue);
				if (newValue != null) {
					newModifyAction(currentValue, newValue);
					setValue(newValue);
				}
			}
		});
		button.setText(LVocab.instance.SELECT);
	}

	public void setShellFactory(LShellFactory<T> factory) {
		dialog.setFactory(factory);
	}
	
	public void setText(String text) {
		button.setText(text);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) {
		if (value != null) {
			button.setEnabled(true);
			currentValue = (T) value;
		} else {
			button.setEnabled(false);
			currentValue = null;
		}
	}

}
