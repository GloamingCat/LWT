package lwt.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import lwt.container.LContainer;

public class LTextBox extends LControlWidget<String> {
	
	private StyledText text;
	
	public LTextBox(LContainer parent) {
		this(parent, false);
	}
	
	public LTextBox(LContainer parent, int cols, int rows) {
		this(parent, false, cols, rows);
	}
	
	public LTextBox(LContainer parent, boolean readOnly, int cols, int rows) {
		this(parent, readOnly);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, cols, rows));
	}

	public LTextBox(LContainer parent, boolean readOnly) {
		super(parent, readOnly ? SWT.READ_ONLY : SWT.NONE);
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				updateCurrentText();
			}
		});
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.keyCode == 13) {
					updateCurrentText();
				}
			}
		});
		text.addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				updateCurrentText();
			}
		});
		text.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent e) {
				updateCurrentText();
			}
		});
	}

	@Override
	protected void createContent(int flags) {
		text = new StyledText(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.WRAP | flags);
	}
	
	public void updateCurrentText() {
		if (!text.getText().equals(currentValue)) {
			newModifyAction(currentValue, text.getText());
			currentValue = text.getText();
		}
	}
	
	@Override
	public void setValue(Object value) {
		if (value != null) {
			String s = (String) value;
			text.setEnabled(true);
			text.setText(s);
			currentValue = s;
		} else {
			text.setEnabled(false);
			text.setText("");
			currentValue = null;
		}
	}
	
	@Override
	protected Control getControl() {
		return text;
	}

	@Override
	public String encodeData(String value) {
		return value;
	}
	
	@Override
	public String decodeData(String str) {
		return str;
	}
	
	@Override
	public boolean canDecode(String str) {
		return true;
	}
	
}
