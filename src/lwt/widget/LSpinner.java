package lwt.widget;

import lwt.action.LControlAction;
import lwt.event.LControlEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class LSpinner extends LControl {

	private Spinner spinner;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LSpinner(Composite parent, int style) {
		super(parent, style);
		LSpinner self = this;
		spinner = new Spinner(this, SWT.BORDER);
		spinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (spinner.getSelection() == (Integer)currentValue)
					return;
				LControlEvent event = new LControlEvent(currentValue, spinner.getSelection());
				newAction(new LControlAction(self, event));
				notifyListeners(event);
				currentValue = event.newValue;
			}
		});
	}
	
	public void setValue(Object obj) {
		Integer i = (Integer) obj;
		spinner.setSelection(i);
		currentValue = i;
	}

}
