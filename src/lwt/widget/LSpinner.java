package lwt.widget;

import lwt.container.*;
import lwt.graphics.LTexture;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

public class LSpinner extends LControlWidget<Integer> {

	private Spinner spinner;

	/**
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter parent new LPanel(new lwt.dialog.LShell(400, 200), 2, true)
	 */
	public LSpinner(LContainer parent) {
		this(parent, 1);
	}
	
	public LSpinner(LContainer parent, int columns) {
		super(parent);
		setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, columns, 1));
	}
	
	@Override
	protected void createContent(int flags) {
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		setLayout(gridLayout);
		spinner = new Spinner(this, SWT.BORDER);
		GridData gd_spinner = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		if (!LTexture.onWindows)
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
	
	@Override
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
	
	@Override
	protected Control getControl() {
		return spinner;
	}

	@Override
	public String encodeData(Integer value) {
		return value + "";
	}
	
	@Override
	public Integer decodeData(String str) {
		return Integer.parseInt(str);
	}

}
