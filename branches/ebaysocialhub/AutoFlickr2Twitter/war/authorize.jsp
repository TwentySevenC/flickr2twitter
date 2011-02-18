<%@ page language="java"
	import="com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory, com.googlecode.flickr2twitter.datastore.*,com.googlecode.flickr2twitter.datastore.model.*,com.googlecode.flickr2twitter.servlet.*,java.util.*,com.googlecode.flickr2twitter.core.*,com.googlecode.flickr2twitter.model.*,com.googlecode.flickr2twitter.intf.*,com.googlecode.flickr2twitter.sina.weibo4j.*,com.googlecode.flickr2twitter.sina.weibo4j.http.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="stylesheets/style.css" rel="stylesheet" type="text/css" />
<title>SocialHub</title>
</head>

<body>
<div id="container">
	<%@ include file="header.jsp"%>
	<div id="content">
		<%
			if(signedIn) {
				boolean isAdmin = MyPersistenceManagerFactory.Permission.ADMIN.name().equals(user.getPermission());
		%>
		<div id="left">
			<h1>Authorize/Configure source and target accounts</h1>
			<ul>
			<li>Source</li>
					<%
						List<UserSourceServiceConfig> sourceSvcs = MyPersistenceManagerFactory
								.getUserSourceServices(user);
						List<UserTargetServiceConfig> targetSvcs = MyPersistenceManagerFactory
								.getUserTargetServices(user);
			
						String currentProviderID = null;
						Collection<ISourceServiceProvider<IItem>> sources = ServiceFactory
								.getAllSourceProviders();
						for (ISourceServiceProvider<IItem> sourceProvider : sources) {
							if (sourceProvider instanceof IAdminServiceProvider && isAdmin == false) {
								continue;
							}
							currentProviderID = sourceProvider.getId();
							GlobalSourceApplicationService sourceApp = MyPersistenceManagerFactory
									.getGlobalSourceAppService(sourceProvider.getId());
						%>
						<p/>
						<% if (sourceProvider instanceof IServiceAuthorizer) {%>
						<a href="/oauth?<%=OAuthServlet.PARA_OPT%>=<%=OAuthServlet.OPT_AUTH_SOURCE%>&<%=OAuthServlet.PARA_PROVIDER_ID%>=<%=currentProviderID%>"
							target="_new"><img src="<%=sourceApp.getImagePath()%>" alt="<%=sourceApp.getDescription()%>"></a>
						<% } else if (sourceProvider instanceof IConfigurableService) {
							IConfigurableService configService = (IConfigurableService)sourceProvider;
						%>
						<a href="<%=configService.getConfigPagePath()%>"
							target="_new"><img src="<%=sourceApp.getImagePath()%>" alt="<%=sourceApp.getDescription()%>"></a>
						<%
						   }
						}
						%>
			<li>Target</li>
					<%
						Collection<ITargetServiceProvider> targets = ServiceFactory
								.getAllTargetProviders();
						for (ITargetServiceProvider targetProvider : targets) {
							if (targetProvider instanceof IAdminServiceProvider && isAdmin == false) {
								continue;
							}
							currentProviderID = targetProvider.getId();
							Map<String, Object> currentData = (Map<String, Object>) session
									.getAttribute(currentProviderID);
			
							GlobalTargetApplicationService targetApp = MyPersistenceManagerFactory
									.getGlobalTargetAppService(targetProvider.getId());
					%>
					<p/>
					<a href="/oauth?<%=OAuthServlet.PARA_OPT%>=<%=OAuthServlet.OPT_AUTH_TARGET%>&<%=OAuthServlet.PARA_PROVIDER_ID%>=<%=currentProviderID%>"
					target="_new"><img src="<%=targetApp.getImagePath()%>" alt="<%=targetApp.getDescription()%>"></a>
					<%
						}
					%>
			</ul>
			<p/>
			<hr id="spacehr"/>
			<p/>
			<a href="user_admin.jsp" class="signoutbutton">Manage your accounts</a>
		</div>
		<%
		} else { //user not signed.
			response.sendRedirect("index.jsp");
		}
		%>
		<%@ include file="right.jsp"%>
	</div>
	<%@ include file="footer.jsp"%>
</div>
</body>
</html>