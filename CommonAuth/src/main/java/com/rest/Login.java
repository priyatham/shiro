package com.rest;

import javax.ws.rs.FormParam;

public class Login {
	
	@FormParam("username")
	private String userName;
	
	@FormParam("password")
	private String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
