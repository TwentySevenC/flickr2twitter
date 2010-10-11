<%@ page language="java" import="com.gmail.yuyang226.autoflickr2twitter.datastore.*, 
								 com.gmail.yuyang226.autoflickr2twitter.datastore.model.*,
								 com.gmail.yuyang226.autoflickr2twitter.servlet.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />

<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Twitter the World - Register </title>
</head>
<body>
<%request.setAttribute("checkLogin", false); %>
<%@ include file="/header.jsp"%>

<h2>Register</h2>
<ul>
<li><h3>Create an User Account</h3></li>


<form action="/userOperation" method="post">
<table>

	<tr>
		<td>User Name *:</td>
		<td><input type="text" name="<%=UserAccountServlet.PARA_EMAIL%>"></input></td>
	</tr>
	<tr>
		<td>Display Name:</td>
		<td><input type="text" name="<%=UserAccountServlet.PARA_SCREEN_NAME%>"></input></td>
	</tr>
	<tr>
		<td>Password:</td>
		<td><input type="password" name="<%=UserAccountServlet.PARA_PASSWORD%>"></input>
		<input type="hidden" name="<%=UserAccountServlet.PARA_OPT%>" value="<%=UserAccountServlet.OPT_ADD_USER%>"></input>
	</td>
	</tr>
	<tr>
		<td><input type="submit" value="Create My Account" /></td>
	</tr>

</table>
</form>

</ul>
<%@ include file="/foot.jsp" %> 
</body>
</html>