package lwt.widget;

import lwt.LVocab;
import lwt.action.LControlAction;
import lwt.dialog.LObjectDialog;
import lwt.dialog.LShellFactory;
import lwt.event.LControlEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

public class LStringButton extends LControl {
	
	private Button button;
	private LObjectDialog<String> dialog;
	
	private Label label;
	private Text text;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LStringButton(Composite parent, int style) {
		super(parent, style);
		LControl self = this;
		button = new Button(this, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String newValue = dialog.open((String) currentValue);
				if (newValue != null) {
					LControlEvent event = new LControlEvent(currentValue, newValue);
					newAction(new LControlAction(self, event));
					notifyListeners(event);
					setValue(newValue);
				}
			}
		});
		button.setText(LVocab.instance.SELECT);
	}
	
	public void setText(Text text) {
		this.text = text;
	}
	
	public void setLabel(Label label) {
		this.label = label;
	}
	
	public void setShellFactory(LShellFactory<String> factory) {
		LObjectDialog<String> dialog = new LObjectDialog<String>(getShell(), getShell().getStyle());
		dialog.setFactory(factory);
		this.dialog = dialog;
	}

	@Override
	public void setValue(Object value) {
		if (value != null) {
			button.setEnabled(true);
			String s = (String) value;
			if (label != null) {
				label.setImage(SWTResourceManager.getImage(getImagePath() + s));
			}
			if (text != null) {
				text.setText(s);
			}
		} else {
			button.setEnabled(false);
			if (label != null) {
				label.setImage(null);
			}
			if (text != null) {
				text.setText("");
			}
		}
		currentValue = value;
	}
	
	protected String getImagePath() {
		return "";
	}

}
