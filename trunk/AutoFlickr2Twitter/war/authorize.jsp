<%@ page language="java"
	import="com.gmail.yuyang226.autoflickr2twitter.datastore.*,com.gmail.yuyang226.autoflickr2twitter.datastore.model.*,com.gmail.yuyang226.autoflickr2twitter.servlet.*,java.util.*"
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
			<%
				List<UserSourceService> sourceSvcs = user.getSourceServices();
				List<UserTargetService> targetSvcs = user.getTargetServices();

				String currentProviderID = null;
				currentProviderID = "flickr";
				Map<String, Object> currentData = (Map<String, Object>) session
						.getAttribute(currentProviderID);
			%>
			<td><a
				href="/oauth?<%=OAuthServlet.PARA_OPT%>=<%=OAuthServlet.OPT_AUTH_SOURCE%>&<%=OAuthServlet.PARA_PROVIDER_ID%>=<%=currentProviderID%>"
				target="_new">Authorize Flicker Account</a></td>
			<td>
			<%
				if (currentData == null) {
					out.print(" <-Please click the link on the left side. It will lead you to the offical authorize page. After authorization, please refresh this page and click the Confirm Authorize link. ");
				} else {
			%>
				<a href="/oauth?<%=OAuthServlet.PARA_OPT%>=<%=OAuthServlet.OPT_AUTH_SOURCE_CONFIRM%>&<%=OAuthServlet.PARA_PROVIDER_ID%>=<%=currentProviderID%>">Confirm Authorize</a>
			<%
				}
			
				out.println(" Already Authorized Accounts: ");
				for( UserSourceService sourceSvc : sourceSvcs){
					if(currentProviderID.equalsIgnoreCase(sourceSvc.getSourceServiceProviderId())){
						out.println(sourceSvc.getServiceUserName() + "     ");
					}
				}
			%>
			</td>
		</tr>

	</table>

	<li>
	<h3>Target List</h3>
	</li>
	<table>
		<tr>
			<td>
			<%
			
				currentProviderID = "twitter";
				currentData = (Map<String, Object>) session
						.getAttribute(currentProviderID);
			%>
			<a
				href="/oauth?<%=OAuthServlet.PARA_OPT%>=<%=OAuthServlet.OPT_AUTH_TARGET%>&<%=OAuthServlet.PARA_PROVIDER_ID%>=twitter"
				target="_new">Authorize Twitter Account</a></td>
			<td>
			<%
				if (currentData == null) {
					out.print(" <-Please click the link on the left side. It will lead you to the offical authorize page. After authorization, please refresh this page and click the Confirm Authorize link. ");
				} else {
			%>
				<a href="/oauth?<%=OAuthServlet.PARA_OPT%>=<%=OAuthServlet.OPT_AUTH_TARGET_CONFIRM%>&<%=OAuthServlet.PARA_PROVIDER_ID%>=<%=currentProviderID%>">Confirm Authorize</a>
			<%
				}
				out.println(" Already Authorized Accounts: ");
				for( UserTargetService targetSvc : targetSvcs){
					if(currentProviderID.equalsIgnoreCase(targetSvc.getTargetServiceProviderId())){
						out.println(targetSvc.getServiceUserName() + "    ");
					}
				}
			%>
			</td>
		</tr>

		<tr>
			<td><a href="/sinacall.jsp" target="_new">Authorize Sina
			Account</a></td>
			<td>Here we can add a button so user can test the authorize
			result</td>
		</tr>
	</table>

</ul>
<%@ include file="/foot.jsp"%>
</body>
</html>