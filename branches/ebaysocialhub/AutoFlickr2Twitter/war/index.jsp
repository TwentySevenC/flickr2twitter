<%@ page language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="stylesheets/style.css" rel="stylesheet" type="text/css" />
<title>SocialHub</title>
</head>

<body>
<div id="container">
	<%@ include file="header.jsp"%>
	<div id="content">
		<%
			if(!signedIn) {
		%>
		<h1>Welcome to SocialHub</h1>
		<hr/>
		<p>You need to sign in first, if you don't have an account here yet, you can use your google or yahoo account to sign in directly.</p>
		<div id="left">
			<form action="/userOperation" method="post" name="frmSign">
				<table class="border_table">
					<tr>
						<td class="first">User Name:</td>
						<td><input type="text" name="<%=UserAccountServlet.PARA_EMAIL%>"></input></td>
					</tr>
					<tr>
						<td>Password:</td>
						<td><input type="password" name="<%=UserAccountServlet.PARA_PASSWORD%>"></input><input
							type="hidden" name="<%=UserAccountServlet.PARA_OPT%>" value="<%=UserAccountServlet.OPT_LOGIN%>"></input></td>
	
					</tr>
					<tr>
						<td></td>
						<td>
							<a href="#" onclick="frmSign.submit();"><img src="/images/button_signin.png" alt=""/></a>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div id="right">
		<p>
			<a href="/openid?op=Google" ><img border="0" src="http://openid.net/images/get-logos/google.png" alt="Google"/></a><br/><br/>
			<a href="/openid?op=Yahoo" ><img border="0" src="http://openid.net/wordpress-content/uploads/2009/11/yahoo.png" alt="Yahoo"/></a>
		</p>
		</div>
		<%
		} else { //user already signed in.
		%>
		<h1>Welcome to SocialHub, <%=user.getScreenName()%>.</h1>
		<hr/>
		<p>Now you can authorize your source and target account if you have not done so, or you can manage your accounts.</p>
		<p>
			<a href="logout.jsp"><img src="/images/button_logout.png" alt=""/></a>
		</p>
		<%
		}
		%>
	</div>
	<%@ include file="footer.jsp"%>
</div>
</body>
</html>