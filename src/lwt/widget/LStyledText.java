package lwt.widget;

import lwt.action.LControlAction;
import lwt.event.LControlEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;

public class LStyledText extends LControl {
	
	private StyledText text;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LStyledText(Composite parent, int style) {
		super(parent, style);
		text = new StyledText(this, SWT.BORDER);
		
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				onTextModify();
			}
		});
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.keyCode == 13) {
					onTextModify();
				}
			}
		});
	}
	
	private void onTextModify() {
		if (!text.getText().equals(currentValue)) {
			LControlEvent event = new LControlEvent(currentValue, text.getText());
			newAction(new LControlAction(this, event));
			notifyListeners(event);
			currentValue = event.newValue;
		}
	}
	
	@Override
	public void setValue(Object value) {
		if (value != null) {
			String s = (String) value;
			text.setEnabled(true);
			text.setText(s);
		} else {
			text.setEnabled(false);
			text.setText("");
		}
		currentValue = value;
	}

}
