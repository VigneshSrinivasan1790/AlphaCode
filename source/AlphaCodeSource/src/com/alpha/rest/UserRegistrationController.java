package com.alpha.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alpha.model.User;
import com.alpha.registration.UserRegistrationService;

@Component
@Path("/registration")
public class UserRegistrationController {

	@Autowired
	UserRegistrationService transactionBo;

	@GET
	@Path("/userRegistration")
	public Response saveUser() {
		int status = 400;
		String message = "";
		User user = new User("Senthil", "senthil@gmail.com");
		int result = transactionBo.save(user);
		if(result == 1)
			status = 200;
		message = "User Registered successfully";
		return Response.status(status).entity(message).build();

	}

	@GET
	@Path("/userDetails")
	public Response getUserDetails() {
		int status = 400;
		User user = transactionBo.displayUser("Senthil");
		if(user!=null)
			status = 200;
		String result = "User Name : " + user.getUserName() + " \n Email Id : " + user.getEmailId();
		return Response.status(status).entity(result).build();

	}
}