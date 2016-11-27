package lwt.dialog;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public abstract class LObjectShell<T> extends Shell {

	protected Composite content;
	protected T result = null;
	protected T initial = null;
	
	public LObjectShell(Shell parent) {
		super(parent, parent.getStyle());
		
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

	public void open(T initial) {
		this.result = null;
		this.initial = initial;
		open();
	}
	
	public T getResult() {
		return result;
	}
	
	protected String[] getItems(ArrayList<?> array) {
		String[] items = new String[array.size()];
		int id = 0;
		for(Object obj : array) {
			String item = String.format("[%03d]", id);
			items[id] = item + obj.toString();
			id++;
		}
		return items;
	}
	
	protected abstract T createResult(T initial);
	
	protected void checkSubclass() { }

}
