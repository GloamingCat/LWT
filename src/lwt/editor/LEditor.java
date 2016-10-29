package lwt.editor;

import java.util.ArrayList;

import lwt.action.LActionStack;

import org.eclipse.swt.widgets.Composite;

/**
 * This class is a generic node from an editor tree.
 * The state of this editor depends on its children's state.
 * It is associated to an Action Stack. If it's the root 
 * editor, it must create a new stack and handle the undo/redo
 * shortcuts.
 * 
 * @author Luisa
 *
 */

public abstract class LEditor extends Composite {

	protected LEditor parent;
	protected LActionStack actionStack;
	
	protected ArrayList<LEditor> children = new ArrayList<>();
	protected ArrayList<LObjectEditor> objectEditors = new ArrayList<>();
	protected ArrayList<LCollectionEditor> collectionEditors = new ArrayList<>();
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LEditor(Composite parent, int style) {
		super(parent, style);

	}
	
	public void addSubEditor(LEditor editor) {
		addChild(editor);
	}
	
	public void addSubEditor(LObjectEditor editor) {
		addChild(editor);
		objectEditors.add(editor);
	}
	
	public void addSubEditor(LCollectionEditor editor) {
		addChild(editor);
		collectionEditors.add(editor);
	}
	
	private void addChild(LEditor editor) {
		if (editor.parent != null) {
			parent.children.remove(editor);
		}
		editor.parent = this;
		children.add(editor);
	}
	
	public void onVisible() {
		for(LEditor child : children) {
			child.onVisible();
		}
	}
	
	public LState getState() {
		final ArrayList<LState> states = getChildrenStates();
		return new LState() {
			@Override
			public void reset() {
				resetStates(states);
			}
		};
	}
	
	protected ArrayList<LState> getChildrenStates() {
		ArrayList<LState> list = new ArrayList<>();
		for(LEditor child : children) {
			list.add(child.getState());
		}
		return list;
	}
	
	protected void resetStates(ArrayList<LState> list) {
		for(LState state : list) {
			state.reset();
		}
	}

}
