package myeditor.data;

import lwt.datainterface.Graphical;

public class Content implements Graphical {

	public String name;
	public int value;
	
	public Content() {}
	
	public Content(String name, int value) { 
		this.name = name;
		this.value = value;
	}
	
	public String toString() {
		return name;
	}

	@Override
	public String getImagePath() {
		return "bla";
	}
	
}
