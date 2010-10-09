<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<title>Cost Record System</title>
</head>

<body>

<%request.setAttribute("checkLogin", true); %>
<%@ include file="/header.jsp"%>
<p>&nbsp;</p>

<div>Welcome to Cost Record System. Now logined as <b><%=house.getHouseName() %></b>.</div>
<div>You can create a role or a cost record. Click <a href="/about.jsp">here</a> for help.</div>

<h2>Switch to another house account</h2>
<form action="/houseOperation" method="post">
<table>

	<tr>
		<td>House Name:</td>
		<td><input type="text" name="houseName"></input></td>
	</tr>
	<tr>
		<td>Password:</td>
		<td><input type="password" name="password"></input><input
			type="hidden" name="operation" value="login"></input></td>

	</tr>
	<tr>
		<td><input type="submit" value="Login" /></td>
		<td>or click <a href="/house.jsp">here</a> to create a house</td>
	</tr>

</table>
</form>

<%@ include file="/foot.jsp"%>
</body>
</html>
