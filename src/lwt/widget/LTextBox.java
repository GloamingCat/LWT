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
import org.eclipse.swt.widgets.Menu;

import lwt.LFlags;
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
	
	//////////////////////////////////////////////////
	// {{ Layout

	public GridData initGridData() {
		Object ld = getLayoutData();
		if (ld != null) {
			return (GridData) ld;
		} else {
			GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
			setLayoutData(gd);
			return gd;
		}
	}
	
	public void setGridData(int ax, int ay, boolean grabx, boolean graby, int cols, int rows) {
		setLayoutData(new GridData(ax, ay, grabx, graby, cols, rows));
	}
	
	public void setSpread(int cols, int rows) {
		GridData gridData = (GridData) initGridData();
		gridData.horizontalSpan = cols;
		gridData.verticalSpan = rows;
	}
	
	public void setAlignment(int a) {
		int h = SWT.FILL;
		int v = SWT.FILL;
		if ((a & LFlags.LEFT) > 0)
			h = SWT.LEFT;
		if ((a & LFlags.RIGHT) > 0)
			h = SWT.RIGHT;
		if ((a & LFlags.MIDDLE) > 0)
			h = SWT.CENTER;
		if ((a & LFlags.TOP) > 0)
			v = SWT.TOP;
		if ((a & LFlags.BOTTOM) > 0)
			v = SWT.BOTTOM;
		if ((a & LFlags.CENTER) > 0)
			v = SWT.CENTER;
		GridData gridData = initGridData();
		gridData.horizontalAlignment = h;
		gridData.verticalAlignment = v;
	}

	public void setExpand(boolean h, boolean v) {
		initGridData();
		GridData gridData = initGridData();
		gridData.grabExcessHorizontalSpace = h;
		gridData.grabExcessVerticalSpace = v;
	}
	
	public void setMinimumWidth(int w) {
		GridData gridData = initGridData();
		gridData.minimumWidth = w;
		gridData.widthHint = w;
	}
	
	public void setMinimumHeight(int h) {
		GridData gridData = initGridData();
		gridData.minimumHeight = h;
		gridData.heightHint = h;
	}
	
	// }}
	
	@Override
	public void setMenu(Menu menu) {
		super.setMenu(menu);
		text.setMenu(menu);
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
