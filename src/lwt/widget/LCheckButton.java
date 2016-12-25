package lwt.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class LCheckButton extends LControl<Boolean> {

	private Button button;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LCheckButton(Composite parent, int style) {
		super(parent, style);
		button = new Button(this, SWT.CHECK);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (button.getSelection() == currentValue)
					return;
				newModifyAction(currentValue, button.getSelection());
				currentValue = button.getSelection();
			}
		});
	}
	
	public void setValue(Object obj) {
		if (obj != null) {
			Boolean i = (Boolean) obj;
			button.setEnabled(true);
			button.setSelection(i);
			currentValue = i;
		} else {
			button.setEnabled(false);
			button.setSelection(false);
			currentValue = null;
		}
	}
	
	public void setText(String text) {
		button.setText(text);
	}

}
