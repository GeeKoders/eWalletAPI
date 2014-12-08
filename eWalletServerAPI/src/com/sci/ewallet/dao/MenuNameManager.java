package com.sci.ewallet.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MenuNameManager {

	static private MenuNameInfo load(ResultSet rset) throws SQLException {
		MenuNameInfo menuName = new MenuNameInfo();
		menuName.id = rset.getInt("id");
		menuName.category_id = rset.getInt("category_id");
		menuName.name = rset.getString("name");
		// menuName.price = rset.getInt("price");
		return menuName;
	}

	public static ArrayList<MenuNameInfo> getList() {

		ArrayList<MenuNameInfo> MenuNames = new ArrayList<MenuNameInfo>();
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "SELECT * FROM ewallet.menu_name ";
			PreparedStatement pstmt = conn.prepareStatement(sql);

			ResultSet rset = pstmt.executeQuery();
			while (rset.next()) {
				MenuNameInfo info = load(rset);
				MenuNames.add(info);
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
		return MenuNames;

	}

}
