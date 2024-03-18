package lwt.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

import lbase.gui.LMenu;
import lwt.container.LContainer;

public class LActionButton extends LControlWidget<Object> {

	private Button button;
	
	public LActionButton(LContainer parent, String text) {
		super(parent);
		setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		button.setText(text);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				notifyEmpty();
			}
		});
	}

	@Override
	protected void createContent(int flags) {
		button = new Button(this, SWT.NONE);
	}

	@Override
	public void onCopyButton(LMenu menu) {}
	
	@Override
	public void onPasteButton(LMenu menu) {}
	
	@Override
	protected Control getControl() {
		return button;
	}

	@Override
	public String encodeData(Object value) {
		return null;
	}
	
	@Override
	public Object decodeData(String str) {
		return null;
	}

}
