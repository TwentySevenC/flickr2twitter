<%@ page contentType="text/html;charset=utf-8" %>
<%@ page language="java" import="com.googlecode.flickr2twitter.sina.weibo4j.*" %>
<%@ page language="java" import="com.googlecode.flickr2twitter.sina.weibo4j.http.*" %>
<%@ page language="java" import="com.googlecode.flickr2twitter.datastore.model.User" %>
<%@ page language="java" import="com.googlecode.flickr2twitter.servlet.UserAccountServlet" %>

<%
	User currentUser = (User) session.getAttribute(UserAccountServlet.PARA_SESSION_USER);
	String email = currentUser.getUserId().getEmail();

	System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
    Weibo weibo = new Weibo();
    
	RequestToken resToken = weibo.getOAuthRequestToken();
	
	StringBuffer sb = new StringBuffer();
	sb.append( "http://charleszq-f2t.appspot.com/sinacallback?t=" );
	sb.append( resToken.getToken() );
	sb.append( "&s=" );
	sb.append( resToken.getTokenSecret() );
	sb.append( "&u=" );
	sb.append( email );
	String url = sb.toString();

%>
<form action="<%=resToken.getAuthorizationURL()%>" method="post">
	<input type="hidden" name="oauth_callback" value="<%=url%>"/>
	<input type="submit" value="Click to authorize sina"/>
</form>

