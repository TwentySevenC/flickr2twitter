<%@ page language="java"
	import="com.googlecode.flickr2twitter.datastore.*,com.googlecode.flickr2twitter.datastore.model.*,com.googlecode.flickr2twitter.servlet.*,java.util.*,com.googlecode.flickr2twitter.core.*,com.googlecode.flickr2twitter.model.*,com.googlecode.flickr2twitter.intf.*"
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

<%
			List<UserSourceServiceConfig> sourceSvcs = MyPersistenceManagerFactory
					.getUserSourceServices(user);
			List<UserTargetServiceConfig> targetSvcs = MyPersistenceManagerFactory
					.getUserTargetServices(user);
			System.out.println( "target services count: " + targetSvcs.size() );

%>
<hr width="80%" align="left"/>
<b>Still under construction</b>

<p>Your source service list:</p>
<table>
<%
	for( UserSourceServiceConfig src : sourceSvcs ) {
%>
		<tr>
			<td><li/></td>
			<td><%=src.getServiceProviderId()%> <a href="<%=src.getUserSiteUrl()%>"><%=src.getServiceUserName()%></a></td>
			<td><a href=""><%=src.isEnabled()?"Disable":"Enable"%></td>
		</tr>
<%
	}
%>
</table>
<hr width="80%" align="left"/>
<p>Your target service list:</p>
<table>
<%
	for( UserTargetServiceConfig tgt : targetSvcs ) {
%>
	<tr>
		<td><li/></td>
		<td><%=tgt.getServiceProviderId()%> <a href="<%=tgt.getUserSiteUrl()%>"><%=tgt.getServiceUserName()%></a></td>
		<td><a href=""><%=tgt.isEnabled()?"Disable":"Enable"%></td>
	</tr>
<%
	}
%>
</table>
</body>
</html>