<%@ page language="java"
	import="com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory.Permission,com.googlecode.flickr2twitter.datastore.*,com.googlecode.flickr2twitter.datastore.model.*,com.googlecode.flickr2twitter.servlet.*,java.util.*,com.googlecode.flickr2twitter.core.*,com.googlecode.flickr2twitter.model.*,com.googlecode.flickr2twitter.intf.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="stylesheets/style.css" rel="stylesheet" type="text/css" />
<title>Flickr2Twitter</title>
</head>

<body>
<div id="container">
	<%@ include file="header.jsp"%>
	
	<%
		if(signedIn == false || user == null) {
			response.sendRedirect("index.jsp");
			return;
		}
		
		List<UserSourceServiceConfig> sourceSvcs = MyPersistenceManagerFactory
				.getUserSourceServices(user);
		List<UserTargetServiceConfig> targetSvcs = MyPersistenceManagerFactory
				.getUserTargetServices(user);
	%>
	<div id="content">
		<div id="left">
		<h1>Manage your accounts</h1>
		<p>
		<ul>
		<li>Source</li>
		<p/>
		<table class="nobordertable" width="100%">
		<%
			boolean odd = true;
			for( UserSourceServiceConfig src : sourceSvcs ) {
		%>
			<tr>
				<td width="60%" <%if(odd) {%>bgcolor="#CDCDCD"<%}else{%>bgcolor="AEAEAE"<%}%>>
					<a target="_new" href="<%=src.getUserSiteUrl()%>"><%=src.getServiceUserName()%>@<%=src.getServiceProviderId().toUpperCase()%></a>
				</td>
				<td <%if(odd) {%>bgcolor="#CDCDCD"<%}else{%>bgcolor="AEAEAE"<%}%>><a href=""><%=src.isEnabled()?"Disable":"Enable"%></td>
			</tr>
		<%
			odd = !odd;
			}
		%>
		</table><p/>
		<li>Target</li>
		<p/>
		<table class="nobordertable" width="100%"><p/>
		<%
			for( UserTargetServiceConfig tgt : targetSvcs ) {
		%>
			<tr>
				<td width="60%" <%if(odd) {%>bgcolor="#CDCDCD"<%}else{%>bgcolor="AEAEAE"<%}%>>
					<a target="_new" href="<%=tgt.getUserSiteUrl()%>"><%=tgt.getServiceUserName()%>@<%=tgt.getServiceProviderId().toUpperCase()%></a>
				</td>
				<td <%if(odd) {%>bgcolor="#CDCDCD"<%}else{%>bgcolor="AEAEAE"<%}%>><a href=""><%=tgt.isEnabled()?"Disable":"Enable"%></td>
			</tr>
		<%
			odd = !odd;
			}
		%>
		</ul>
		</table><p/>
		</div>

		<%@ include file="right.jsp"%>
	</div>
	<%@ include file="footer.jsp"%>
</div>
</body>
</html>