package lwt.editor;

import java.util.ArrayList;

import lwt.action.LActionStack;
import lwt.dataestructure.LPath;
import lwt.event.LSelectionEvent;
import lwt.event.listener.LSelectionListener;
import lwt.widget.LMenuCollection;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Holds common functionalities for LTreeEditor and LListEditor.
 * 
 * @author Luisa
 *
 */

public abstract class LCollectionEditor<T, ST> extends LEditor {
	
	public LMenuCollection<T, ST> collection;
	public String fieldName = "";
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LCollectionEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
	}
	
	public void setEditEnabled(boolean value) {
		collection.setEditEnabled(value);
	}
	
	public void setInsertNewEnabled(boolean value) {
		collection.setInsertNewEnabled(value);
	}
	
	public void setDuplicateEnabled(boolean value) {
		collection.setDuplicateEnabled(value);
	}
	
	public void setDeleteEnabled(boolean value) {
		collection.setDeleteEnabled(value);
	}
	
	public void setDragEnabled(boolean value) {
		collection.setDragEnabled(value);
	}
	
	public void addChild(LObjectEditor editor) {
		collection.addSelectionListener(new LSelectionListener() {
			@Override
			public void onSelect(LSelectionEvent e) {
				editor.setObject(collection.getSelectedObject());
			}
		});
		editor.collectionEditor = this;
		addChild((LEditor) editor);
	}

	public void setCollection(LMenuCollection<T, ST> collection) {
		this.collection = collection;
		collection.setActionStack(actionStack);
	}
	
	@Override
	public void onVisible() {
		collection.refresh();
	}

	@Override
	public LState getState() {
		final LPath currentPath = collection.getSelectedPath();
		final ArrayList<LState> states = getChildrenStates();
		return new LState() {
			@Override
			public void reset() {
				LSelectionEvent e = collection.select(currentPath);
				collection.notifySelectionListeners(e);
				resetStates(states);
			}
		};
	}
	
	public void setActionStack(LActionStack stack) {
		super.setActionStack(stack);
		collection.setActionStack(stack);
	}
	
}
