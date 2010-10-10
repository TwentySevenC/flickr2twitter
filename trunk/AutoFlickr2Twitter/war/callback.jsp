<%@ page contentType="text/html;charset=utf-8" %>
<%@ page language="java" import="com.gmail.yuyang226.autoflickr2twitter.sina.weibo4j.http.*" %>
<%@ page language="java" import="com.gmail.yuyang226.autoflickr2twitter.sina.weibo4j.*" %>
<%@ page language="java" import="com.gmail.yuyang226.autoflickr2twitter.datastore.*" %>
<%@ page language="java" import="com.gmail.yuyang226.autoflickr2twitter.datastore.model.*" %>

<jsp:useBean id="weboauth" scope="session" class="com.gmail.yuyang226.autoflickr2twitter.sina.weibo4j.examples.WebOAuth" />
<%
	
	String verifier=request.getParameter("oauth_verifier");
	
	if(verifier!=null)
	{
		System.out.println("oauth:"+verifier);
		SinaToken token = MyPersistenceManagerFactory.getSinaToken();

		if(token!=null)
		{
			AccessToken accessToken=weboauth.requstAccessToken(token.getToken(), token.getTokenSecret(),verifier);
				if(accessToken!=null)
				{
					//out.println(accessToken.getToken());
					//out.println(accessToken.getTokenSecret());
					weboauth.update(accessToken,"send weibo with web oauth");
					out.println("Done");				
				}else
					{
					out.println("access token request error");
					}
		
		}
		else
			{
			out.println("request token session error");
			}
	}
	else
		{
		out.println("verifier String error");
		}

%>   