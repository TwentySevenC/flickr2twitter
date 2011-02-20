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
  
  function highlightSelectedMenuItem() {
	  var path = location.pathname;
	  var headUl = document.getElementById("header_ul");
	  var links = headUl.getElementsByTagName("a");
	  for(i=0; i<links.length; i++) {
		  link = links[i];
		  if(path.indexOf(link.getAttribute("href")) != -1){
			  link.style.backgroundColor="#FEBE43";
			  break;
		  }
	  }
  }
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
		<div id="logo_w1">Social Hub
			<span id="header_span">
				<a href="https://appengine.google.com/"><img src="http://code.google.com/appengine/images/appengine-silver-120x30.gif" alt="Google App Engine" /></a>
			</span>
		</div>
		<div id="logo_w2"></div>
		<div id="header_text">
			
		</div>
		<ul id="header_ul">
			<li><a href="index.jsp">Home</a></li>
			<% if (signedIn == true && MyPersistenceManagerFactory
					.Permission.ADMIN.name().equals(user.getPermission())) {
				//only admin user could see the register page
				%>
				<li><a href="register.jsp">Register</a></li>
			<%	
			}
			%>
			<li><a href="authorize.jsp">Authorize</a></li>
			<li><a href="user_admin.jsp">Manage</a></li>
			<li><a href="about.jsp">Help</a></li>
			<% if (signedIn == true) { %>
			<li><a href="logout.jsp">Logout</a></li>
			<% } %>
		</ul>
		<script type="text/javascript">
			highlightSelectedMenuItem();
		</script>
</div>