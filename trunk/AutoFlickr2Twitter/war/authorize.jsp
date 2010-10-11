<%@ page language="java"
	import="com.gmail.yuyang226.autoflickr2twitter.datastore.*,com.gmail.yuyang226.autoflickr2twitter.datastore.model.*,com.gmail.yuyang226.autoflickr2twitter.servlet.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />

<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Twitter the World - Authorize your Source and Target</title>
</head>
<body>
<%
	request.setAttribute("checkLogin", true);
%>
<%@ include file="/header.jsp"%>

<h2>Authorize</h2>
<ul>
	<li>
	<h3>Source List</h3>
	</li>

	<table>

		<tr>
			<td><a href="/oauth?<%=OAuthServlet.PARA_OPT%>=<%=OAuthServlet.OPT_AUTH_SOURCE %>&<%=OAuthServlet.PARA_PROVIDER_ID%>=flickr">Authorize Flicker Account</a></td>
			<td>Here we can add a button so user can test the authorize
			result</td>
		</tr>

	</table>

	<li>
	<h3>Target List</h3>
	</li>
	<table>
		<tr>
			<td><a href="/oauth?<%=OAuthServlet.PARA_OPT%>=<%=OAuthServlet.OPT_AUTH_SOURCE %>&<%=OAuthServlet.PARA_PROVIDER_ID%>=twitter">Authorize Twitter Account</a></td>
			<td>Here we can add a button so user can test the authorize
			result</td>
		</tr>

		<tr>
			<td><a href="/sinacall.jsp" target="_new">Authorize Sina Account</a></td>
			<td>Here we can add a button so user can test the authorize
			result</td>
		</tr>
	</table>

</ul>
<%@ include file="/foot.jsp"%>
</body>
</html>