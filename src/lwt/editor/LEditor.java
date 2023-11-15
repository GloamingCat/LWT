package lwt.editor;

import java.lang.reflect.Field;

import org.eclipse.swt.widgets.Composite;

public abstract class LEditor extends LView {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LEditor(Composite parent, int style) {
		super(parent, style);
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
