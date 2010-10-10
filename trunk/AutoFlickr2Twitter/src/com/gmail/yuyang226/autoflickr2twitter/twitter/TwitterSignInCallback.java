/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.twitter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.yuyang226.autoflickr2twitter.core.GlobalDefaultConfiguration;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.AccessToken;
import twitter4j.http.OAuthAuthorization;
import twitter4j.http.RequestToken;

/**
 * @author haozhou
 * 
 */
public class TwitterSignInCallback extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2749531190784856800L;
	private static final Logger log = Logger
			.getLogger(TwitterSignInCallback.class.getName());

	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		final String consumerKey = GlobalDefaultConfiguration.getInstance()
				.getTwitterConsumerId();
		final String consumerSecret = GlobalDefaultConfiguration.getInstance()
				.getTwitterConsumerSecret();

		ConfigurationBuilder builder = new ConfigurationBuilder();
		OAuthAuthorization auth = new OAuthAuthorization(builder.build(),
				consumerKey, consumerSecret);
		try {
			String requestToken = "";
			String requestTokenSecret = "";
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("requestToken"))
					requestToken = cookie.getValue();
				else if (cookie.getName().equals("requestTokenSecret"))
					requestTokenSecret = cookie.getValue();
			}
			AccessToken accessToken = auth
					.getOAuthAccessToken(new RequestToken(requestToken,
							requestTokenSecret));
			auth.setOAuthAccessToken(accessToken);
			String token = accessToken.getToken();
			String secret = accessToken.getTokenSecret();
			response.getWriter().println(token);
			response.getWriter().println(secret);
			Twitter twitter = new TwitterFactory().getInstance(auth);
			User user = twitter.verifyCredentials();
			response.getWriter().println(user.getScreenName());
			response.getWriter().println(user.getTimeZone());
			response.getWriter().println(user.getFollowersCount());
		} catch (Exception e) {
			log.log(Level.SEVERE, "callback error", e);
		}
	}
}
