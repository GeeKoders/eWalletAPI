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

public class ScanManager {
	static public boolean insert(ScanInfo scan) {
		boolean result = false;
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "INSERT INTO ewallet.scan (type, source_id, source_count, dest_id, dest_count, price, status, create_date, update_date) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement pstmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, scan.type);
			pstmt.setInt(2, scan.source_id);
			pstmt.setInt(3, scan.source_count);
			pstmt.setInt(4, scan.dest_id);
			pstmt.setInt(5, scan.dest_count);
			pstmt.setInt(6, scan.price);
			pstmt.setInt(7, scan.status);
			pstmt.setTimestamp(8, new Timestamp(scan.create_date));
			pstmt.setTimestamp(9, new Timestamp(scan.update_date));
			pstmt.executeUpdate();
			ResultSet rset = pstmt.getGeneratedKeys();
			if (rset.next()) {
				scan.id = rset.getInt(1);
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
