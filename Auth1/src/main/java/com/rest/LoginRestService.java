package com.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DelegatingSubject;
import org.jboss.resteasy.annotations.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.cache.CacheCommon;
import com.common.util.CommonUtils;
import com.connection.util.AuthenticationDetails;
import com.connection.util.Manager;
import com.dao.IUserDao;
import com.entity.Appuser;

public class LoginRestService implements ILoginRestService{
	
	@Inject @Manager
	private IUserDao userDao;
	
	@Inject
	CacheCommon cacheCommon;
	
	Logger logger = LoggerFactory.getLogger(LoginRestService.class);
	
	@Override
	public Response login(@Form Login login, @Context HttpServletRequest request){
		
		String sessionId = null;
		
		String browser = request.getHeader("user-agent");
		browser = CommonUtils.getBrowser(browser);
		DelegatingSubject subject = (DelegatingSubject) SecurityUtils.getSubject();
		
		/*
		 * Validating that the user already has logged in from other machine.
		 */
		validateHost(subject.getHost(), login.getUserName());
		/*
		 * Validating whether the user logged in from another browser.
		 */
		validateUserLogin(subject.getHost(), login.getUserName(), browser);
		
		UsernamePasswordToken token = new UsernamePasswordToken(login.getUserName(), login.getPassword());
		subject.login(token);
		String responseMessage = subject.isAuthenticated()?"user authenticated" : "user is not authenticated";
		
		Session session = subject.getSession(false);
		if(session != null){
			sessionId = (String) session.getId();
			responseMessage = responseMessage + " Session Id : " + (String) session.getId();
		}
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("username", login.getUserName());
		Appuser appuser = (Appuser) userDao.findByNamedQuery(Appuser.FIND_BY_USERNAME, parameters);

		if(appuser != null){
			
			String stringToken = UUID.randomUUID().toString();
			appuser.setToken(stringToken);
			appuser.setHostname(subject.getHost());
			userDao.update(appuser);
			
			cacheUser(subject, session, stringToken, browser);
		}
		
		return Response.status(Status.OK).entity(responseMessage).build();
	}


	private void validateHost(String host, String userName) {
		String browser = null;
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("username", userName);
		Appuser user = (Appuser) userDao.findByNamedQuery(Appuser.FIND_BY_USERNAME, parameters);
		if(user != null && user.getHostname() != null && !host.equals(user.getHostname())){
			Map<String, AuthenticationDetails> browserDetails = (Map<String, AuthenticationDetails>) cacheCommon.get(user.getHostname());
			if(browserDetails != null && browserDetails.size() > 0){
				for (String browserName : browserDetails.keySet()) {
					AuthenticationDetails authenticationDetails = browserDetails.get(browserName);
					if(authenticationDetails != null && authenticationDetails.getUser() != null 
							&& authenticationDetails.getUser().equals(userName)){
						logger.info("{} has forcibly logged out from {}", userName, user.getHostname());
						browser = browserName;
						break;
					}
				}
				if(browser != null){
					logger.info("browser details before : {}", browserDetails);
					AuthenticationDetails authenticationDetails = browserDetails.remove(browser);
					cacheCommon.insert(user.getHostname(), browserDetails);
					logger.info("auth data for {} has removed for {} and host {}", authenticationDetails.getUser(), 
																	browser, user.getHostname());
					logger.info("browser details: {}", browserDetails);
				}
			}
		}
	}


	/*
	 * This functions checks whether the user already has logged in from another browser
	 * and delete the browser information from cache.
	 */
	private void validateUserLogin(String host, String userName, String browser) {
		String matchBrowser = null;
		Map<String, AuthenticationDetails> userDetails = (Map<String, AuthenticationDetails>) cacheCommon.get(host);
		if(userDetails != null && userDetails.size() > 0){
			for (String browserName : userDetails.keySet()) {
				if(!browserName.equals(browser)){
					AuthenticationDetails authenticationDetails = userDetails.get(browserName);
					if(userName.equals(authenticationDetails.getUser())){
						logger.info("user has logged in {} browser", browserName);
						matchBrowser = browserName;
						break;
					}
				}
			}
			if(matchBrowser != null){
				userDetails.remove(matchBrowser);
				cacheCommon.insert(host, userDetails);
			}
		}
	}


	private void cacheUser(DelegatingSubject subject, Session session,
			String stringToken, String browser) {
		AuthenticationDetails authenticationDetails = null;
		Map<String, AuthenticationDetails> browserDetails  = (Map<String, AuthenticationDetails>) cacheCommon.get(subject.getHost());
		if(browserDetails != null){
			if(browserDetails.size() > 0 && browserDetails.containsKey(browser)){
				browserDetails.remove(browser);
			}
		}else {
			browserDetails = new HashMap<String, AuthenticationDetails>();
		}
		if(authenticationDetails == null){
			authenticationDetails = new AuthenticationDetails();
		}
		authenticationDetails.setToken(stringToken);
		authenticationDetails.setUser(subject.getPrincipal().toString());
		authenticationDetails.setSessionId(session.getId().toString());
		browserDetails.put(browser, authenticationDetails);
		cacheCommon.insert(subject.getHost(), browserDetails);
	}

	@Override
	public Response registration(@FormParam("username")String username, @FormParam("password")String password){
		Appuser appuser = new Appuser();
		appuser.setUsername(username);
		appuser.setPassword(new Sha256Hash(password).toBase64());
		
		DelegatingSubject subject = (DelegatingSubject) SecurityUtils.getSubject();
//		appuser.setHostname(subject.getHost());
		userDao.saveUser(appuser);
		
		return Response.status(Status.OK).build();
	}
	
	@Override
	public Response logout(){
		DelegatingSubject subject = (DelegatingSubject) SecurityUtils.getSubject();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("username", subject.getPrincipal());
		Appuser user = (Appuser) userDao.findByNamedQuery(Appuser.FIND_BY_USERNAME, parameters);
		if(user != null){
			
			Session session = subject.getSession(false);
			Map<String, AuthenticationDetails> sessionDetails = (Map<String, AuthenticationDetails>) cacheCommon.get(subject.getHost());
			if(sessionDetails != null && sessionDetails.size() > 0 && sessionDetails.containsKey(session.getId().toString())){
				sessionDetails.remove(session.getId());
			}
			user.setToken(null);
			user.setHostname(null);
			userDao.update(user);
		}
		
		subject.logout();
		
		return Response.status(Status.OK).entity("logged out successfully..").build();
	}
	
	
	@Override
	public Response getToken(String user){
		String token = null;
		Subject subject = SecurityUtils.getSubject();
		if(subject.isAuthenticated()){
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("username", user);
			token = (String) userDao.findByNamedQuery(Appuser.FIND_TOKEN_BY_USERNAME, parameters);
			subject.getSession().touch();
		}
		return Response.status(Status.OK).entity(token).build();
	}
	
}
