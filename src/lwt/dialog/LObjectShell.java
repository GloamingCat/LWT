package lwt.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class LObjectShell<T> extends Shell {

	protected Composite content;
	protected T result;

	/**
	 * Create the shell.
	 * @param display
	 */
	public LObjectShell(Display display, final T initial) {
		super(display, SWT.SHELL_TRIM);
		
		setSize(450, 300);
		setText(getText());
		setLayout(new GridLayout(1, false));
		
		content = new Composite(this, SWT.NONE);
		content.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite buttons = new Composite(this, SWT.NONE);
		buttons.setLayout(new GridLayout(2, true));
		buttons.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnOk = new Button(buttons, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				result = createResult(initial);
				close();
			}
		});
		btnOk.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnOk.setText("OK");
		
		Button btnCancel = new Button(buttons, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				result = null;
				close();
			}
		});
		btnCancel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnCancel.setText("Cancel");
	}
	
	public T getResult() {
		return result;
	}
	
	protected abstract T createResult(T initial);

}
