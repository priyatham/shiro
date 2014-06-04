package com.security;

import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.subject.Subject;

import com.common.cache.CacheCommon;
import com.common.cache.CacheUtils;
import com.connection.util.AuthenticationDetails;
import com.dao.IUserDao;
import com.entity.Appuser;

public class SecuritySessionListener implements SessionListener {

	IUserDao userDao;
	private static CacheCommon cacheCommon;
	
	static{
		cacheCommon = new CacheCommon();
	}
	
	public SecuritySessionListener() {
		 InitialContext ic;
			try {
				ic = new InitialContext();
				this.userDao = (IUserDao) ic.lookup("java:global/Auth1/UserDao!com.dao.IUserDao");
				
			} catch (NamingException e) {
				e.printStackTrace();
			}
	}
	

	@Override
	public void onExpiration(Session session) {
		Appuser user = getUser(session);
		if(user != null){
			user.setHostname(null);
			user.setToken(null);
			userDao.update(user);
		}
		clearCache(session);
	}
	
	
	private void clearCache(Session session) {
		String host = session.getHost();
		String browser = null;
		Map<String,AuthenticationDetails> browserDetails = (Map<String,AuthenticationDetails>) cacheCommon.get(host);
		if(browserDetails != null && browserDetails.size() > 0){
			for (String browserName : browserDetails.keySet()) {
				AuthenticationDetails authenticationDetails = browserDetails.get(browserName);
				if(authenticationDetails != null && session.getId().toString().equals(authenticationDetails.getSessionId())){
					browser = browserName;
					break;
				}
			}
			if(browser != null){
				browserDetails.remove(browser);
				cacheCommon.insert(host, browserDetails);
			}
		}
	}

	private Appuser getUser(Session session) {
		Appuser user = null;
		String host = session.getHost();
		
		Map<String,AuthenticationDetails> browserDetails = (Map<String,AuthenticationDetails>) cacheCommon.get(host);
		if(browserDetails != null && browserDetails.size() > 0){
			for (String browserName : browserDetails.keySet()) {
				AuthenticationDetails authenticationDetails = browserDetails.get(browserName);
				if(authenticationDetails != null && session.getId().toString().equals(authenticationDetails.getSessionId())){
					Map<String, Object> parameters = new HashMap<String, Object>();
					parameters.put("username", authenticationDetails.getUser());
					user = (Appuser)userDao.findByNamedQuery(Appuser.FIND_BY_USERNAME, parameters);
					return user;
				}
			}
		}
		return null;
	}


	@Override
	public void onStart(Session session) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStop(Session session) {
		// TODO Auto-generated method stub
		
	}

}
