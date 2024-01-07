package lwt.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Menu;

import lwt.container.LContainer;
import lwt.event.listener.LSelectionListener;

public class LButton extends LWidget {

	public LSelectionListener onClick;
	protected Button button;
	
	public LButton(LContainer parent, String text) {
		super(parent);
		setLayout(new FillLayout());
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				execute();
			}
		});
		button.setText(text);
	}

	@Override
	protected void createContent(int flags) {
		button = new Button(this, SWT.NONE);
	}
	
	protected void execute() {
		if (onClick != null) {
			onClick.onSelect(null);
		}
	}
	
	public void setHoverText(String text) {
		button.setToolTipText(text);
	}
	
	@Override
	public void onCopyButton(Menu menu) {}
	
	@Override
	public void onPasteButton(Menu menu) {}

	@Override
	public boolean canDecode(String str) {
		return false;
	}

	@Override
	protected void checkSubclass() { }

}
