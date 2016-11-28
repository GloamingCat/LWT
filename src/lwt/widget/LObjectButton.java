package lwt.widget;

import lwt.LVocab;
import lwt.action.LControlAction;
import lwt.dialog.LObjectDialog;
import lwt.dialog.LShellFactory;
import lwt.event.LControlEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionEvent;

public class LObjectButton<T> extends LControl {
	
	protected Button button;
	protected LObjectDialog<T> dialog;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LObjectButton(Composite parent, int style) {
		super(parent, style);
		LControl self = this;
		button = new Button(this, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				@SuppressWarnings("unchecked")
				T newValue = dialog.open((T) currentValue);
				if (newValue != null) {
					LControlEvent event = new LControlEvent(currentValue, newValue);
					newAction(new LControlAction(self, event));
					setValue(newValue);
					notifyListeners(event);
				}
			}
		});
		button.setText(LVocab.instance.SELECT);
	}
	
	public void setShellFactory(LShellFactory<T> factory) {
		LObjectDialog<T> dialog = new LObjectDialog<T>(getShell(), getShell().getStyle());
		dialog.setFactory(factory);
		this.dialog = dialog;
	}
	
	public void setText(String text) {
		button.setText(text);
	}

}
