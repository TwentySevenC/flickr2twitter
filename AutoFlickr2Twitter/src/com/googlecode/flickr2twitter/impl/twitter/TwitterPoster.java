/**
 * 
 */
package com.googlecode.flickr2twitter.impl.twitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import com.googlecode.flickr2twitter.core.GlobalDefaultConfiguration;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

/**
 * @author yayu
 *
 */
public class TwitterPoster {
	private static final Logger log = Logger.getLogger(TwitterPoster.class.getName());
	private Twitter twitter = null;

	/**
	 * 
	 */
	public TwitterPoster() {
		super();
	}
	
	public String requestNewToken() throws TwitterException {
		try {
			twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(GlobalDefaultConfiguration.getInstance().getTwitterConsumerId(), 
					GlobalDefaultConfiguration.getInstance().getTwitterConsumerSecret());
			RequestToken requestToken = twitter.getOAuthRequestToken();
			log.info("Open the following URL and grant access to your account:");
			log.info(requestToken.getAuthorizationURL());
			return requestToken.getAuthorizationURL();
		} catch (TwitterException e) {
			twitter = null;
			throw e;
		}
	}
	
	public String readyTwitterAuthorization() throws TwitterException {
		StringBuffer buf = new StringBuffer();
		if (twitter != null) {
			AccessToken accessToken = twitter.getOAuthAccessToken();
			buf.append(" User Id: " + accessToken.getUserId());
			buf.append(" User Screen Name: " + accessToken.getScreenName());
			buf.append(" Access Token: " + accessToken.getToken());
			buf.append(" Token Secret: " + accessToken.getTokenSecret());
			/*PersistenceManagerFactory pmf = MyPersistenceManagerFactory.getInstance();
			PersistenceManager pm = pmf.getPersistenceManager();

			try {
				List<UserConfiguration> users = MyPersistenceManagerFactory.getAllUsers();
				if (users.isEmpty() == false) {
					UserConfiguration user = users.get(0);
					user = pm.getObjectById(UserConfiguration.class, user.getFlickrUserId());
					user.setTwitterUserId(String.valueOf(twitter.verifyCredentials().getId()));
					user.setTwitterUserName(accessToken.getScreenName());
					user.setTwitterAccessToken(accessToken.getToken());
					user.setTwitterTokenSecret(accessToken.getTokenSecret());
				}
			} finally {
				pm.close();
			}*/
		}
		return buf.toString();
	}
	
	public static void main(String[] args) {
		try {
			TwitterPoster twitter = new TwitterPoster();
			twitter.requestNewToken();
			BufferedReader infile =
		          new BufferedReader ( new InputStreamReader (System.in) );
		        String line = infile.readLine();
			System.out.println(twitter.readyTwitterAuthorization());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
