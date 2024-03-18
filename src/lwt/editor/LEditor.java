package lwt.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

import lbase.LVocab;
import lbase.gui.LPastable;
import lwt.container.LContainer;
import lwt.container.LView;
import lwt.widget.LWidget;

public abstract class LEditor extends LView implements LPastable {

	//////////////////////////////////////////////////
	// {{ Constructors
	
	/**
	 * No layout.
	 * @param parent
	 * @param doubleBuffered
	 */
	public LEditor(LContainer parent, boolean doubleBuffered) {
		super(parent, doubleBuffered);
	}

	// }}
	
	//////////////////////////////////////////////////
	// {{ Object
	
	public abstract void setObject(Object object);
	public abstract void saveObjectValues();

	// }}
	
	//////////////////////////////////////////////////
	// {{ Menu
	
	private void addHeaderButtons(Composite parent) {
		Button copyButton = new Button(parent, SWT.NONE);
		copyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				onCopyButton(null);
			}
		});
		copyButton.setText(LVocab.instance.COPY);
		Button pasteButton = new Button(parent, SWT.NONE);
		pasteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				onPasteButton(null);
			}
		});
		pasteButton.setText(LVocab.instance.PASTE);
	}
	
	private LPopupMenu addMenu(Composite parent) {
		LPopupMenu menu = new LPopupMenu(parent);
		parent.setMenu(menu);
		setCopyEnabled(menu, true);
		setPasteEnabled(menu, true);
		addFocusOnClick(parent);
		return menu;
	}
	
	private void addFocusOnClick(Composite c) {
		LEditor editor = this;
		c.setEnabled(true);
		c.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				if (e.button == 1) { // Left button
					getMenuInterface().setFocusEditor(editor);
				}
			}
		});
	}
	
	public void addMenu() {
		addMenu(getContentComposite());
	}
	
	public void addMenu(LContainer frame) {
		Composite c = frame.getContentComposite();
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
			addFocusOnClick(widget);
		} else if (widget.getMenu() == null) {
			widget.setMenu(menu);
			addFocusOnClick(widget);
		}
	}
	
	public void addHeader(LContainer parent) {
		if (parent == null)
			parent = this;
		Composite header = new Composite(parent.getContentComposite(), 0); 
		header.setLayout(new RowLayout());
		addHeaderButtons(header);
	}
	
	public void setCopyEnabled(lbase.gui.LMenu menu, boolean value) {
		menu.setMenuButton(value, LVocab.instance.COPY, "copy", (d) -> onCopyButton(menu), "Ctrl+&C");
	}

	public void setPasteEnabled(lbase.gui.LMenu menu, boolean value) {
		menu.setMenuButton(value, LVocab.instance.PASTE, "paste", (d) -> onPasteButton(menu), "Ctrl+&V");
	}
	
	// }}
	
}
