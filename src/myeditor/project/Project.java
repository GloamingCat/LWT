package myeditor.project;

import lwt.dataserialization.LProject;

public class Project implements LProject {

	public static Project current = null;
	public String path;
	
	public Project(String path) {
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
	public boolean hasChanges() {
		return true;
	}

	@Override
	public boolean isDataFile(String path) {
		return false;
	}
	
}
