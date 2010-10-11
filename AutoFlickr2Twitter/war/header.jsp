
<%@ page language="java" import="com.gmail.yuyang226.autoflickr2twitter.datastore.*, 
								 com.gmail.yuyang226.autoflickr2twitter.datastore.model.*,
								 com.gmail.yuyang226.autoflickr2twitter.servlet.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<h1>Cost Record System</h1>

<table>
	<tr>
		<td>|&nbsp;<a href="/index.jsp">Index</a>&nbsp;</td>
		<td>|&nbsp;<a href="/register.jsp">Register</a> &nbsp;</td>
		<td>|&nbsp;<a href="/authorize.jsp">Authorize Source &amp; Target</a> &nbsp;</td>
		<td>|&nbsp;<a href="/authorize.jsp">Link Source &amp; Target</a> &nbsp;</td>
		<td>|&nbsp;<a href="/about.jsp">About &amp; Help</a>&nbsp;|</td>
		<%
			User user = (User) session.getAttribute(UserAccountServlet.PARA_SESSION_USER);
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
%>
<h2>Message Board</h2>
<div style="background-color: #EEEEEE;"><%=message%></div>
<%
	session.removeAttribute("message");
	}
%>

<%
boolean checkLogin = Boolean.TRUE.equals(request.getAttribute("checkLogin"));
if (user == null && checkLogin == true) {
%>
<h2>Login </h2>
<form action="/userOperation" method="post">
<table>

	<tr>
		<td>User Name:</td>
		<td><input type="text" name="<%=UserAccountServlet.PARA_EMAIL%>"></input></td>
	</tr>
	<tr>
		<td>Password:</td>
		<td><input type="password" name="<%=UserAccountServlet.PARA_PASSWORD%>"></input><input
			type="hidden" name="<%=UserAccountServlet.PARA_OPT%>" value="<%=UserAccountServlet.OPT_LOGIN%>"></input></td>

	</tr>
	<tr>
		<td><input type="submit" value="Login" /></td>
		<td>or click <a href="/register.jsp">here</a> to create your account for free!</td>
	</tr>

</table>
</form>
<%@ include file="/foot.jsp" %> 
<%
		return;
	}
%>