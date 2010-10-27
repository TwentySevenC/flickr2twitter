
<%@ page language="java"
	import="com.googlecode.flickr2twitter.datastore.*,com.googlecode.flickr2twitter.datastore.model.*,com.googlecode.flickr2twitter.servlet.*,com.googlecode.flickr2twitter.core.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
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
<h1>Twitter the world system</h1>

<%
	com.googlecode.flickr2twitter.datastore.model.User user = (com.googlecode.flickr2twitter.datastore.model.User) session
			.getAttribute(UserAccountServlet.PARA_SESSION_USER);
%>

<table>
	<tr>
		<td>|&nbsp;<a href="/index.jsp">Index</a>&nbsp;</td>
		<td>|&nbsp;<a href="/register.jsp">Register</a> &nbsp;</td>
		<td>|&nbsp;<a href="/authorize.jsp">Authorize Source &amp;
		Target</a> &nbsp;</td>
		<td>|&nbsp;<a href="/user_admin.jsp">Manage Your Account</a>
		&nbsp;</td>
		<%
			if (user != null
					&& com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory.Permission.ADMIN.name().equals(user.getPermission()) == true) {
		%>
		<td>|&nbsp;<a href="/system_admin.jsp">System Admin</a> &nbsp;</td>
		<%
			}
		%>
		<td>|&nbsp;<a href="/about.jsp">About &amp; Help</a>&nbsp;|</td>

		<%
			if (user != null) {
		%>
		<td>&nbsp;Welcome <b><%=user.getScreenName()%></b>&nbsp;</td>
		<%
			}
		%>
	</tr>
</table>
<%
	String message = (String) session.getAttribute("message");
	if (message != null && message.trim().length() > 0) {
		message = message.replace("\r", "<br/>");
%>
<h2>Message Board</h2>
<div style="background-color: #EEEEEE;"><%=message%></div>
<%
	session.removeAttribute("message");
	}
%>

<%
	boolean checkLogin = Boolean.TRUE.equals(request
			.getAttribute("checkLogin"));
	if (user == null && checkLogin == true) {
%>
<h2>Login</h2>
<form action="/userOperation" method="post">
<table>

	<tr>
		<td>User Name:</td>
		<td><input type="text" name="<%=UserAccountServlet.PARA_EMAIL%>"></input></td>
	</tr>
	<tr>
		<td>Password:</td>
		<td><input type="password"
			name="<%=UserAccountServlet.PARA_PASSWORD%>"></input><input
			type="hidden" name="<%=UserAccountServlet.PARA_OPT%>"
			value="<%=UserAccountServlet.OPT_LOGIN%>"></input></td>

	</tr>
	<tr>
		<td><input type="submit" value="Login" /></td>
		<td>or click <a href="/register.jsp">here</a> to create your
		account for free!</td>
	</tr>

</table>
</form>
<%@ include file="/foot.jsp"%>
<%
	return;
	}
%>
</body>
</html>