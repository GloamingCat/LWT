package lwt.widget;

import lwt.action.LControlAction;
import lwt.event.LControlEvent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class LText extends LControl {
	
	private Text text;
	private Object currentValue;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LText(Composite parent, int style) {
		super(parent, style);
		LText self = this;
		text = new Text(this, SWT.BORDER);
		
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				LControlEvent event = new LControlEvent(currentValue, text.getText());
				newAction(new LControlAction(self, event));
				notifyListeners(event);
				currentValue = event.newValue;
			}
		});
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.keyCode == 13) {
					LControlEvent event = new LControlEvent(currentValue, text.getText());
					newAction(new LControlAction(self, event));
					notifyListeners(event);
					currentValue = event.newValue;
				}
			}
		});
	}
	
	@Override
	public void setValue(Object value) {
		String s = (String) value;
		text.setText(s);
		currentValue = s;
	}

}
