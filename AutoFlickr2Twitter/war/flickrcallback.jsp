<%@ page language="java"
	import="com.googlecode.flickr2twitter.datastore.*,com.googlecode.flickr2twitter.datastore.model.*,com.googlecode.flickr2twitter.servlet.*,java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<%
	String currentProviderID = null;
	currentProviderID = "flickr";
	User user = (User) session
			.getAttribute(UserAccountServlet.PARA_SESSION_USER);
	Map<String, Object> currentData = (Map<String, Object>) session
			.getAttribute(currentProviderID);
	String frob = request.getParameter("frob");
	if (frob != null) {
		currentData.put("frob", frob);
	}
	if (currentData == null || user == null) {
		session.setAttribute("message",
				"Authorize not done. Please login first.");
		response.sendRedirect("/index.jsp");

	} else {
		String url = "/oauth?" + OAuthServlet.PARA_OPT + "="
				+ OAuthServlet.OPT_AUTH_SOURCE_CONFIRM + "&"
				+ OAuthServlet.PARA_PROVIDER_ID + "="
				+ currentProviderID;
		response.sendRedirect(url);
	}
%>
