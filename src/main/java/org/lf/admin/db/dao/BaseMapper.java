package org.lf.admin.db.dao;

import java.util.List;

public interface BaseMapper<T> {
	T select(T record);
	
	List<T> selectList(T record);
}
