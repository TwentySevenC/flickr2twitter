package com.gmail.yuyang226.autoflickr2twitter.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.yuyang226.autoflickr2twitter.datastore.MyPersistenceManagerFactory;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.User;
import com.google.appengine.repackaged.com.google.common.base.StringUtil;

public class UserAccountServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// private static final Logger log =
	// Logger.getLogger(UserAccountServlet.class
	// .getName());

	public static final String OPT_ADD_USER = "Add_User";
	public static final String OPT_LOGIN = "Login";

	public static final String PARA_OPT = "operation";

	public static final String PARA_SESSION_USER = "user";

	public static final String PARA_KEY = "user_key";
	public static final String PARA_EMAIL = "user_email";
	public static final String PARA_PASSWORD = "user_password";
	public static final String PARA_SCREEN_NAME = "user_screenName";
	public static final String PARA_PERMISSION = "user_permission";

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		StringBuffer msg = new StringBuffer();

		resp.setContentType("text/plain");
		String operation = req.getParameter(PARA_OPT);

		try {
			if (OPT_ADD_USER.equalsIgnoreCase(operation)) {
				addUserAccount(req, resp, msg);
			} else if (OPT_LOGIN.equalsIgnoreCase(operation)) {
				doLogin(req, resp, msg);
			}
		} catch (Exception ex) {
			msg.append("Exception occured:\n");
			msg.append(ex.getMessage());
		} finally {
			if (msg.length() > 0) {
				req.getSession().setAttribute("message", msg.toString());
			}
			resp.sendRedirect("/index.jsp");
		}
	}

	private void addUserAccount(HttpServletRequest req,
			HttpServletResponse resp, StringBuffer msg) {
		String userEmail = req.getParameter(PARA_EMAIL);
		String password = req.getParameter(PARA_PASSWORD);
		String screenName = req.getParameter(PARA_SCREEN_NAME);

		if (StringUtil.isEmpty(userEmail) == true) {
			msg.append("User Email could not be empty! Creation is not successful.");
			return;
		}

		MyPersistenceManagerFactory.createNewUser(userEmail, password,
				screenName);
	}

	private void doLogin(HttpServletRequest req, HttpServletResponse resp,
			StringBuffer msg) {
		String userEmail = req.getParameter(PARA_EMAIL);
		String password = req.getParameter(PARA_PASSWORD);
		if (StringUtil.isEmpty(userEmail) == true) {
			msg.append("User Email could not be empty! Login failed.");
			return;
		}
		User user = MyPersistenceManagerFactory.getLoginUser(userEmail,
				password);
		if (user == null) {
			msg.append("User name and password not match! Login failed");
		} else {
			msg.append("Login Success!");
			req.getSession().setAttribute(PARA_SESSION_USER, user);
		}

	}

}
