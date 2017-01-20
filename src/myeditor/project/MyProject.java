package myeditor.project;

import lwt.dataserialization.LSerializer;

public class MyProject implements LSerializer {

	public static MyProject current = null;
	public String path;
	
	public MyProject(String path) {
		this.path = path;
		current = this;
	}
	
	public String dataPath() {
		return path + "data/";
	}
	
	public String imagePath() {
		return path + "images/";
	}
	
	public String scriptPath() {
		return path + "scripts/custom/";
	}

	@Override
	public boolean save() {
		return true;
	}

	@Override
	public boolean load() {
		return true;
	}

	@Override
	public boolean isDataFolder(String path) {
		return false;
	}
	
}
