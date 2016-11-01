package myeditor.data;

public class Content {

	public String name;
	public int value;
	
	public Content(String name, int value) { 
		this.name = name;
		this.value = value;
	}
	
	public String toString() {
		return name;
	}
	
}
