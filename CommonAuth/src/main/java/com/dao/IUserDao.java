package com.dao;

import java.util.Map;

public interface IUserDao<T> {
	public void saveUser(T t);
	
	public T findByNamedQuery(String queryString, Map<String, Object> parameters);

	public void update(T t);
}
