package myeditor.data;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.SWTResourceManager;

import lwt.datainterface.LGraphical;
import myeditor.project.MyProject;

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
		return SWTResourceManager.getImage(MyProject.current.imagePath() + "img.png");
	}
	
	public MyContent clone() {
		return new MyContent(name, value);
	}
	
	public String encode() {
		return value + "," + name;
	}
	
	public static MyContent decode(String str) {
		int i = str.indexOf(',');
		int value = Integer.parseInt(str.substring(0, i));
		String name = str.substring(i + 1);
		return new MyContent(name, value);
	}
	
}
