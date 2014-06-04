package com.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.Form;

@Path("/")
public interface ILoginRestService {

	@POST
	@Path("/login")
	public Response login(@Form Login login, @Context HttpServletRequest request);
	
	@POST
	@Path("/registration")
	public Response registration(@FormParam("username")String username, @FormParam("password")String password);
	
	@GET
	@Path("/logout")
	public Response logout();
	
	@GET
	@Path("/token/{user}")
	public Response getToken(@PathParam("user")String user);
	
}
