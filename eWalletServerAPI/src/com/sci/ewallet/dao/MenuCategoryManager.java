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

public class MenuCategoryManager {
	static private MenuCategoryInfo load(ResultSet rset) throws SQLException {
		MenuCategoryInfo menuCategory = new MenuCategoryInfo();
		menuCategory.id = rset.getInt("id");
		menuCategory.name = rset.getString("name");
		return menuCategory;
	}

	public static ArrayList<MenuCategoryInfo> getList() {

		ArrayList<MenuCategoryInfo> menuCategorys = new ArrayList<MenuCategoryInfo>();
		try {
			Context initContext = new InitialContext();  
			Context envContext  = (Context)initContext.lookup("java:/comp/env");  
			DataSource ds = (DataSource)envContext.lookup("jdbc/ewallet");  
			Connection conn = ds.getConnection();
			String sql = "SELECT * FROM ewallet.menu_category ";
			PreparedStatement pstmt = conn.prepareStatement(sql);

			ResultSet rset = pstmt.executeQuery();
			while (rset.next()) {
				MenuCategoryInfo info = load(rset);
				menuCategorys.add(info);
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
		return menuCategorys;

	}
}
