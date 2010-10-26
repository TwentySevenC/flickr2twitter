<%@ page language="java"
	import="java.util.logging.*,java.net.*,com.googlecode.flickr2twitter.datastore.*,com.googlecode.flickr2twitter.datastore.model.*,com.googlecode.flickr2twitter.servlet.*,java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<%
	String extra = request.getParameter("extra");
	String frob = request.getParameter("frob");
	Logger log = Logger.getLogger("flickerredirect.jsp");
	log.info("Flickr Redirect Extra=" + extra + ", frob=" + frob);
	if (frob != null && extra != null) {
		String baseUrl = extra;
		if (baseUrl.contains("%")) {
			String encoding = System.getProperty("file.encoding");
			baseUrl = URLDecoder.decode(extra, encoding);
		}
		
		String url = baseUrl + "?frob=" + frob;
		log.info("Redirecting to->" + url);
		response.sendRedirect(url);
	}
%>
