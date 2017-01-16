package com.alpha.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alpha.model.User;
import com.alpha.registration.UserRegistrationService;

@Component
@Path("/registration")
public class UserRegistrationController {
	Logger logger = Logger.getLogger(UserRegistrationController.class);

	@Autowired
	UserRegistrationService registrationBo;

	@POST
	@Path("/userRegistration")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerUser(String mess) {
		int status = 400;
		String message = "";
		ObjectMapper mapper = new ObjectMapper();
		User user;
		try {
			user = mapper.readValue(mess, User.class);
			System.out.println(user.getUserName() + "   " + user.getEmailId());
			
			int result = registrationBo.registerUser(user);
			if(result == 1)
				status = 200;
			message = "User Registered successfully";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Response.status(status).entity(message).build();

	}

	@GET
	@Path("/userDetails")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserDetails() {
		logger.info("This is my first log4j's statement");
		User user = registrationBo.displayUser("Senthil");
		int status = 400;
		String message = "";
		if(user!=null) {
			status = 200;
			message = user.getUserName() + " ; " + user.getEmailId() + " ; " + user.getPhoneNumber();
		}
		return Response.status(status).entity(message).build();

	}
}