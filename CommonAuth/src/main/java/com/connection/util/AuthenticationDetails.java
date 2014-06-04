package com.connection.util;

import java.io.Serializable;

public class AuthenticationDetails implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4276193998247347544L;

	private String user;
	
	private String token;
	
	private String sessionId;
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
