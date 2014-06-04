package com.app;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.rest.LoginRestService;
import com.rest.MessageRestService;

@ApplicationPath("/rest")
public class MessageApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> classes = new HashSet<Class<?>>();

	public MessageApplication() {
		classes.add(MessageRestService.class);
		classes.add(LoginRestService.class);
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
	
	public Set<Class<?>> getClasses(){
		return classes;
	}
	
	public void setClasses(Set<Class<?>> classes){
		this.classes = classes;
	}
	
}
