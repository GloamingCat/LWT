package lwt.container;

import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

public class LToolBar extends LPanel {
	
	private ToolBar toolBar;
	
	public LToolBar(LContainer parent) {
		super(parent);
		toolBar = new ToolBar(this, SWT.FLAT | SWT.RIGHT);
		setFillLayout(true);
	}
	
	private <T> ToolItem addItem(int type, Consumer<T> onSelect, T data, String txt) {
		ToolItem item = new ToolItem(toolBar, type);
		if (onSelect != null) {
			item.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					onSelect.accept(data);
				}
			});
		}
		Image img = SWTResourceManager.getImage(txt);
		if (img == null)
			item.setText(txt);
		else
			item.setImage(img);
		return item;
	}
	
	public <T> void addItem(Consumer<T> onSelect, T data, String imagePath, boolean selected) {
		addItem(SWT.RADIO, onSelect, data, imagePath).setSelection(selected);
	}
	
	public void addCheckItem(Consumer<Boolean> onSelect, String imagePath, boolean selected) {
		ToolItem item = addItem(SWT.CHECK, null, null, imagePath);
		item.setSelection(selected);
		item.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				onSelect.accept(item.getSelection());
			}
		});
	}
	
	public <T> void addButtonItem(Consumer<T> onSelect, T data, String imagePath) {
		addItem(SWT.NONE, onSelect, data, imagePath);
	}
	
	public void addSeparator() {
		new ToolItem(toolBar, SWT.SEPARATOR);
	}

}
