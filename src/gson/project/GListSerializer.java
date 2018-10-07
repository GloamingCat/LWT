package gson.project;

import java.lang.reflect.Type;

import lwt.dataestructure.LDataList;

public interface GListSerializer<T> {

	public Type getDataType();
	public LDataList<T> getList();
	
}
