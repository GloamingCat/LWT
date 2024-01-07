package myeditor.data;

import lwt.datainterface.LGraphical;
import lwt.graphics.LTexture;
import myeditor.project.MyProject;

public class MyContent implements LGraphical {

	public String name;
	public String img;
	public int value;
	
	public MySubContent subContent;
	
	public MyContent() {}
	
	public MyContent(String name) {
		this(name, 0, "img.png", new MySubContent());
	}
	
	public MyContent(int i, int j) {
		this("item " + i, i, "img.png", new MySubContent(j, 
				"The type of item " + i + " is " + j));
	}
	
	public MyContent(String name, int value, String img, MySubContent sc) { 
		this.name = name;
		this.value = value;
		this.img = img;
		subContent = sc;
	}
	
	public String toString() {
		return name;
	}

	@Override
	public LTexture toImage() {
		return new LTexture(MyProject.current.imagePath() + img);
	}
	
	public boolean equals(Object other) {
		if (other instanceof MyContent) {
			MyContent o = (MyContent) other;
			return o.name.equals(name) && o.subContent.equals(subContent)
					&& o.img.equals(img) && o.value == value;
		} else {
			return false;
		}
	}
	
	public MyContent clone() {
		return new MyContent(name, value, img, subContent.clone());
	}
	
	public String encode() {
		return value + "," + name + "," + img + "," + subContent.encode();
	}
	
	public static MyContent decode(String str) {
		try {
			int i = str.indexOf(',');
			String value = str.substring(0, i);
			str = str.substring(i + 1);
			i = str.indexOf(',');
			String name = str.substring(0, i);
			str = str.substring(i + 1);
			i = str.indexOf(',');
			String img = str.substring(0, i);
			String subContent = str.substring(i + 1);
			return new MyContent(name, Integer.parseInt(value),
				img, MySubContent.decode(subContent));
		} catch (StringIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public static boolean canDecode(String str) {
		String[] parts = str.split(",");
		if (parts.length < 4)
			return false;
		try {
			Integer.parseInt(parts[0]);
			Integer.parseInt(parts[3]);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
}
