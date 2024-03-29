package lwt.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wb.swt.SWTResourceManager;

import lwt.container.LContainer;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;

public class LCheckBox extends LControlWidget<Boolean> {

	private Button button;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LCheckBox(LContainer parent) {
		this(parent, 1);
	}
	
	public LCheckBox(LContainer parent, int columns) {
		super(parent);
		if (columns > 1)
			setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, columns, 1));
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
	
	@Override
	protected void createContent(int flags) {
		button = new Button(this, SWT.CHECK);
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
	
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		button.setForeground(SWTResourceManager.getColor(enabled ? SWT.COLOR_BLACK : SWT.COLOR_DARK_GRAY));
	}
	
	@Override
	protected Control getControl() {
		return button;
	}

	@Override
	public String encodeData(Boolean value) {
		return value + "";
	}
	
	@Override
	public Boolean decodeData(String str) {
		return Boolean.parseBoolean(str);
	}

}
