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

import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.OAuthAuthorization;
import twitter4j.http.RequestToken;

/**
 * @author haozhou
 * 
 */
public class SignInWithTwitter extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(SignInWithTwitter.class.getName());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2121769166973786061L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		final String consumerKey = GlobalDefaultConfiguration.getInstance().getTwitterConsumerId();
		final String consumerSecret = GlobalDefaultConfiguration.getInstance().getTwitterConsumerSecret();
		
		ConfigurationBuilder builder = new ConfigurationBuilder();
		OAuthAuthorization auth = new OAuthAuthorization(builder.build(),
				consumerKey, consumerSecret);
		try {
			RequestToken requestToken = auth.getOAuthRequestToken();
			response.addCookie(new Cookie("requestToken", requestToken
					.getToken()));
			response.addCookie(new Cookie("requestTokenSecret", requestToken
					.getTokenSecret()));
			log.fine("auth url " + requestToken.getAuthorizationURL());
			response.sendRedirect(requestToken.getAuthorizationURL());
			
		} catch (Exception e) {
			log.log(Level.SEVERE, "login error", e);
		}
	}
}
