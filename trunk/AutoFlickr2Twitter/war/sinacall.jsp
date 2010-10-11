<%@ page contentType="text/html;charset=utf-8" %>
<%@ page language="java" import="com.gmail.yuyang226.autoflickr2twitter.sina.weibo4j.*" %>
<%@ page language="java" import="com.gmail.yuyang226.autoflickr2twitter.sina.weibo4j.http.*" %>

<%
	System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
    System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
    Weibo weibo = new Weibo();
    
	RequestToken resToken = weibo.getOAuthRequestToken();

%>
<a href="<%=resToken.getAuthorizationURL()%>" target="_new">Authorize Sina, After done, paste the pin into the following text field, then OK</a>
<p/>
<html>
	<body>
		<form action="sinacallback" method="post">
			<LABEL for="verifier">Please paste the pin code here: </LABEL>
			<input type="text" name="verifier"/>
			<input type="hidden" name="token" value="<%=resToken.getToken()%>"/> 
			<input type="hidden" name="secret" value="<%=resToken.getTokenSecret()%>"/>
			<input type="submit" value="OK"/>
		</form>
	</body>
</html>