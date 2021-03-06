package lwt.dialog;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import lwt.LVocab;
import lwt.dialog.LObjectShell;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class LStringShell extends LObjectShell<String> {
	
	private Text txtName;

	public LStringShell(Shell parent) {
		super(parent);
		
		content.setLayout(new GridLayout(2, false));
		
		Label lblName = new Label(content, SWT.NONE);
		lblName.setText(LVocab.instance.TEXT);
		
		txtName = new Text(content, SWT.BORDER);
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		pack();
	}
	
	public void open(String initial) {
		super.open(initial);
		txtName.setText(initial);
	}

	@Override
	protected String createResult(String initial) {
		if (txtName.getText().equals(initial)) {
			return null;
		} else {
			return txtName.getText();
		}
	}

}
