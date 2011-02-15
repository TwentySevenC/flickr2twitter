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
		<div id="left">
			<h1>Welcome to SocialHub</h1>
			<p>You need to sign in first, if you don't have an account here yet, you can use your google or yahoo account to sign in directly.</p>
			<p/>
			<form action="/userOperation" method="post" name="frmSign">
				<table class="border_table">
					<tr>
						<td>User Name:</td>
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
							<a href="#" onclick="frmSign.submit();"><img src="images/signin_Button.jpg"/></a>
						</td>
					</tr>
				</table>
			</form>
			<p>Or sign in with open ID account:</p>
			<a href="/openid?op=Google" ><img border="0" src="http://openid.net/images/get-logos/google.png" alt="Google"></a><br/>
			<a href="/openid?op=Yahoo" ><img border="0" src="http://openid.net/wordpress-content/uploads/2009/11/yahoo.png" alt="Yahoo"></a>
			<p/>
		</div>
		<%
		} else { //user already signed in.
		%>
		<div id="left">
			<h1>Welcome to SocialHub, <%=user.getScreenName()%>.</h1>
			<p>Now you can authorize your source and target account if you have not done so, or you can manage your accounts.</p>
			<p/>
			<p/>
			<hr/>
			<p/>
			<a href="logout.jsp" class="signoutbutton">Exit</a>			
		</div>
		<%
		}
		%>
		<%@ include file="right.jsp"%>
	</div>
	<%@ include file="footer.jsp"%>
</div>
</body>
</html>