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

import lwt.LMenuInterface;
import lwt.LVocab;
import lwt.container.LContainer;
import lwt.container.LFrame;
import lwt.container.LView;
import lwt.widget.LWidget;

public abstract class LEditor extends LView {

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

	/**
	 * Fill/row layout.
	 * @param parent
	 * @param horizontal
	 * @param equalCells
	 * @param doubleBuffered
	 */
	public LEditor(LContainer parent, boolean horizontal, boolean equalCells, boolean doubleBuffered) {
		super(parent, horizontal, equalCells, doubleBuffered);
	}
	
	/**
	 * Fill layout with no margin.
	 * @param parent
	 * @param horizontal
	 * @param doubleBuffered
	 */
	public LEditor(LContainer parent, boolean horizontal, boolean doubleBuffered) {
		super(parent, horizontal, doubleBuffered);
	}
	
	/**
	 * Grid layout.
	 * @param parent
	 * @param columns
	 * @param equalCols
	 * @param doubleBuffered
	 */
	public LEditor(LContainer parent, int columns, boolean equalCols, boolean doubleBuffered) {
		super(parent, columns, equalCols, doubleBuffered);
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
	
	private Menu addMenu(Composite parent) {
		Menu menu = new Menu(parent);
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
		addMenu(getComposite());
	}
	
	public void addMenu(LFrame frame) {
		Menu menu = getMenu();
		if (menu == null) {
			menu = addMenu(frame.getComposite());
			setMenu(menu);
			addFocusOnClick(this);
		} else if (frame.getMenu() == null) {
			frame.setMenu(menu);
			addFocusOnClick(frame);
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
		Composite header = new Composite(parent.getComposite(), 0); 
		header.setLayout(new RowLayout());
		addHeaderButtons(header);
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
