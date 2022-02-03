package lwt.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class LActionButton extends LControl<Object> {

	private Button button;
	
	public LActionButton(Composite parent, String text) {
		super(parent, 0);
		button = new Button(this, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				notifyEmpty();
			}
		});
	}


}
