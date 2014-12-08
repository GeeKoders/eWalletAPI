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

public class StoreManager {

	static private StoreInfo load(ResultSet rset) throws SQLException {
		StoreInfo store = new StoreInfo();
		store.id = rset.getInt("id");
		store.username = rset.getString("username");
		store.name = rset.getString("name");
		store.password = rset.getString("password");
		store.price = rset.getInt("price");
		store.type = rset.getInt("type");
		store.address = rset.getString("address");
		store.status = rset.getInt("status");
		Timestamp time = rset.getTimestamp("create_date");
		store.create_date = time == null ? 0 : time.getTime();
		time = rset.getTimestamp("update_date");
		store.update_date = time == null ? 0 : time.getTime();
		return store;
	}

	static public boolean insert(StoreInfo store) {
		boolean result = false;
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "INSERT INTO ewallet.store (username, name, password, price, type, address, status, create_date, update_date) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement pstmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, store.username);
			pstmt.setString(2, store.name);
			pstmt.setString(3, store.password);
			pstmt.setInt(4, store.price);
			pstmt.setInt(5, store.type);
			pstmt.setString(6, store.address);
			pstmt.setInt(7, store.status);
			pstmt.setTimestamp(8, new Timestamp(store.create_date));
			pstmt.setTimestamp(9, new Timestamp(store.update_date));
			pstmt.executeUpdate();
			ResultSet rset = pstmt.getGeneratedKeys();
			if (rset.next()) {
				store.id = rset.getInt(1);
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

	public static StoreInfo get(String name, String password) {
		StoreInfo store = null;
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "SELECT * FROM ewallet.store WHERE name=? AND password=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, password);
			ResultSet rset = pstmt.executeQuery();
			if (rset.next()) {
				store = load(rset);
			}
			rset.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new java.lang.RuntimeException(e);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return store;
	}

}
