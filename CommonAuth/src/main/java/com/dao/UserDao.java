package com.dao;

import java.util.Map;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.connection.util.Manager;

@Stateless
@Remote(value = IUserDao.class)
public class UserDao<T> implements IUserDao<T>{

	@Inject @Manager
	private EntityManager manager;
//	private Instance<EntityManager> manager;
	
	@Override
	public void saveUser(T t){
		manager.persist(t);
	}

	@Override
	public T findByNamedQuery(String queryString, Map<String, Object> parameters) {
		T t = null;
		Query query = manager.createNamedQuery(queryString);
		for (String key : parameters.keySet()) {
			query.setParameter(key, parameters.get(key));
		}
		try{
			t = (T) query.getSingleResult();
		}catch(NoResultException noResultException){
			
		}
		return t;
	}
	
	@Override
	public void update(T t) {
		manager.merge(t);
	}

}
