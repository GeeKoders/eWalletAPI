/**
 * 
 */
package com.sci.ewallet.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * @author Shane
 */
public class TransactionManager {

	static private TransactionInfo load(ResultSet rset) throws SQLException {
		TransactionInfo transaction = new TransactionInfo();
		transaction.id = rset.getInt("id");
		transaction.type = rset.getString("type");
		transaction.source_id = rset.getInt("source_id");
		transaction.source_count = rset.getInt("source_count");
		transaction.dest_id = rset.getInt("dest_id");
		transaction.dest_count = rset.getInt("dest_count");
		transaction.price = rset.getInt("price");
		transaction.issuer = rset.getString("issuer");
		transaction.serial_no = rset.getString("serial_no");
		;
		transaction.card_id = rset.getInt("card_id");
		transaction.card_no = rset.getString("card_no");
		transaction.status = rset.getInt("status");
		Timestamp time = rset.getTimestamp("create_date");
		transaction.create_date = time == null ? 0 : time.getTime();
		time = rset.getTimestamp("update_date");
		transaction.update_date = time == null ? 0 : time.getTime();
		return transaction;
	}

	static public boolean insert(TransactionInfo tran) {
		boolean result = false;
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "INSERT INTO ewallet.transaction (type, source_id, source_count, dest_id, dest_count, price, issuer, serial_no, card_id, card_no, status, create_date, update_date) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement pstmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, tran.type);
			pstmt.setInt(2, tran.source_id);
			pstmt.setInt(3, tran.source_count);
			pstmt.setInt(4, tran.dest_id);
			pstmt.setInt(5, tran.dest_count);
			pstmt.setInt(6, tran.price);
			pstmt.setString(7, tran.issuer);
			pstmt.setString(8, tran.serial_no);
			;
			pstmt.setInt(9, tran.card_id);
			pstmt.setString(10, tran.card_no);
			pstmt.setInt(11, tran.status);
			pstmt.setTimestamp(12, new Timestamp(tran.create_date));
			pstmt.setTimestamp(13, new Timestamp(tran.update_date));
			pstmt.executeUpdate();
			ResultSet rset = pstmt.getGeneratedKeys();
			if (rset.next()) {
				tran.id = rset.getInt(1);
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

	static public boolean update(TransactionInfo tran) {
		boolean result = false;
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "UPDATE ewallet.transaction SET type = ?, source_id = ?, source_count = ?, dest_id = ?, dest_count = ?, price = ?, issuer = ?, serial_no = ?, card_id = ?, card_no = ?, create_date = ?, update_date = ?, status = ? WHERE id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, tran.type);
			pstmt.setInt(2, tran.source_id);
			pstmt.setInt(3, tran.source_count);
			pstmt.setInt(4, tran.dest_id);
			pstmt.setInt(5, tran.dest_count);
			pstmt.setInt(6, tran.price);
			pstmt.setString(7, tran.issuer);
			pstmt.setString(8, tran.serial_no);
			;
			pstmt.setInt(9, tran.card_id);
			pstmt.setString(10, tran.card_no);
			pstmt.setTimestamp(11, new Timestamp(tran.create_date));
			pstmt.setTimestamp(12, new Timestamp(tran.update_date));
			pstmt.setInt(13, tran.status);
			pstmt.setInt(14, tran.id);
			pstmt.execute();
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

	static public TransactionInfo get(int id) {
		TransactionInfo Transaction = null;
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "SELECT * FROM ewallet.transaction WHERE id=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, id);
			ResultSet rset = pstmt.executeQuery();
			if (rset.next()) {
				Transaction = load(rset);
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
		return Transaction;
	}

	public static TransactionInfo get(int id, String type, int source_id,
			int price) {
		TransactionInfo Transaction = null;
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "SELECT * FROM ewallet.transaction WHERE id=? AND type = ? AND source_id = ? AND price = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, id);
			pstmt.setString(2, type);
			pstmt.setInt(3, source_id);
			pstmt.setInt(4, price);
			ResultSet rset = pstmt.executeQuery();
			if (rset.next()) {
				Transaction = load(rset);
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
		return Transaction;
	}

	/**
	 * @param id
	 * @return
	 */
	public static ArrayList<TransactionInfo> list(int source_id, int status) {
		ArrayList<TransactionInfo> Transactions = new ArrayList<TransactionInfo>();
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "SELECT * FROM ewallet.transaction WHERE source_id = ? AND status = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, source_id);
			pstmt.setInt(2, status);
			ResultSet rset = pstmt.executeQuery();
			while (rset.next()) {
				TransactionInfo info = load(rset);
				Transactions.add(info);
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
		return Transactions;
	}

	public static ArrayList<TransactionInfo> listByTime(int source_id,
			Date time_start, Date time_end, int status) {
		ArrayList<TransactionInfo> Transactions = new ArrayList<TransactionInfo>();
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "SELECT * FROM ewallet.transaction WHERE source_id = ? AND status = ? AND update_date between ? AND ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, source_id);
			pstmt.setInt(2, status);
			pstmt.setTimestamp(3, new Timestamp(time_start.getTime()));
			pstmt.setTimestamp(4, new Timestamp(time_end.getTime()));
			ResultSet rset = pstmt.executeQuery();
			while (rset.next()) {
				TransactionInfo info = load(rset);
				Transactions.add(info);
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
		return Transactions;
	}

}
