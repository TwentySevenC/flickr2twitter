package com.gmail.yuyang226.autoflickr2twitter.servlet;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.yuyang226.autoflickr2twitter.client.AutoFlickr2TwitterService;
import com.gmail.yuyang226.autoflickr2twitter.core.ServiceFactory;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.User;
import com.gmail.yuyang226.autoflickr2twitter.server.AutoFlirckr2TwitterServiceImpl;

public class OAuthServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(OAuthServlet.class
			.getName());

	public static final String OPT_AUTH_SOURCE = "Auth_Source";
	public static final String OPT_AUTH_TARGET = "Auth_Target";

	public static final String OPT_AUTH_SOURCE_CONFIRM = "Auth_Source_Confirm";
	public static final String OPT_AUTH_TARGET_CONFIRM = "Auth_Target_Confirm";

	public static final String OPT_TEST_AUTH = "Auth_Test";

	public static final String PARA_OPT = "operation";

	public static final String PARA_PROVIDER_ID = "provider_id";

	public static final String PARA_SESSION_FLICKER_AUTH_TOKEN = "flicker_auth_token";
	public static final String PARA_SESSION_TWITTER_AUTH_TOKEN = "twitter_auth_token";

	private AutoFlickr2TwitterService authService = new AutoFlirckr2TwitterServiceImpl();

	@SuppressWarnings("unchecked")
	private void doAuthConfirm(HttpServletRequest req,
			HttpServletResponse resp, boolean sourceProvider, StringBuffer msg) {
		String providerId = req.getParameter(PARA_PROVIDER_ID);
		User user = (User) req.getSession().getAttribute(
				UserAccountServlet.PARA_SESSION_USER);
		String userEmail = user.getUserId().getEmail();
		try {

			Map<String, Object> data = (Map<String, Object>) req.getSession()
					.getAttribute(providerId);
			authService.testToken(sourceProvider, providerId, userEmail, data);

			String retMsg = null;
			if (sourceProvider == true) {
				retMsg = ServiceFactory.getSourceServiceProvider(providerId)
						.readyAuthorization(userEmail, data);
			} else {
				retMsg = ServiceFactory.getTargetServiceProvider(providerId)
						.readyAuthorization(userEmail, data);
			}
			log.info(retMsg);

			msg.append("Auth successful!");

			req.getSession().removeAttribute(providerId);

		} catch (Exception e) {
			e.printStackTrace();
			msg.append("Auth confirm faild. Provider ID is: " + providerId
					+ ". Error message is:" + e.getMessage());
		}
	}

	private void testAuth(HttpServletRequest req, HttpServletResponse resp,
			StringBuffer msg) {

	}

	private void doAuth(HttpServletRequest req, HttpServletResponse resp,
			boolean sourceProvider, StringBuffer msg) {
		String providerId = req.getParameter(PARA_PROVIDER_ID);
		try {
			// FIXME: shall we store the token now? if session times out, this
			// token will be lost forever.
			Map<String, Object> data = authService.authorize(sourceProvider,
					providerId);

			req.getSession().setAttribute(providerId, data);
			String tokenUrl = String.valueOf(data.get("url"));
			resp.sendRedirect(tokenUrl);

		} catch (Exception e) {
			e.printStackTrace();
			msg.append("Auth faild. Provider ID is: " + providerId
					+ ". Error message is:" + e.getMessage());
		}
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		User user = (User) req.getSession().getAttribute(
				UserAccountServlet.PARA_SESSION_USER);
		if (user == null) {
			req.getSession().setAttribute("message", "Please Login first!");
			resp.sendRedirect("/index.jsp");
			return;
		}

		String operation = req.getParameter(PARA_OPT);
		StringBuffer msg = new StringBuffer();
		try {
			if (OPT_AUTH_SOURCE.equalsIgnoreCase(operation)) {
				doAuth(req, resp, true, msg);
				return;
			} else if (OPT_AUTH_TARGET.equalsIgnoreCase(operation)) {
				doAuth(req, resp, false, msg);
				return;
			} else if (OPT_AUTH_SOURCE_CONFIRM.equalsIgnoreCase(operation)) {
				doAuthConfirm(req, resp, true, msg);
			} else if (OPT_AUTH_TARGET_CONFIRM.equalsIgnoreCase(operation)) {
				doAuthConfirm(req, resp, false, msg);
			} else if (OPT_TEST_AUTH.equalsIgnoreCase(operation)) {
				testAuth(req, resp, msg);
			}
		} catch (Exception ex) {
			msg.append("Exception occured:\n");
			msg.append(ex.getMessage());
		} finally {
			if (msg.length() > 0) {
				req.getSession().setAttribute("message", msg.toString());
			}
			resp.sendRedirect("/authorize.jsp");
		}
	}

}
