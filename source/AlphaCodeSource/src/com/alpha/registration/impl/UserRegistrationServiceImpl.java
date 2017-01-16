package com.alpha.registration.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.alpha.common.dao.Repository;
import com.alpha.model.User;
import com.alpha.registration.UserRegistrationService;

public class UserRegistrationServiceImpl implements UserRegistrationService {

	private Repository userRegistrationRepository;
	Connection con = null;

	private final String SAMPLE_SELECT = "SELECT USERNAME,EMAILID FROM TEAMALPHA.USERREGISTRATION WHERE USERNAME = ?";
	public UserRegistrationServiceImpl(Repository userRegistrationRepository) {
		this.userRegistrationRepository = userRegistrationRepository;
	}
	public User displayUser(String userName) {

		User user = null;
		try(Connection con = userRegistrationRepository.getConnection()) {
			PreparedStatement ps = con.prepareStatement(SAMPLE_SELECT);
			ps.setString(1, userName);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {

			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return user;
	}

	public int registerUser(User user){

		String sql = "INSERT INTO TEAMALPHA.USERREGISTRATION " +
				"(USERNAME, EMAILID, PHONENUMBER, USERPASSWORD) VALUES (?, ?, ?, ?)";
		int status = 0;
		
		if(displayUser(user.getUserName()) != null) {
			return -1;
		}

		try(Connection con = userRegistrationRepository.getConnection()) {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user.getUserName());
			ps.setString(2, user.getEmailId());
			ps.setString(3, user.getPhoneNumber());
			ps.setString(4, getHashPassword(user.getPassword()));
			status = ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return status;
	}

	public static String getHashPassword(String text) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return text;
	}

}