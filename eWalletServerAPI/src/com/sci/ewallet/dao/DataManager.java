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

public class DataManager {

	static public boolean insert(DataInfo data) {
		boolean result = false;
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "INSERT INTO ewallet.data (issuer, serial_no, cert, pfx, mac_address, status, create_date, update_date) VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement pstmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, data.issuer);
			pstmt.setString(2, data.serial_no);
			pstmt.setString(3, data.cert);
			pstmt.setString(4, data.pfx);
			pstmt.setString(5, data.mac_address);
			pstmt.setInt(6, data.status);
			pstmt.setTimestamp(7, new Timestamp(data.create_date));
			pstmt.setTimestamp(8, new Timestamp(data.update_date));
			pstmt.executeUpdate();
			ResultSet rset = pstmt.getGeneratedKeys();
			if (rset.next()) {
				data.id = rset.getInt(1);
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
}
