package com.common.cache;

import net.spy.memcached.MemcachedClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheCommon {
	
	Logger logger = LoggerFactory.getLogger(CacheCommon.class);
	MemcachedClient memcachedClient;
	public static final int CACHE_EXPIRY = 1800000;
	
	public CacheCommon() {
		memcachedClient = CacheUtils.getCacheClient();
	}

	public void doUpdate(String key, Object value) {
		logger.trace("doUpdate(Session) - start"); 

		memcachedClient.replace(key, CACHE_EXPIRY , value);

		logger.trace("doUpdate(Session) - end"); 
	}

	public void doDelete(String key) {
		logger.trace("doDelete(Session) - start"); 

		memcachedClient.delete(key);

		logger.trace("doDelete(Session) - end"); 
	}

	public void doCreate(String key, Object value) {
		logger.trace("doCreate(Session) - start"); 
		memcachedClient.set(key, CACHE_EXPIRY , value);
		logger.trace("doCreate(Session) - end"); 
	}
	
	public Object get(String key){
		
		return memcachedClient.get(key);
	}
	
	public void insert(String key, Object value){
		Object o = memcachedClient.get(key);
		if(o == null){
			doCreate(key, value);
		}else {
			doUpdate(key, value);
		}
		
	}
	
}
