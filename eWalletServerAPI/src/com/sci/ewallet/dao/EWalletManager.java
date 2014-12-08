package com.sci.ewallet.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class EWalletManager {

	public static boolean insert(EWalletInfo info) {
		boolean result = false;
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "INSERT INTO ewallet.ewallet (account, account_hash, email, email_hash, status, name, mobile, address, device_id, p10_value, cert, check_expire, mail_code, sms_code, create_date, update_date) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement pstmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, info.account);
			pstmt.setString(2, info.accountHash);
			pstmt.setString(3, info.email);
			pstmt.setString(4, info.emailHash);
			pstmt.setInt(5, info.status);
			pstmt.setString(6, info.name);
			pstmt.setString(7, info.mobile);
			pstmt.setString(8, info.address);
			pstmt.setString(9, info.deviceId);
			pstmt.setString(10, info.p10Value);
			pstmt.setBytes(11, info.cert);
			pstmt.setLong(12, info.checkExpire);
			pstmt.setInt(13, info.mailCode);
			pstmt.setInt(14, info.smsCode);
			pstmt.setLong(15, info.create_date);
			pstmt.setLong(16, info.update_date);
			pstmt.executeUpdate();
			ResultSet rset = pstmt.getGeneratedKeys();
			if (rset.next()) {
				info.id = rset.getInt(1);
				result = true;
			}
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static boolean save(EWalletInfo info) {
		boolean result = false;
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "UPDATE ewallet.ewallet SET account_hash = ?, email_hash = ?, status = ?, name = ?, mobile = ?, address = ?, pk_data = ?, p10_value = ?, cert = ?, check_expire = ?, mail_code = ?, sms_code = ?, update_date = ? WHERE id=?;";
			PreparedStatement pstmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, info.accountHash);
			pstmt.setString(2, info.emailHash);
			pstmt.setInt(3, info.status);
			pstmt.setString(4, info.name);
			pstmt.setString(5, info.mobile);
			pstmt.setString(6, info.address);
			pstmt.setBytes(7, info.pkData);
			pstmt.setString(8, info.p10Value);
			pstmt.setBytes(9, info.cert);
			pstmt.setLong(10, info.checkExpire);
			pstmt.setInt(11, info.mailCode);
			pstmt.setInt(12, info.smsCode);
			pstmt.setLong(13, info.update_date);
			pstmt.setInt(14, info.id);
			pstmt.executeUpdate();
			result = true;
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static EWalletInfo load(ResultSet rset) throws SQLException {
		EWalletInfo info = new EWalletInfo();
		info.id = rset.getInt("id");
		info.status = rset.getInt("status");
		info.account = rset.getString("account");
		info.accountHash = rset.getString("account_hash");
		info.email = rset.getString("email");
		info.emailHash = rset.getString("email_hash");
		info.name = rset.getString("name");
		info.mobile = rset.getString("mobile");
		info.address = rset.getString("address");
		info.deviceId = rset.getString("device_id");
		info.pkData = rset.getBytes("pk_data");
		info.p10Value = rset.getString("p10_value");
		info.cert = rset.getBytes("cert");
		info.checkExpire = rset.getLong("check_expire");
		info.mailCode = rset.getInt("mail_code");
		info.smsCode = rset.getInt("sms_Code");
		info.create_date = rset.getLong("create_date");
		info.update_date = rset.getLong("update_date");
		return info;
	}

	public static EWalletInfo getByAccount(String account) {
		EWalletInfo info = null;
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "SELECT * FROM ewallet.ewallet WHERE account = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, account);
			ResultSet rset = pstmt.executeQuery();
			if (rset.next()) {
				info = load(rset);
			}
			rset.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	}

	public static EWalletInfo getByEmail(String email) {
		EWalletInfo info = null;
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "SELECT * FROM ewallet.ewallet WHERE email = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			ResultSet rset = pstmt.executeQuery();
			if (rset.next()) {
				info = load(rset);
			}
			rset.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	}

	public static EWalletInfo login(String account, String password) {
		EWalletInfo info = null;
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "SELECT * FROM ewallet.ewallet WHERE (account = ? AND account_hash = ?) OR (email = ? AND email_hash = ?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, account);
			pstmt.setString(2, password);
			pstmt.setString(3, account);
			pstmt.setString(4, password);
			ResultSet rset = pstmt.executeQuery();
			if (rset.next()) {
				info = load(rset);
			}
			rset.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	}

}
