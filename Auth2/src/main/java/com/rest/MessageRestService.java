package com.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

//http://localhost:8080/RESTfulExample/rest/message/hello%20world
@Path("/test")
public class MessageRestService {
	
	@GET
	@Path("/{param}")
	public Response printMessage(@PathParam("param") String msg) {

		String result = "Restful example : " + msg;

		return Response.status(200).entity(result).build();

	}
	
	@GET
	@Path("/sget")
	public Response getMessage() {

		return Response.status(200).entity("This is GET").build();

	}
	
	@POST
	@Path("/spost")
	public Response postMessage() {
		String sessionId = null;
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		if(session != null){
			sessionId = (String) session.getId();
		}
		return Response.status(200).entity("This is POST, Session Id : " + sessionId).build();

	}
	
	@PUT
	@Path("/sput")
	public Response putMessage() {
		
		return Response.status(200).entity("This is PUT").build();

	}
	
}