package lwt.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import lwt.container.LContainer;
import lwt.graphics.LTexture;

public class LToggleButton extends LControlWidget<Boolean> {

	private Label icon;
	private LTexture imgTrue;
	private LTexture imgFalse;
	private boolean enabled = true;
	
	public LToggleButton(LContainer parent) {
		super(parent);
		icon.setAlignment(SWT.CENTER);
		icon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				newModifyAction(currentValue, !currentValue);
				if (enabled) {
					setValue(!currentValue);
				}
			}
		});
	}
	
	public LToggleButton(LContainer parent, String imgTrue, String imgFalse) {
		this(parent);
		this.imgFalse = new LTexture(imgFalse);
		this.imgTrue = new LTexture(imgTrue);
		icon.setImage(this.imgFalse.convert());
	}

	@Override
	protected void createContent(int flags) {
		icon = new Label(this, SWT.NONE);
	}
	
	public void setImages(LTexture imgTrue, LTexture imgFalse) {
		this.imgFalse = imgFalse;
		this.imgTrue = imgTrue;
	}

	public void setValue(Object obj) {
		if (obj != null) {
			enabled = true;
			Boolean i = (Boolean) obj;
			icon.setImage(i ? imgTrue.convert() : imgFalse.convert());
			currentValue = i;
		} else {
			enabled = false;
			icon.setImage(imgFalse.convert());
			currentValue = null;
		}
	}
	
	@Override
	protected Control getControl() {
		return icon;
	}

	@Override
	public String encodeData(Boolean value) {
		return value + "";
	}
	
	@Override
	public Boolean decodeData(String str) {
		return Boolean.parseBoolean(str);
	}

}
