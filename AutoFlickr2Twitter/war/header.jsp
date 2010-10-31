<%@ page language="java"
	import="com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory,com.googlecode.flickr2twitter.datastore.*,com.googlecode.flickr2twitter.datastore.model.*,com.googlecode.flickr2twitter.servlet.*,com.googlecode.flickr2twitter.core.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-19322812-2']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();
</script>

<%
	boolean signedIn = false;
	com.googlecode.flickr2twitter.datastore.model.User user = (com.googlecode.flickr2twitter.datastore.model.User) session
			.getAttribute(UserAccountServlet.PARA_SESSION_USER);
			
	if (user != null ) {
		signedIn = true;
	}
%>

<div id="header">
		<div id="logo_w1">Flickr2Twitter</div>
		<div id="logo_w2"></div>
		<div id="header_text">
			
		</div>
		<ul>
			<li><a href="index.jsp">Home</a></li>
			<% if (signedIn == true && MyPersistenceManagerFactory
					.Permission.ADMIN.name().equals(user.getPermission())) {
				//only admin user could see the register page
				%>
				<li><a href="register.jsp">Register</a></li>
			<%	
			}
			%>
			<li><a href="authorize.jsp">Authorize Source & Target</a></li>
			<li><a href="user_admin.jsp">Manage Accounts</a></li>
			<li><a href="about.jsp">Help</a></li>
		</ul>
</div>