package lwt.widget;

import java.lang.reflect.Type;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import lwt.container.LContainer;

public class LToggleButton extends LControlWidget<Boolean> {

	private Label icon;
	private Image imgTrue;
	private Image imgFalse;
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
		this.imgFalse = SWTResourceManager.getImage(LToggleButton.class, imgFalse);
		this.imgTrue = SWTResourceManager.getImage(LToggleButton.class, imgTrue);
		icon.setImage(this.imgFalse);
	}

	@Override
	protected void createContent(int flags) {
		icon = new Label(this, SWT.NONE);
	}
	
	public void setImages(Image imgTrue, Image imgFalse) {
		this.imgFalse = imgFalse;
		this.imgTrue = imgTrue;
	}

	public void setValue(Object obj) {
		if (obj != null) {
			enabled = true;
			Boolean i = (Boolean) obj;
			icon.setImage(i ? imgTrue : imgFalse);
			currentValue = i;
		} else {
			enabled = false;
			icon.setImage(imgFalse);
			currentValue = null;
		}
	}

	@Override
	protected Type getType() {
		return Boolean.class;
	}

}
