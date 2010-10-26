<%@ page language="java"
	import="com.googlecode.flickr2twitter.datastore.*,com.googlecode.flickr2twitter.datastore.model.*,com.googlecode.flickr2twitter.servlet.*,java.util.*,com.googlecode.flickr2twitter.core.*,com.googlecode.flickr2twitter.model.*,com.googlecode.flickr2twitter.intf.*,com.googlecode.flickr2twitter.sina.weibo4j.*,com.googlecode.flickr2twitter.sina.weibo4j.http.*"
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

		<%
			
			List<UserSourceServiceConfig> sourceSvcs = MyPersistenceManagerFactory
					.getUserSourceServices(user);
			List<UserTargetServiceConfig> targetSvcs = MyPersistenceManagerFactory
					.getUserTargetServices(user);

			String currentProviderID = null;
			Collection<ISourceServiceProvider<IItem>> sources = ServiceFactory
					.getAllSourceProviders();
			for (ISourceServiceProvider<IItem> sourceProvider : sources) {
				currentProviderID = sourceProvider.getId();
				GlobalSourceApplicationService sourceApp = MyPersistenceManagerFactory
						.getGlobalSourceAppService(sourceProvider.getId());
				Map<String, Object> currentData = (Map<String, Object>) session
						.getAttribute(currentProviderID);
		%>
		<tr>
			<td><a
				href="/oauth?<%=OAuthServlet.PARA_OPT%>=<%=OAuthServlet.OPT_AUTH_SOURCE%>&<%=OAuthServlet.PARA_PROVIDER_ID%>=<%=currentProviderID%>"
				target="_blank">Authorize <%=sourceApp.getAppName()%>
			Account </a></td>
			<td>
			<%
				if (currentData == null) {
						out
								.print(" <-Please click the link on the left side. It will lead you to the offical authorize page. After authorization, please refresh this page and click the Confirm Authorize link. ");
					} else if (sourceApp.getAuthPagePath() == null) {
			%> <a
				href="/oauth?<%=OAuthServlet.PARA_OPT%>=<%=OAuthServlet.OPT_AUTH_SOURCE_CONFIRM%>&<%=OAuthServlet.PARA_PROVIDER_ID%>=<%=currentProviderID%>">Confirm
			Authorize</a> <%
 	}
 %>
			<h4>Already Authorized Accounts:</h4>
			<ul>
				<%
					for (UserSourceServiceConfig sourceSvc : sourceSvcs) {
							if (currentProviderID.equalsIgnoreCase(sourceSvc
									.getServiceProviderId())) {
								out.println("<li>" + sourceSvc.getServiceUserName()
										+ "  <a href=\"" + sourceSvc.getUserSiteUrl()
										+ "\">Go to my page</a></li>");
							}
						}
				%>
			</ul>
			</td>
		</tr>
		<%
			}
		%>


	</table>



	<li>
	<h3>Target List</h3>
	</li>

	<table>

		<%
			Collection<ITargetServiceProvider> targets = ServiceFactory
					.getAllTargetProviders();
			for (ITargetServiceProvider targetProvider : targets) {
				currentProviderID = targetProvider.getId();
				Map<String, Object> currentData = (Map<String, Object>) session
						.getAttribute(currentProviderID);

				GlobalTargetApplicationService targetApp = MyPersistenceManagerFactory
						.getGlobalTargetAppService(targetProvider.getId());
		%>
		<tr>
			<td><a
				href="/oauth?<%=OAuthServlet.PARA_OPT%>=<%=OAuthServlet.OPT_AUTH_TARGET%>&<%=OAuthServlet.PARA_PROVIDER_ID%>=<%=currentProviderID%>"
				target="_blank">Authorize <%=targetApp.getAppName()%>
			Account </a></td>
			<td>
			<%
				if (currentData == null) {
						out.print(" <-Please click the link on the left side. It will lead you to the offical authorize page. After authorization, please refresh this page and click the Confirm Authorize link. ");
				} else if (targetApp.getAuthPagePath() == null) {
			%> <a
				href="/oauth?<%=OAuthServlet.PARA_OPT%>=<%=OAuthServlet.OPT_AUTH_TARGET_CONFIRM%>&<%=OAuthServlet.PARA_PROVIDER_ID%>=<%=currentProviderID%>">Confirm
			Authorize</a> <%
			 	}
			 %>
			<h4>Already Authorized Accounts:</h4>
			<ul>
				<%
					for (UserTargetServiceConfig targetSvc : targetSvcs) {
							if (currentProviderID.equalsIgnoreCase(targetSvc
									.getServiceProviderId())) {
								out.println("<li>" + targetSvc.getServiceUserName()
										+ "  <a href=\"" + targetSvc.getUserSiteUrl()
										+ "\">Go to my page</a></li>");
							}
						}
				%>
			</ul>
			</td>
		</tr>
		<%
			}
		%>

	</table>

</ul>
<%@ include file="/foot.jsp"%>
</body>
</html>