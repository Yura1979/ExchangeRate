package com.gmail.tyi.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DbHelper {

	static final String DB_CONNECTION = "jdbc:mysql://192.168.0.50:3306/nbu_usd_rate?serverTimezone=Europe/Kiev";
	static final String DB_USER = "admin";
	static final String DB_PASSWORD = "P@ssw0rd79";
	static Connection conn;

	private DbHelper() {

	}

	public void addRate(String date, BigDecimal rate) {
		try (PreparedStatement ps = conn.prepareStatement("insert into Rates (date, rate) VALUES(?, ?)")) {
			ps.setString(1, date);
			ps.setBigDecimal(2, rate);
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getRateCondition(String condition) {

		String sql = "select " + condition +"(rate) from Rates";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			for (int i = 1; i <= md.getColumnCount(); i++) {
				System.out.print(md.getColumnName(i) + "\t\t");
			}
			System.out.println();
			while (rs.next()) {
				for (int i = 1; i <= md.getColumnCount(); i++) {
					System.out.print(rs.getString(i) + "\t\t");
				}
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static DbHelper getInstance() {
		DbHelper dbInstance = new DbHelper();
		try {
			conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dbInstance;
	}

}
