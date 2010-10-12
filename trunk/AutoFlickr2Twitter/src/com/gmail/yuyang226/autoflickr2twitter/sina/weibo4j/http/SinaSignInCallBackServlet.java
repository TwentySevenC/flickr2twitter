package com.gmail.yuyang226.autoflickr2twitter.sina.weibo4j.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.yuyang226.autoflickr2twitter.datastore.model.User;
import com.gmail.yuyang226.autoflickr2twitter.impl.sina.TargetServiceProviderSina;
import com.gmail.yuyang226.autoflickr2twitter.servlet.UserAccountServlet;
import com.gmail.yuyang226.autoflickr2twitter.sina.weibo4j.Weibo;
import com.gmail.yuyang226.autoflickr2twitter.sina.weibo4j.WeiboException;

public class SinaSignInCallBackServlet extends HttpServlet {

	private static final Logger log = Logger
			.getLogger(SinaSignInCallBackServlet.class.getName());
	private static final long serialVersionUID = -1016731113426658126L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//get user information from session
		User currentUser = (User) req.getSession().getAttribute(UserAccountServlet.PARA_SESSION_USER);
		if( currentUser == null ) {
			resp.getWriter().println("Use not signed in, please sign in first.");
			return;
		}


		// Get the verifier;
		String verifier = req.getParameter("verifier");
		log.log(Level.INFO, "oauth_verifier = " + verifier);

		// Request token and secret
		String requestToken = req.getParameter("token");
		String requestTokenSecret = req.getParameter("secret");

		if (verifier == null) {
			resp.getWriter().println("Error to get the verifier.");
			return;
		}

		if (requestTokenSecret == null || requestToken == null) {
			resp.getWriter().println("Error to get request token from cookie.");
			return;
		}

		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret",
				Weibo.CONSUMER_SECRET);

		Weibo weibo = new Weibo();
		try {
			AccessToken accessToken = weibo.getOAuthAccessToken(requestToken, requestTokenSecret,
					verifier);
			if (accessToken == null) {
				resp.getWriter().println("Error to get access token.");
				return;
			} else {
				resp.getWriter().println("Authorization done.");
				
				//save the access token into db
				TargetServiceProviderSina tps = new TargetServiceProviderSina();
				Map<String,Object> data = new HashMap<String,Object>();
				data.put("token",accessToken.getToken());
				data.put("secret", accessToken.getTokenSecret());
				try {
					tps.readyAuthorization(currentUser.getUserId().getEmail(), data);
				} catch (Exception e) {
					resp.getWriter().println("<p>But error to save the access token." + e.getMessage());
					log.log(Level.WARNING, e.getMessage());
				}
				return;
			}
		} catch (WeiboException e) {
			resp.getWriter().println("Error: " + e.getMessage());
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req,resp);
	}
	
	

}
