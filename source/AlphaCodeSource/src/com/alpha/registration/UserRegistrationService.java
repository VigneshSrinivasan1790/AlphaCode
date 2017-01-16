package com.alpha.registration;

import com.alpha.model.User;

public interface UserRegistrationService{

	int registerUser(User user);
	
	User displayUser(String userName);

}