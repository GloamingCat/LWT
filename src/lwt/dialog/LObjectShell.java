package lwt.dialog;

import java.util.ArrayList;

import lwt.LVocab;
import lwt.editor.LPanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public abstract class LObjectShell<T> extends LShell {

	protected LPanel content;
	protected T result = null;
	protected T initial = null;
	
	public LObjectShell(LShell parent) {
		super(parent);
		
		setText(getText());
		setLayout(new GridLayout(1, false));
		
		content = new LPanel(this);
		content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite buttons = new Composite(this, SWT.NONE);
		buttons.setLayout(new GridLayout(2, true));
		buttons.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		
		Button btnOk = new Button(buttons, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				result = createResult(initial);
				if (initial.equals(result))
					result = null;
				close();
			}
		});
		btnOk.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnOk.setText(LVocab.instance.OK);
		
		Button btnCancel = new Button(buttons, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				result = null;
				close();
			}
		});
		btnCancel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnCancel.setText(LVocab.instance.CANCEL);
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
			String item = String.format("[%03d] ", id);
			items[id] = item + obj.toString();
			id++;
		}
		return items;
	}
	
	protected abstract T createResult(T initial);
	
	protected void checkSubclass() { }

}
