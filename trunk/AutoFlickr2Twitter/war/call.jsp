<%@ page contentType="text/html;charset=utf-8" %>
<%@ page language="java" import="com.gmail.yuyang226.autoflickr2twitter.sina.weibo4j.http.*" %>
<%@ page language="java" import="com.gmail.yuyang226.autoflickr2twitter.sina.weibo4j.*" %>
<%@ page language="java" import="com.gmail.yuyang226.autoflickr2twitter.datastore.*" %>
<%@ page language="java" import="com.gmail.yuyang226.autoflickr2twitter.datastore.model.*" %>
<%@ page language="java" import="javax.jdo.*" %>

<jsp:useBean id="weboauth" scope="session" class="com.gmail.yuyang226.autoflickr2twitter.sina.weibo4j.examples.WebOAuth" />
<%
if("1".equals(request.getParameter("opt")))
{
	RequestToken resToken=weboauth.request("http://localhost:8888/callback.jsp");
	if(resToken!=null)
	{
		SinaToken token = new SinaToken();
		token.setToken( resToken.getToken() );
		token.setTokenSecret( resToken.getTokenSecret() );
		
		PersistenceManagerFactory pmf = MyPersistenceManagerFactory.getInstance();
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			pm.makePersistent(token);
		} finally {
			pm.close();
		}
		
		System.out.println("Authorization url: " + resToken.getAuthorizationURL());
		response.sendRedirect(resToken.getAuthorizationURL());
	}
	else
	{
		out.println("request error");
	}
}else{
%>
<a href="call.jsp?opt=1">Authorize Sina</a>   
<%}%>