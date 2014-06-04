package com.dao;

import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.entity.Appuser;

public class RealmDao extends JdbcRealm {
	
	Logger log = LoggerFactory.getLogger(RealmDao.class);
			
	IUserDao userDao;
	
	public RealmDao() {
		InitialContext ic;
		try {
			ic = new InitialContext();
			this.userDao = (IUserDao) ic.lookup("java:global/Auth1/UserDao!com.dao.IUserDao");
			
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean supports(AuthenticationToken token) {
		// TODO Auto-generated method stub
		return super.supports(token);
	}
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();

        // Null username is invalid
        if (username == null) {
            throw new AccountException("Null usernames are not allowed by this realm.");
        }

        SimpleAuthenticationInfo info = null;
        try {

            String password = null;
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("username", username);
            password = (String) userDao.findByNamedQuery(Appuser.FIND_PASSWORD_BY_USERNAME, parameters);

            if (password == null) {
                throw new UnknownAccountException("No account found for user [" + username + "]");
            }

            info = new SimpleAuthenticationInfo(username, password.toCharArray(), getName());
            

        } catch (Exception e) {
            final String message = "There was a SQL error while authenticating user [" + username + "]";
            if (log.isErrorEnabled()) {
                log.error(message, e);
            }

            // Rethrow any SQL errors as an authentication exception
            throw new AuthenticationException(message, e);
        } 

        return info;
	}

}
