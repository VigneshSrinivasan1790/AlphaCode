package com.alpha.common.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class RepositoryImpl implements Repository {

	private DataSource dataSource;
	
	public RepositoryImpl(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public Connection getConnection() throws SQLException{
		return dataSource.getConnection();
	}
}
