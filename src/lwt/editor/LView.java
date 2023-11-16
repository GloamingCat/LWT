package lwt.editor;

import java.util.ArrayList;

import lwt.action.LActionStack;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * This class is a generic node from an editor tree.
 * The state of this editor depends on its children's state.
 * It is associated to an Action Stack. If it's the root 
 * editor, it must create a new stack and handle the undo/redo
 * shortcuts.
 *
 */

public abstract class LView extends Composite {

	protected LView parent;
	protected LActionStack actionStack;
	protected boolean isActionRoot = false;
	
	protected ArrayList<LView> children = new ArrayList<>();
	protected ArrayList<LEditor> subEditors = new ArrayList<>();
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LView(Composite parent) {
		super(parent, SWT.NONE);
	}
	
	public LView(Composite parent, int style) {
		super(parent, style);
	}
	
	public void addChild(LView child) {
		if (child.parent != null) {
			parent.children.remove(child);
		}
		child.parent = this;
		if (child.getActionStack() == null) {
			child.setActionStack(actionStack);
		}
		children.add(child);
	}
	
	public void addChild(LEditor editor) {
		addChild((LView) editor);
		subEditors.add(editor);
	}

	public void removeChild(LView child) {
		if (child.parent != this)
			return;
		child.parent = null;
		children.remove(child);
	}
	
	public void removeChild(LEditor editor) {
		removeChild((LView) editor);
		subEditors.remove(editor);
	}
	
	public void onVisible() {
		try {
			onChildVisible();
		} catch(Exception e) {
			System.out.println(this.getClass());
			throw e;
		}
	}
	
	public void onChildVisible() {
		for(LView child : children) {
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
		for(LView child : children) {
			list.add(child.getState());
		}
		return list;
	}
	
	protected void resetStates(ArrayList<LState> list) {
		for(LState state : list) {
			state.reset();
		}
	}
	
	public void createActionStack() {
		actionStack = new LActionStack(this);
		isActionRoot = true;
	}
	
	public void setActionStack(LActionStack stack) {
		this.actionStack = stack;
		isActionRoot = false;
		for(LView child : children) {
			child.setActionStack(stack);
		}
	}
	
	public LActionStack getActionStack() {
		return actionStack;
	}
	
	public void undo() {
		actionStack.undo();
	}
	
	public void redo() {
		actionStack.redo();
	}

	public boolean canUndo() {
		return actionStack.canUndo();
	}

	public boolean canRedo() {
		return actionStack.canRedo();
	}
	
	public void restart() {
		if (isActionRoot)
			actionStack.clear();
		restartChildren();
	}
	
	public void restartChildren() {
		for(LView child : children) {
			child.restart();
		}
	}

}
