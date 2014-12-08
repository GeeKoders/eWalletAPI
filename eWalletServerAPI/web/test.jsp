<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>  
<%@ page import="javax.sql.*" %>  
<%@ page import="javax.naming.*" %> 
<%@ page import="tw.idv.shanedai.util.db.*" %>
<%
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String message = "";
    try {
		Context initContext = new InitialContext();  
		Context envContext  = (Context)initContext.lookup("java:/comp/env");  
		DataSource ds = (DataSource)envContext.lookup("jdbc/demo");  
		conn = ds.getConnection();
		stmt = conn.createStatement();
		String sql = "select userid,userpassword,username from UserData order by userid";
		rs = stmt.executeQuery(sql);
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Show All User</title>
  </head>
  <body>
  <P><%=message%></p>
  <%
    if(rs!=null){
      %>
      <table border="1">
        <thead>
          <tr>
            <th>User ID</th>
            <th>User Password</th>
            <th>User Name</th>
          </tr>
        </thead>
        <%
        while(rs.next()){
          String uid = rs.getString(1);
          String upwd = rs.getString(2);
          String uname = rs.getString(3);
        %>
        <tbody>
          <tr>
            <td><%=uid%></td>
            <td><%=upwd%></td>
            <td><%=uname%></td>
          </tr>
        </tbody>
        <%
        }
        %>
      </table>
      <%
    }

    } catch (Exception ex) {
		message = ex.getMessage();
		System.out.println(ex);
    } finally {
    	conn.close();  
    	conn=null; 
    }
  %>
  </body>
</html>
  
