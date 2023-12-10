package myeditor.project;

import lwt.dataestructure.LDataList;
import lwt.dataestructure.LDataTree;
import lwt.dataserialization.LFileManager;
import lwt.dataserialization.LSerializer;
import myeditor.MyVocab;
import myeditor.data.MyContent;

public class MyProject implements LSerializer {

	public static MyProject current = null;
	public String path;
	
	public LDataList<MyContent> contentList; 
	public LDataList<MyContent> contentGrid; 
	public LDataTree<MyContent> contentTree; 
	public LDataList<String> subContentTypes; 
	
	public MyProject(String path) {
		this.path = LFileManager.getDirectory(path);
		current = this;
	}
	
	private LDataList<MyContent> defaultContentList(int n) {
		LDataList<MyContent> list = new LDataList<>();
		for(int i = 0; i < n; i++) {
			list.add(new MyContent(i*10, i));
		}
		return list;
	}
	
	private LDataTree<MyContent> defaultContentTree(int n, int m, int l) {
		LDataTree<MyContent> root = new LDataTree<>();
		for (int i = 0; i < n; i++) {
			MyContent data = new MyContent(i, i);
			data.name = "item " + i;
			LDataTree<MyContent> node = new LDataTree<MyContent>(data, root);
			for (int j = 0; j < m; j++) {
				data = new MyContent(j, i);
				data.name = "item " + i + " " + j;
				LDataTree<MyContent> subnode = new LDataTree<MyContent>(data, node);
				for (int k = 0; k < l; k++) {
					data = new MyContent(k, j);
					data.name = "item " + i + " " + j + " " + k;
					new LDataTree<MyContent>(data, subnode);
				}
			}
		}
		return root;
	}
	
	private LDataList<String> defaultTypes(int n) {
		LDataList<String> types = new LDataList<>(n);
		for (int i = 0; i < n; i++)
			types.add(MyVocab.instance.TYPE + i);
		return types;
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
	public void initialize() {
		subContentTypes = defaultTypes(10);
		contentList = defaultContentList(9);
		contentGrid = defaultContentList(9);
		contentTree = defaultContentTree(3, 3, 3);
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
