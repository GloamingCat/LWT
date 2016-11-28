package lwt.widget;

import lwt.LVocab;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class LStringButton extends LObjectButton<String> {
	
	private Label label;
	private Text text;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LStringButton(Composite parent, int style) {
		super(parent, style);
		setText(LVocab.instance.SELECT);
	}
	
	public void setText(Text text) {
		this.text = text;
	}
	
	public void setLabel(Label label) {
		this.label = label;
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
