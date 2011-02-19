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
		<div id="left">
			<ul>
				<li><h3>Set the Seller's ID you want to follow</h3></li>
				<form action="/ebayConfig" method="post" name="frmEbay">
					<table class="border_table">
						<tr>
							<td>Seller Id:</td>
							<td>
								<input type="text" name="<%=EbayConfigServlet.PARA_SELLER_ID%>"/>
							</td>
						</tr>
						<tr>
				            <td>
				            	<input type="submit" value="OK"/>
				            </td>
				        </tr>
					</table>
				</form>
			</ul>
		</div>
	</div>
	<%@ include file="footer.jsp"%>
</div>
</body>
</html>