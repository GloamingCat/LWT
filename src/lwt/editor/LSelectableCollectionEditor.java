package lwt.editor;

import java.util.ArrayList;

import lwt.dataestructure.LPath;
import lwt.dialog.LObjectDialog;
import lwt.event.LSelectionEvent;
import lwt.event.listener.LSelectionListener;
import lwt.widget.LSelectableCollection;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Holds common functionalities for LTreeEditor and LListEditor.
 *
 */

public abstract class LSelectableCollectionEditor<T, ST> extends LCollectionEditor<T, ST> {

	public String fieldName = "";
	protected LObjectDialog<ST> editDialog = null;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LSelectableCollectionEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
	}
	public void addChild(LObjectEditor editor) {
		getCollectionWidget().addSelectionListener(new LSelectionListener() {
			@Override
			public void onSelect(LSelectionEvent e) {
				LPath path = getCollectionWidget().getSelectedPath();
				editor.setObject(getCollectionWidget().toObject(path), path);
			}
		});
		editor.collectionEditor = this;
		addChild((LEditor) editor);
	}
	
	@Override
	public LState getState() {
		final LPath currentPath = getCollectionWidget().getSelectedPath();
		final ArrayList<LState> states = getChildrenStates();
		return new LState() {
			@Override
			public void reset() {
				LSelectionEvent e = getCollectionWidget().select(currentPath);
				getCollectionWidget().notifySelectionListeners(e);
				resetStates(states);
			}
		};
	}
	
	public abstract LSelectableCollection<T, ST> getCollectionWidget();

}
