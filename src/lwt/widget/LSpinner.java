package lwt.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class LSpinner extends LControl<Integer> {

	private Spinner spinner;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LSpinner(Composite parent, int style) {
		super(parent, style);
		spinner = new Spinner(this, SWT.BORDER);
		spinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (spinner.getSelection() == currentValue)
					return;
				newModifyAction(currentValue, spinner.getSelection());
				currentValue = spinner.getSelection();
			}
		});
	}
	
	public void setValue(Object obj) {
		if (obj != null) {
			Integer i = (Integer) obj;
			spinner.setEnabled(true);
			spinner.setSelection(i);
			currentValue = i;
		} else {
			spinner.setEnabled(false);
			spinner.setSelection(0);
			currentValue = null;
		}
	}
	
	public void setMinimum(int i) {
		spinner.setMinimum(i);
	}
	
	public void setMaximum(int i) {
		spinner.setMaximum(i);
	}

}
