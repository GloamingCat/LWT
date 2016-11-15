package lwt.widget;

import lwt.action.LControlAction;
import lwt.event.LControlEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class LCheckButton extends LControl {

	private Button button;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LCheckButton(Composite parent, int style) {
		super(parent, style);
		LCheckButton self = this;
		button = new Button(this, SWT.CHECK);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (button.getSelection() == (Boolean)currentValue)
					return;
				LControlEvent event = new LControlEvent(currentValue, button.getSelection());
				newAction(new LControlAction(self, event));
				notifyListeners(event);
				currentValue = event.newValue;
			}
		});
	}
	
	public void setValue(Object obj) {
		if (obj != null) {
			Boolean i = (Boolean) obj;
			button.setEnabled(true);
			button.setSelection(i);
		} else {
			button.setEnabled(false);
			button.setSelection(false);
		}
		currentValue = obj;
	}
	
	public void setText(String text) {
		button.setText(text);
	}

}
