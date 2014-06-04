package com.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "appuser")
@NamedQueries({
	@NamedQuery(name = Appuser.FIND_PASSWORD_BY_USERNAME, query = "select a.password from Appuser a where a.username = :username"),
	@NamedQuery(name = Appuser.FIND_BY_HOSTNAME, query = "select a from Appuser a where a.hostname = :hostname"),
	@NamedQuery(name = Appuser.FIND_BY_USERNAME, query = "select a from Appuser a where a.username = :username"),
	@NamedQuery(name = Appuser.FIND_TOKEN_BY_USERNAME, query = "select a.token from Appuser a where a.username = :username"),
	@NamedQuery(name = Appuser.FIND_TOKEN_BY_HOSTNAME, query = "select a.token from Appuser a where a.hostname = :hostname"),
	@NamedQuery(name = Appuser.FIND_BY_TOKEN, query = "select a from Appuser a where a.token = :token")
})
public class Appuser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7148441362312228328L;
	
	public static final String PREFIX = "com.entity.Appuser.";
	public static final String FIND_PASSWORD_BY_USERNAME = PREFIX + "findPasswordByUsername";
	public static final String FIND_BY_HOSTNAME = PREFIX  + "findByHostname";

	public static final String FIND_BY_USERNAME = PREFIX + "findByUsername";

	public static final String FIND_TOKEN_BY_USERNAME = PREFIX + "findTokenByUsername";
	
	public static final String FIND_TOKEN_BY_HOSTNAME = PREFIX + "findTokenByHostname";

	public static final String FIND_BY_TOKEN = PREFIX + "findByToken";


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	
	private String password;
	
	@Column(name = "host_name")
	private String hostname;
	
	private String token;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String stringToken) {
		this.token = stringToken;
	}
}
