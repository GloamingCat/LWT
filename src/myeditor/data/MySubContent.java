package myeditor.data;

public class MySubContent {

	public String description = "No description";
	public int value = 0;
	
	public MySubContent() {}
	public MySubContent(int value, String description) {
		this.value = value;
		this.description = description;
	}
	
	public String toString() {
		return "Type " + value;
	}
	
	public boolean equals(Object other) {
		if (other instanceof MySubContent) {
			MySubContent o = (MySubContent) other;
			return o.description.equals(description) && o.value == value;
		} else {
			return false;
		}
	}
	
	public MySubContent clone() {
		return new MySubContent(value, description);
	}
	
	public String encode() {
		return value + "," + description;
	}
	
	public static MySubContent decode(String str) {
		int i = str.indexOf(',');
		String value = str.substring(0, i);
		String description = str.substring(i + 1);
		MySubContent content = new MySubContent(Integer.parseInt(value), description);
		return content;
	}
	
	public static boolean canDecode(String str) {
		int i = str.indexOf(',');
		if (i < 0)
			return false;
		try {
			Integer.parseInt(str.substring(0, i));
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
}
