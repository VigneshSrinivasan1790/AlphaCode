package com.alpha.registration.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import com.alpha.common.dao.Repository;
import com.alpha.model.User;
import com.alpha.registration.UserRegistrationService;
import com.alpha.util.MailerUtil;

public class UserRegistrationServiceImpl implements UserRegistrationService {

	private final Repository userRegistrationRepository;
	private final MailerUtil mailerUtil;
	Connection con = null;

	private static final String GET_USER_DETAILS = "SELECT ID, USERNAME, EMAILID FROM TEAMALPHA.USER_REGISTRATION WHERE EMAILID = ?";
	private static final String ADD_USER = "INSERT INTO TEAMALPHA.USER_REGISTRATION " +
			"(USERNAME, EMAILID, PHONENUMBER, USERPASSWORD,ACCOUNTISACTIVE, UPDATETIME) VALUES (?, ?, ?, ?, 'I', NOW())";

	private static final String ADD_ACTIVATION_CODE = "INSERT INTO TEAMALPHA.USER_ACTIVATION " +
			"(USERID, ACTIVATIONCODE, EXPIRATIONDATE) VALUES (?, ?, ?)";

	public UserRegistrationServiceImpl(Repository userRegistrationRepository, MailerUtil mailerUtil) {
		this.userRegistrationRepository = userRegistrationRepository;
		this.mailerUtil = mailerUtil;
	}
	
	public User displayUser(String emailId) {

		User user = new User();
		try(Connection con = userRegistrationRepository.getConnection()) {
			PreparedStatement ps = con.prepareStatement(GET_USER_DETAILS);
			ps.setString(1, emailId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user.setUserId(rs.getInt("ID"));
				user.setUserName(rs.getString("USERNAME"));
				user.setEmailId(rs.getString("EMAILID"));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return user;
	}

	public String registerUser(User user){

		String status = "";
		User registeredUser = displayUser(user.getEmailId());

		if(registeredUser.getEmailId() != null) {
			return "Email Id already registered!";
		}
		status = registerNewUser(user);


		return status;
	}

	private String registerNewUser(User user) {
		String status = "";
		try(Connection con = userRegistrationRepository.getConnection()) {
			PreparedStatement ps = con.prepareStatement(ADD_USER);
			ps.setString(1, user.getUserName());
			ps.setString(2, user.getEmailId());
			ps.setString(3, user.getPhoneNumber());
			ps.setString(4, getHashPassword(user.getPassword()));
			if(ps.executeUpdate() == 1) {
				addActivationCodeForNewUser(user);
				status = "User Registered Successfully and a verification Email is sent to the Registered email address for Activation.";
			}
			else
				status = "Error registering user. Please try again.";
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return status;
	}

	private void addActivationCodeForNewUser(User user) throws ParseException {
		String activationCode = UUID.randomUUID().toString();
		int userId = displayUser(user.getEmailId()).getUserId();
		try(Connection con = userRegistrationRepository.getConnection()) {
			PreparedStatement ps = con.prepareStatement(ADD_ACTIVATION_CODE);
			ps.setInt(1, userId);
			ps.setString(2, activationCode);
			ps.setString(3, getExpiryDate(Calendar.getInstance()));
			if(ps.executeUpdate() != 1)
				throw new RuntimeException("Unable to add user");
			
			String activationUrl = "https://www.alignmygoals.com/"+user.getUserName()+"&" + activationCode;
			mailerUtil.sendMailTo(user.getUserName(), user.getPhoneNumber(), user.getEmailId(), activationUrl);
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
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

	private static String getExpiryDate(Calendar cal) throws ParseException {
		cal.add(Calendar.DATE, 1);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		String formatted = format1.format(cal.getTime());
		
		return formatted;
	}
}