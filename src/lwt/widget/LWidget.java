package lwt.widget;

import lwt.container.LContainer;
import lwt.container.LPanel;
import lwt.editor.LPopupMenu;
import lwt.LMenuInterface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;

import lbase.LVocab;
import lbase.action.LAction;
import lbase.gui.LPastable;

public abstract class LWidget extends LPanel implements LPastable {

	protected LMenuInterface menuInterface;

	public LWidget(LContainer parent, int style) {
		super(parent);
		setFillLayout(true);
		parent.getContentComposite().setTabList(null);
		createContent(style);
	}

	/**
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter parent new lwt.container.LPanel(new LShell(800, 600), true)
	 */
	public LWidget(LContainer parent) {
		this(parent, SWT.NONE);
	}
	
	protected abstract void createContent(int flags);

	public void setMenuInterface(LMenuInterface mi) {
		menuInterface = mi;
	}

	public void newAction(LAction action) {
		if (menuInterface != null) {
			menuInterface.actionStack.newAction(action);
		}
	}
	
	public void setHoverText(String text) {
		super.setToolTipText(text);
	}
	
	//////////////////////////////////////////////////
	// {{ Menu
	
	private LPopupMenu addMenu(Composite parent) {
		LPopupMenu menu = new LPopupMenu(parent);
		setCopyEnabled(menu, true);
		setPasteEnabled(menu, true);
		addFocusOnClick(parent);
		return menu;
	}
	
	private void addFocusOnClick(Composite c) {
		LWidget widget = this;
		c.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				if (e.button == 1) { // Left button
					System.out.println(widget);
					menuInterface.setFocusWidget(widget);
				}
			}
		});
	}
	
	public void addMenu() {
		addMenu(this);
	}
	
	public void addMenu(LContainer frame) {
		Composite c = frame.getContentComposite();
		LPopupMenu menu = (LPopupMenu) getMenu();
		if (menu == null) {
			menu = addMenu(c);
			setMenu(menu);
			addFocusOnClick(this);
		} else if (c.getMenu() == null) {
			c.setMenu(menu);
			addFocusOnClick(c);
		}
	}
	
	public void addMenu(LWidget widget) {
		LPopupMenu menu = (LPopupMenu) getMenu();
		if (menu == null) {
			menu = addMenu((Composite) widget);
			setMenu(menu);
			addFocusOnClick(this);
		} else if (widget.getMenu() == null) {
			widget.setMenu(menu);
			addFocusOnClick(widget);
		}
	}

	public void setCopyEnabled(lbase.gui.LMenu menu, boolean value) {
		menu.setMenuButton(value, LVocab.instance.COPY, "copy", (d) -> onCopyButton(menu), "Ctrl+&C");
	}

	public void setPasteEnabled(lbase.gui.LMenu menu, boolean value) {
		menu.setMenuButton(value, LVocab.instance.PASTE, "paste", (d) -> onPasteButton(menu), "Ctrl+&V");
	}
	
	// }}

}
