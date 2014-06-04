package com.common.cache;

import java.io.IOException;
import java.net.InetSocketAddress;

import net.spy.memcached.MemcachedClient;

public class CacheUtils {
	private static MemcachedClient memcachedClient = null;
	
	public static MemcachedClient getCacheClient(){
		if(memcachedClient == null){
			try {
				memcachedClient = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return memcachedClient;
	}
}
