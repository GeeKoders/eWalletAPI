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

public class CreditCardManager {

	static public boolean insert(CreditCardInfo creditCard) {
		boolean result = false;
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "INSERT INTO ewallet.credit_card (member_id, card_number, expire_date, valid_number, status, create_date, update_date) VALUES(?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement pstmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, creditCard.member_id);
			pstmt.setString(2, creditCard.card_number);
			pstmt.setString(3, creditCard.expire_date);
			pstmt.setInt(4, creditCard.valid_number);
			pstmt.setInt(5, creditCard.status);
			pstmt.setTimestamp(6, new Timestamp(creditCard.create_date));
			pstmt.setTimestamp(7, new Timestamp(creditCard.update_date));

			pstmt.executeUpdate();
			ResultSet rset = pstmt.getGeneratedKeys();
			if (rset.next()) {
				creditCard.id = rset.getInt(1);
				result = true;
			}
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} finally {
		}
		return result;
	}

}
