package com.security;

import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.support.DelegatingSubject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.cache.CacheCommon;
import com.common.util.CommonUtils;
import com.connection.util.AuthenticationDetails;
import com.dao.IUserDao;

/**
 * Servlet Filter implementation class CommonFilter
 */
public class CommonFilter extends AccessControlFilter{
	
	Logger logger = LoggerFactory.getLogger(CommonFilter.class);
	public String tokenUrl;
	
	private IUserDao userDao;
	
	private static CacheCommon cacheCommon;

	static{
		cacheCommon = new CacheCommon();
	}
	
       
    public CommonFilter() {
        super();
        InitialContext ic;
		try {
			ic = new InitialContext();
			this.userDao = (IUserDao) ic.lookup("java:global/Auth2/UserDao!com.dao.IUserDao");
			
		} catch (NamingException e) {
			e.printStackTrace();
		}
    }

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
//		Appuser user = null;
		AuthenticationDetails user = null;
		String sessionId = null;
		
		HttpServletRequest req = (HttpServletRequest) request;
		String browser = req.getHeader("user-agent");
		browser = CommonUtils.getBrowser(browser);
		
		DelegatingSubject subject = (DelegatingSubject) SecurityUtils.getSubject();
		
		/*
		 * Checking whether user has logged in using form.
		 * If logged in then check for authentication.
		 */
		logger.info("host name : {}", subject.getHost());
		if(cacheCommon.get(subject.getHost()) != null){
			Map<String, AuthenticationDetails> browserDetails = (Map<String, AuthenticationDetails>) cacheCommon.get(subject.getHost());
			
			
			if(!subject.isAuthenticated()){
				logger.info("user not authenticated, token {}", cacheCommon.get(subject.getHost()).toString());
				
				if(browserDetails != null && browserDetails.size() > 0 && browserDetails.containsKey(browser)) {
					user = browserDetails.get(browser);
				}
				
				if(user != null){
					logger.info("user host : {} and token : {}", subject.getHost(), user.getToken());
					char[] password = getToken(user.getUser(), user.getSessionId());
					if(password != null && password.length > 0){
						UsernamePasswordToken token = new UsernamePasswordToken(user.getUser(), password);
						subject.login(token);
					}else {
						return false;
					}
				}else{
					return false;
				}
			}else {
				logger.info("user name : {}", subject.getPrincipal());
				if(browserDetails != null && browserDetails.size() > 0 && browserDetails.containsKey(browser)){
					for (String browserName : browserDetails.keySet()) {
						if(!browserName.equals(browser)){
							AuthenticationDetails authenticationDetails = browserDetails.get(browserName);
							if(authenticationDetails != null){
								if(authenticationDetails.getUser().equals(subject.getPrincipal().toString())){
									subject.logout();
									return false;
								}
							}
						}
					}
				}else {
					subject.logout();
					logger.info("Auth2 logged out successfully");
				}
			}
			return true;
		}
		return false;
	}

	private char[] getToken(String user, String sessionId) throws Exception {
		String token = null;
		String url = tokenUrl;
		url = url.concat("/").concat(user).concat(";JSESSIONID=").concat(sessionId);
		token = CommonUtils.getUrlResponse(url);
		if(token != null){
			return token.toCharArray();
		}
		return null;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		saveRequestAndRedirectToLogin(request, response);
        return false;
	}

	public String getTokenUrl() {
		return tokenUrl;
	}

	public void setTokenUrl(String tokenUrl) {
		this.tokenUrl = tokenUrl;
	}

}
