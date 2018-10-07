package gson.project;

import java.lang.reflect.Type;

import lwt.dataestructure.LDataTree;

public interface GTreeSerializer<T> {

	public Type getDataType();
	public LDataTree<T> getTree();
	
}
