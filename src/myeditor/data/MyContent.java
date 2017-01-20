package myeditor.data;

import org.eclipse.swt.graphics.Image;

import lwt.datainterface.LGraphical;

public class MyContent implements LGraphical {

	public String name;
	public int value;
	
	public MyContent() {}
	
	public MyContent(String name, int value) { 
		this.name = name;
		this.value = value;
	}
	
	public String toString() {
		return name;
	}

	@Override
	public Image toImage() {
		return null;
	}
	
}
