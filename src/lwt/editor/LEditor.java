package lwt.editor;

import java.lang.reflect.Field;

import lwt.container.LContainer;
import lwt.container.LView;

public abstract class LEditor extends LView {
	
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

	public abstract void setObject(Object object);
	
	public abstract void saveObjectValues();

	protected Object getFieldValue(Object object, String name) {
		try {
			Field field = object.getClass().getField(name);
			return field.get(object);
		} catch (NoSuchFieldException e) {
			System.out.println(name + " not found in " + object.getClass().toString());
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected void setFieldValue(Object object, String name, Object value) {
		try {
			Field field = object.getClass().getField(name);
			field.set(object, value);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
}
