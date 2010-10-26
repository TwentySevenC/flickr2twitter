<%@ page language="java" import="com.googlecode.flickr2twitter.datastore.*, 
								 com.googlecode.flickr2twitter.datastore.model.*,
								 com.googlecode.flickr2twitter.servlet.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />

<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Twitter the World - Register </title>
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-19322812-2']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>
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