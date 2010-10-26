<%@ page language="java"
	import="com.googlecode.flickr2twitter.datastore.*,
	com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory.Permission,
	com.googlecode.flickr2twitter.datastore.model.*,
	com.googlecode.flickr2twitter.servlet.*,
	java.util.*,
	com.googlecode.flickr2twitter.core.*,
	com.googlecode.flickr2twitter.model.*,
	com.googlecode.flickr2twitter.intf.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />

<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Twitter the World - Authorize your Source and Target</title>
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
</head>
<body>
<%
	request.setAttribute("checkLogin", true);
%>
<%@ include file="/header.jsp"%>
<%
	if (Permission.ADMIN.equals(user.getPermission()) == false) {
		// note admin, return;
		return;
	}
%>
<a href="/userPasswordMigration">Migrate to New Password</a>
<%@ include file="/foot.jsp"%>
</body>
</html>