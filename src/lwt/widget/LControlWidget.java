package lwt.widget;

import java.util.ArrayList;

import lwt.LGlobals;
import lwt.action.LControlAction;
import lwt.container.LContainer;
import lwt.event.LControlEvent;
import lwt.event.listener.LControlListener;

import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public abstract class LControlWidget<T> extends LWidget implements LControl<T> {
	
	protected ArrayList<LControlListener<T>> modifyListeners = new ArrayList<>();
	protected T currentValue;
	
	public LControlWidget(LContainer parent) {
		super(parent);
	}
	
	public LControlWidget(LContainer parent, int flags) {
		super(parent.getComposite(), flags);
	}
	
	public void modify(T newValue) {
		T oldValue = currentValue;
		setValue(newValue);
		newModifyAction(oldValue, newValue);
	}
	
	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		currentValue = (T) value;
	}
	
	public T getValue() {
		return currentValue;
	}
	
	public boolean isEnabled() {
		return currentValue != null;
	}
	
	public void setEnabled(boolean value) {}
	
	protected Control getControl() {
		return this;
	}
	
	@Override
	public void setHoverText(String text) {
		getControl().setToolTipText(text);
	}
	
	@Override
	public void setMenu(Menu menu) {
		super.setMenu(menu);
		getControl().setMenu(menu);
	}

	
	//-------------------------------------------------------------------------------------
	// Modify Events
	//-------------------------------------------------------------------------------------

	protected void newModifyAction(T oldValue, T newValue) {
		LControlEvent<T> event = new LControlEvent<T>(oldValue, newValue);
		newAction(new LControlAction<T>(this, event));
		notifyListeners(event);
	}
	
	public LControlEvent<T> createEvent() {
		LControlEvent<T> e = new LControlEvent<T>(null, currentValue);
		e.detail = -1;
		return e;
	}
	
	public void addModifyListener(LControlListener<T> listener) {
		modifyListeners.add(listener);
	}
	
	public void removeModifyListener(LControlListener<T> listener) {
		modifyListeners.remove(listener);
	}
	
	public void notifyListeners(LControlEvent<T> event) {
		for(LControlListener<T> listener : modifyListeners) {
			listener.onModify(event);
		}
	}
	
	public void notifyEmpty() {
		LControlEvent<T> e = createEvent();
		notifyListeners(e);
	}
	
	//-------------------------------------------------------------------------------------
	// Copy / Paste
	//-------------------------------------------------------------------------------------
	
	public void onCopyButton(Menu menu) {
		String str = encodeData(currentValue);
		LGlobals.clipboard.setContents(new Object[] { str },
				new Transfer[] { TextTransfer.getInstance() });
	}
	
	public void onPasteButton(Menu menu) {
		String str = (String) LGlobals.clipboard.getContents(TextTransfer.getInstance());
		if (str == null)
			return;
		try {
			T newValue = decodeData(str);
			if (newValue != null && !newValue.equals(currentValue))
				modify(newValue);	
		} catch (ClassCastException e) {
			System.err.println(e.getMessage());
			return;
		}
	}
	
	protected abstract String encodeData(T value);
	protected abstract T decodeData(String str);
	
	public boolean canDecode(String str) {
		try {
			decodeData(str);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

}
