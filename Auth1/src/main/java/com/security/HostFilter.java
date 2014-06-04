package com.security;

import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.support.DelegatingSubject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.cache.CacheCommon;
import com.common.util.CommonUtils;
import com.connection.util.AuthenticationDetails;
import com.dao.IUserDao;
import com.entity.Appuser;

/**
 * Servlet Filter implementation class CommonFilter
 */
public class HostFilter extends AccessControlFilter{
	
	Logger logger = LoggerFactory.getLogger(HostFilter.class);
	private IUserDao userDao;
	
	private static CacheCommon cacheCommon;

	static{
		cacheCommon = new CacheCommon();
	}
	
       
    public HostFilter() {
        super();
        InitialContext ic;
		try {
			ic = new InitialContext();
			this.userDao = (IUserDao) ic.lookup("java:global/Auth1/UserDao!com.dao.IUserDao");
			
		} catch (NamingException e) {
			e.printStackTrace();
		}
    }

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		Appuser user = null;
		String sessionId = null;
		
		HttpServletRequest req = (HttpServletRequest) request;
		String browser = req.getHeader("user-agent");
		browser = CommonUtils.getBrowser(browser);
		
		DelegatingSubject subject = (DelegatingSubject) SecurityUtils.getSubject();
		Session session = subject.getSession();
		if(session != null){
			sessionId = session.getId().toString();
		}
		/*
		 * Checking whether user has logged in using form.
		 * If logged in then check for authentication.
		 */
		logger.info("host name : {}", subject.getHost());
		if(cacheCommon.get(subject.getHost()) != null){
			logger.info("token : {}", cacheCommon.get(subject.getHost()));
			
			Map<String, AuthenticationDetails> browserDetails = (Map<String, AuthenticationDetails>) cacheCommon.get(subject.getHost());
			if(subject.isAuthenticated()){
				if(browserDetails != null && browserDetails.size() > 0 && browserDetails.containsKey(browser)){
					AuthenticationDetails authenticationDetails = browserDetails.get(browser);
					if(authenticationDetails != null){
						logger.info("{} is authenticated with browser : {}", subject.getPrincipal(), browser);
						logger.info("{} has cached.", authenticationDetails.getUser());
						if(subject.getPrincipal().toString().equals(authenticationDetails.getUser())){
							return true;
						}
					}
				}else {
					logger.info("{} logged out focibily");
					subject.logout();
				}
			}
		}
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		saveRequestAndRedirectToLogin(request, response);
        return false;
	}

}
