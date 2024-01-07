package lwt.widget;

import lwt.LVocab;
import lwt.action.LAction;
import lwt.container.LContainer;
import lwt.container.LPanel;
import lwt.LMenuInterface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

public abstract class LWidget extends LPanel {

	protected LMenuInterface menuInterface;

	public LWidget(LContainer parent, int style) {
		super(parent);
		setFillLayout(true);
		parent.getComposite().setTabList(null);
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
	
	private Menu addMenu(Composite parent) {
		Menu menu = new Menu(parent);
		parent.setMenu(menu);
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
		Composite c = frame.getComposite();
		Menu menu = getMenu();
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
		Menu menu = getMenu();
		if (menu == null) {
			menu = addMenu((Composite) widget);
			setMenu(menu);
			addFocusOnClick(this);
		} else if (widget.getMenu() == null) {
			widget.setMenu(menu);
			addFocusOnClick(widget);
		}
	}

	public void setCopyEnabled(Menu menu, boolean value) {
		String str = toString();
		LMenuInterface.setMenuButton(menu, value, LVocab.instance.COPY, "copy", new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.out.println("Copied from " + str);
				onCopyButton(menu);
			}
		}, 'C');
	}

	public void setPasteEnabled(Menu menu, boolean value) {
		String str = toString();
		LMenuInterface.setMenuButton(menu, value, LVocab.instance.PASTE, "paste", new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.out.println("Pasted on " + str);
				onPasteButton(menu);
			}
		}, 'V');
	}

	public abstract void onCopyButton(Menu menu);
	public abstract void onPasteButton(Menu menu);
	public abstract boolean canDecode(String str);
	
	// }}

}
