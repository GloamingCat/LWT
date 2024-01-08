package lwt.container;

import java.util.ArrayList;

import lwt.LMenuInterface;
import lwt.action.LActionStack;
import lwt.editor.LEditor;
import lwt.editor.LState;

import org.eclipse.swt.SWT;

public class LView extends LPanel {

	protected LView parent;
	protected LMenuInterface menuInterface;
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

	public void addChild(LView child) {
		if (child.parent != null) {
			parent.children.remove(child);
		}
		child.parent = this;
		if (child.getMenuInterface() == null) {
			child.setMenuInterface(menuInterface);
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
	
	public void createMenuInterface() {
		menuInterface = new LMenuInterface(this);
	}
	
	public void setMenuInterface(LMenuInterface mi) {
		this.menuInterface = mi;
		for(LView child : children) {
			child.setMenuInterface(mi);
		}
	}
	
	public LActionStack getActionStack() {
		LMenuInterface mi = getMenuInterface();
		if (mi == null)
			return null;
		return mi.actionStack;
	}
	
	public LMenuInterface getMenuInterface() {
		return menuInterface;
	}

	public void restart() {
		if (getActionStack().getRootView() == this)
			getActionStack().clear();
		restartChildren();
	}
	
	public void restartChildren() {
		for(LView child : children) {
			child.restart();
		}
	}

}
