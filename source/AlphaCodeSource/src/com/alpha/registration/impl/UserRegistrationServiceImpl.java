package com.alpha.registration.impl;

import java.sql.Blob;
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
		try {
			con = userRegistrationRepository.getConnection();
			PreparedStatement ps = con.prepareStatement(SAMPLE_SELECT);
			ps.setString(1, userName);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user = new User(
						rs.getString("USERNAME"),
						rs.getString("EMAILID")
						);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return user;
	}

	public int save(User user){

		String sql = "INSERT INTO TEAMALPHA.USERREGISTRATION " +
				"(USERNAME, EMAILID, USERPASSWORD) VALUES (?, ?, ?)";
		int status = 0;

		try {
			con = userRegistrationRepository.getConnection();
			Blob b1 = con.createBlob();
			b1.setBytes(1, new byte[10]);
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user.getUserName());
			ps.setString(2, user.getEmailId());
			ps.setBlob(3, b1);
			status = ps.executeUpdate();
			ps.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {}
			}
		}
		return status;
	}
}