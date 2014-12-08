<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.sql.*" %>
<%@page import="tw.idv.shanedai.util.db.*" %>
<%@page import="com.sci.ewallet.*" %>
<%@page import="com.sci.ewallet.bean.*" %>
<%@page import="com.sci.ewallet.dao.*" %>
<%@page import="com.sci.ewallet.service.*" %>
<%
	int STATUS_SUCCESS = 0;
	int STATUS_FAIL = 1;
	String account = request.getParameter("account");
	int mailCode = Integer.parseInt(request.getParameter("mailCode"));
	StatusBean bean = new EWallet().askSMSCheck(account, mailCode);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>SMS CHECK</title>
</head>
<body>
<b><%= bean.message %></b>
</body>
</html>