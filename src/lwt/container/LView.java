package lwt.container;

import java.util.ArrayList;

import lwt.action.LActionStack;
import lwt.editor.LEditor;
import lwt.editor.LState;

import org.eclipse.swt.SWT;

public abstract class LView extends LPanel {

	protected LView parent;
	protected LActionStack actionStack;
	protected boolean isActionRoot = false;
	boolean doubleBuffered = false;
	
	protected ArrayList<LView> children = new ArrayList<>();
	protected ArrayList<LEditor> subEditors = new ArrayList<>();
	
	/**
	 * No layout.
	 * @param parent
	 * @param doubleBuffered
	 */
	public LView(LContainer parent, boolean doubleBuffered) {
		super(parent.getComposite(),  SWT.NONE);
		this.doubleBuffered = doubleBuffered;
	}

	/**
	 * Fill/row layout.
	 * @param parent
	 * @param horizontal
	 * @param equalCells
	 * @param doubleBuffered
	 */
	public LView(LContainer parent, boolean horizontal, boolean equalCells, boolean doubleBuffered) {
		super(parent.getComposite(), horizontal, equalCells, SWT.NONE);
		this.doubleBuffered = doubleBuffered;
	}
	
	/**
	 * Fill layout with no margin.
	 * @param parent
	 * @param horizontal
	 * @param doubleBuffered
	 */
	public LView(LContainer parent, boolean horizontal, boolean doubleBuffered) {
		super(parent.getComposite(), horizontal, SWT.NONE);
		this.doubleBuffered = doubleBuffered;
	}
	
	/**
	 * Grid layout.
	 * @param parent
	 * @param columns
	 * @param equalCols
	 * @param doubleBuffered
	 */
	public LView(LContainer parent, int columns, boolean equalCols, boolean doubleBuffered) {
		super(parent.getComposite(), columns, equalCols,  SWT.NONE);
		this.doubleBuffered = doubleBuffered;
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
	
	public void layout(boolean changed) {
		if (doubleBuffered)
			setRedraw(false);
		super.layout(changed);
		if (doubleBuffered)
			setRedraw(true);
		
	}
	
	public void onVisible() {
		if (doubleBuffered)
			setRedraw(false);
		try {
			onChildVisible();
		} catch(Exception e) {
			System.out.println(this.getClass());
			throw e;
		}
		if (doubleBuffered)
			setRedraw(true);
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
