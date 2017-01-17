package com.alpha.registration;

import com.alpha.model.User;

public interface UserRegistrationService{

	String registerUser(User user);
	
	User displayUser(String userName);

}