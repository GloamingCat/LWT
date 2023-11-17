package lwt.editor;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;

import lwt.LContainer;
import lwt.LScrollPanel;
import lwt.dataestructure.LDataList;
import lwt.event.LControlEvent;
import lwt.event.listener.LControlListener;
import lwt.widget.LControlWidget;
import lwt.widget.LLabel;

public abstract class LGridForm<T> extends LObjectEditor<LDataList<T>> {

	protected LDataList<T> values;
	protected ArrayList<LControlWidget<T>> controls;
	protected LScrollPanel scrollComp;
	protected LPanel content;
	
	public LGridForm(LContainer parent, int columns) {
		super(parent, true, false);
		controls = new ArrayList<>();
		scrollComp = new LScrollPanel(this, true, false);
		content = new LPanel(scrollComp, columns * 2, false);
		scrollComp.setContent(content);
	}
	
	public void setObject(Object obj) {
		super.setObject(obj);
		if (obj != null) {
			for(int i = 0; i < controls.size(); i++) {
				if (i < currentObject.size()) {
					controls.get(i).setValue(currentObject.get(i));
				} else {
					T value = getDefaultValue();
					currentObject.add(value);
					controls.get(i).setValue(value);
				}
			}
		} else {
			for(LControlWidget<T> control : controls) {
				control.setValue(null);
				control.setEnabled(false);
			}
		}
	}
	
	public void saveObjectValues() {
		super.saveObjectValues();
		for(int i = 0; i < controls.size(); i++) {
			if (i < currentObject.size()) {
				currentObject.set(i, controls.get(i).getValue());
			} else {
				T value = getDefaultValue();
				currentObject.add(value);
				controls.get(i).setValue(value);
			}
		}
	}

	public void onVisible() {
		Control[] children = content.getChildren();
		ArrayList<Object> data = getList();
		// Update children
		children = content.getChildren();
		controls.clear();
		for (int i = 0; i < children.length / 2; i++)	{
			LLabel label = (LLabel) children[i * 2];
			label.setText(getLabelText(i, data.get(i)));
			@SuppressWarnings("unchecked")
			LControlWidget<T> control = (LControlWidget<T>) children[i * 2 + 1];
			controls.add(control);
		}
		// Add missing controls for exceeding attributes
		for(int i = children.length / 2; i < data.size(); i ++) {
			new LLabel(content, "");
			LControlWidget<T> control = createControl(i, data.get(i));
			final int k = i;
			control.addModifyListener(new LControlListener<T>() {
				@Override
				public void onModify(LControlEvent<T> event) {
					if (currentObject != null) {
						currentObject.set(k, event.newValue);
					}
				}
			});
			control.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			control.setActionStack(getActionStack());
			controls.add(control);
		}
		// Remove exceeding controls
		for (int i = data.size() * 2; i < children.length; i++) {
			children[i].dispose();
		}
		scrollComp.setContent(content);
		scrollComp.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	protected abstract T getDefaultValue();
	protected abstract LDataList<Object> getList();
	protected abstract LControlWidget<T> createControl(final int i, final Object obj);
	protected abstract String getLabelText(final int i, final Object obj);
	
}
