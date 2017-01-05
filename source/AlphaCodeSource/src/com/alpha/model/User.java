package com.alpha.model;

import java.sql.Blob;

public class User {
	private String userName;
	private String emailId;
	private Blob password;
	
	public User(String userName, String emailId) {
		this.userName = userName;
		this.emailId = emailId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public Blob getPassword() {
		return password;
	}
	public void setPassword(Blob password) {
		this.password = password;
	}
}
