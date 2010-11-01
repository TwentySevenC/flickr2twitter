<%@ page language="java"
	import="com.googlecode.flickr2twitter.impl.photobucket.*,com.googlecode.flickr2twitter.exceptions.*,com.googlecode.flickr2twitter.core.*,java.util.logging.*,com.google.gdata.client.http.AuthSubUtil,com.googlecode.flickr2twitter.datastore.*,com.googlecode.flickr2twitter.datastore.model.*,com.googlecode.flickr2twitter.servlet.*,java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<%
	String currentProviderID = SourceServiceProviderPhotobucket.ID;
	User user = (User)session.getAttribute(UserAccountServlet.PARA_SESSION_USER);
	Map<String, Object> currentData = (Map<String, Object>) session
			.getAttribute(currentProviderID);
	Logger log = Logger.getLogger(SourceServiceProviderPhotobucket.CALLBACK_URL);
	log.info(String.valueOf(request.getParameterMap()));
	String token = request.getParameter(SourceServiceProviderPhotobucket.KEY_OAUTHTOKEN);
	String status = request.getParameter(SourceServiceProviderPhotobucket.KEY_STATUS);
	log.info("Photobucket Access Token: " + token + "; OAuth Status: " + status);
	if (currentData == null || user == null || token == null) {
		session.setAttribute("message","Authorize not done. Please login first.");
		response.sendRedirect("/index.jsp");
	} else if ("ready".equals(status) == false) {
		session.setAttribute("message","User did not authorize this application, status=" + status);
		response.sendRedirect("/authorize.jsp");
	} else {
		currentData.put(SourceServiceProviderPhotobucket.KEY_OAUTHTOKEN, token);
		try {
			String retMsg = null;
				retMsg = ServiceFactory.getSourceServiceProvider(currentProviderID)
				.readyAuthorization(user.getUserId().getEmail(), currentData);
			log.info(retMsg);
		} catch (TokenAlreadyRegisteredException ex) {
			request.getSession().removeAttribute(currentProviderID);
			log.warning("Account \"" + ex.getUserName()
					+ "\" is already authorized. Token for this account is:"
					+ ex.getToken());
		} catch (Exception e) {
			log.warning(e.toString());
		}
		response.sendRedirect(SourceServiceProviderPhotobucket.POST_AUTH_PAGE);
	}
%>
