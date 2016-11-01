package lwt.action;

import java.util.ArrayList;

import lwt.editor.LView;
import lwt.editor.LState;

public class LActionStack {

	private static class Node {
		public LAction action;
		public LState state;
		public Node(LAction action, LState state) {
			this.action = action;
			this.state = state;
		}
	}
	
	private int savedAction = 0;
	private int lastAction = 0;
	private ArrayList<Node> actions = new ArrayList<>();
	private LView rootEditor;
	
	public LActionStack(LView rootEditor) {
		this.rootEditor = rootEditor;
		LActionManager.getInstance().addStack(this);
	}
	
	public void newAction(LAction action) {
		while (lastAction < actions.size()) {
			actions.remove(lastAction);
			savedAction = -1;
		}
		actions.add(new Node(action, rootEditor.getState()));
		lastAction++;
	}
	
	public void undo() {
		if (lastAction > 0) {
			lastAction--;
			actions.get(lastAction).state.reset();
			actions.get(lastAction).action.undo();
			System.out.println("lalal2");
		}
	}
	
	public void redo() {
		if (lastAction < actions.size()) {
			actions.get(lastAction).state.reset();
			actions.get(lastAction).action.redo();
			lastAction++;
		}
	}
	
	public boolean canUndo() {
		return lastAction > 0;
	}
	
	public boolean canRedo() {
		return lastAction < actions.size();
	}
	
	public void onSave() {
		savedAction = lastAction;
	}
	
	public boolean hasChanges() {
		return savedAction != lastAction;
	}
	
}
