package lwt.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import lwt.LContainer;

public class LActionButton extends LControlWidget<Object> {

	private Button button;
	
	public LActionButton(LContainer parent, String text) {
		super(parent);
		button = new Button(this, SWT.NONE);
		button.setText(text);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				notifyEmpty();
			}
		});
	}

}
