package com.sci.ewallet.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MemberManager {

	static private MemberInfo load(ResultSet rset) throws SQLException {
		MemberInfo member = new MemberInfo();
		member.id = rset.getInt("id");
		member.username = rset.getString("username");
		member.username_hashcode = rset.getString("username_hashcode");
		member.name = rset.getString("name");
		member.password = rset.getString("password");
		member.email = rset.getString("email");
		member.email_hashcode = rset.getString("email_hashcode");
		member.mobile = rset.getString("mobile");
		member.address = rset.getString("address");
		member.issuer = rset.getString("issuer");
		member.serial_no = rset.getString("serial_no");
		member.mac_address = rset.getString("mac_address");
		member.status = rset.getInt("status");
		Timestamp time = rset.getTimestamp("create_date");
		member.create_date = time == null ? 0 : time.getTime();
		time = rset.getTimestamp("update_date");
		member.update_date = time == null ? 0 : time.getTime();
		return member;
	}

	static public boolean insert(MemberInfo member) {
		boolean result = false;
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "INSERT INTO ewallet.member (username, username_hashcode, name, password, email, email_hashcode, mobile, address, issuer, serial_no, mac_address, status, create_date, update_date) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement pstmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, member.username);
			pstmt.setString(2, member.username_hashcode);
			pstmt.setString(3, member.name);
			pstmt.setString(4, member.password);
			pstmt.setString(5, member.email);
			pstmt.setString(6, member.email_hashcode);
			pstmt.setString(7, member.mobile);
			pstmt.setString(8, member.address);
			pstmt.setString(9, member.issuer);
			pstmt.setString(10, member.serial_no);
			pstmt.setString(11, member.mac_address);
			pstmt.setInt(12, member.status);
			pstmt.setTimestamp(13, new Timestamp(member.create_date));
			pstmt.setTimestamp(14, new Timestamp(member.update_date));
			pstmt.executeUpdate();
			ResultSet rset = pstmt.getGeneratedKeys();
			if (rset.next()) {
				member.id = rset.getInt(1);
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

	static public boolean update(MemberInfo member) {
		boolean result = false;
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "UPDATE ewallet.member SET name=?, password=?, mobile=?, address=?, update_date=? WHERE id=?;";
			PreparedStatement pstmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, member.name);
			pstmt.setString(2, member.password);
			pstmt.setString(3, member.mobile);
			pstmt.setString(4, member.address);
			pstmt.setTimestamp(5, new Timestamp(member.update_date));
			pstmt.setInt(6, member.id);
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

	static public MemberInfo getByLogin(String username, String password) {
		MemberInfo member = null;
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "SELECT * FROM ewallet.member WHERE username=? AND password=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			ResultSet rset = pstmt.executeQuery();
			if (rset.next()) {
				member = load(rset);
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
		return member;
	}

	static public MemberInfo getByUsername(String username) {
		MemberInfo member = null;
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "SELECT * FROM ewallet.member WHERE username=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			ResultSet rset = pstmt.executeQuery();
			if (rset.next()) {
				member = load(rset);
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
		return member;
	}

	static public MemberInfo getByEmail(String email) {
		MemberInfo member = null;
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "SELECT * FROM ewallet.member WHERE email=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			ResultSet rset = pstmt.executeQuery();
			if (rset.next()) {
				member = load(rset);
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
		return member;
	}

	static public MemberInfo getById(int id) {
		MemberInfo member = null;
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "SELECT * FROM ewallet.member WHERE id=? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			ResultSet rset = pstmt.executeQuery();
			if (rset.next()) {
				member = load(rset);
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
		return member;
	}
}
