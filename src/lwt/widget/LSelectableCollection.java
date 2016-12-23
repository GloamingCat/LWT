package lwt.widget;

import java.util.ArrayList;

import lwt.dataestructure.LPath;
import lwt.event.LSelectionEvent;
import lwt.event.listener.LSelectionListener;

import org.eclipse.swt.widgets.Composite;

public abstract class LSelectableCollection<T, ST> extends LCollection<T, ST> {

	public LSelectableCollection(Composite parent, int style) {
		super(parent, style);
	}
	
	protected ArrayList<LSelectionListener> selectionListeners = new ArrayList<>();
	public void addSelectionListener(LSelectionListener listener) {
		selectionListeners.add(listener);
	}
	
	public void notifySelectionListeners(LSelectionEvent event) {
		for(LSelectionListener listener : selectionListeners) {
			listener.onSelect(event);
		}
	}
	
	//-------------------------------------------------------------------------------------
	// Selection
	//-------------------------------------------------------------------------------------
	
	public LSelectionEvent select(LPath parent, int index) {
		if (parent == null) {
			parent = new LPath(index);
		} else {
			parent = parent.addLast(index);
		}
		return select(parent);
	}
	
	public abstract LSelectionEvent select(LPath path);
	
	public abstract T getSelectedObject();
	
	public abstract LPath getSelectedPath();
	
}
