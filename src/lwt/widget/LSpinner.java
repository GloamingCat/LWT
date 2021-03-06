package lwt.widget;

import lwt.LImageHelper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class LSpinner extends LControl<Integer> {

	private Spinner spinner;
	
	public LSpinner(Composite parent) {
		this(parent, SWT.NONE);
	}
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LSpinner(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		setLayout(gridLayout);
		spinner = new Spinner(this, SWT.BORDER);
		GridData gd_spinner = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		if (!LImageHelper.onWindows)
			gd_spinner.heightHint = 26;
		spinner.setLayoutData(gd_spinner);
		spinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (currentValue != null && spinner.getSelection() == currentValue)
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
