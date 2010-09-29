/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Logger;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.PropertyConfiguration;
import twitter4j.http.AccessToken;
import twitter4j.http.Authorization;
import twitter4j.http.OAuthAuthorization;
import twitter4j.http.RequestToken;

/**
 * @author yayu
 *
 */
public class TwitterPoster {
	private static final Logger log = Logger.getLogger(FlickrIntegrator.class.getName());

	/**
	 * 
	 */
	private TwitterPoster() {
		super();
	}
	
	public static void requestNewToken() throws TwitterException, IOException {
		 // The factory instance is re-useable and thread safe.
	    Twitter twitter = new TwitterFactory().getInstance();
	    twitter.setOAuthConsumer(GlobalConfiguration.getInstance().getTwitterConsumerId(), 
	    		GlobalConfiguration.getInstance().getTwitterConsumerSecret());
	    RequestToken requestToken = twitter.getOAuthRequestToken();
	    AccessToken accessToken = null;
	    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    while (null == accessToken) {
	    	log.info("Open the following URL and grant access to your account:");
	    	log.info(requestToken.getAuthorizationURL());
	    	log.info("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
	      String pin = br.readLine();
	      try{
	         if(pin.length() > 0){
	           accessToken = twitter.getOAuthAccessToken(requestToken, pin);
	         }else{
	           accessToken = twitter.getOAuthAccessToken();
	         }
	      } catch (TwitterException te) {
	        if(401 == te.getStatusCode()){
	        	log.info("Unable to get the access token.");
	        }else{
	          te.printStackTrace();
	        }
	      }
	    }
	    //persist to the accessToken for future reference.
	    storeAccessToken(twitter.verifyCredentials().getId() , accessToken);
	}
	
	public static void updateTwitterStatus(String message) throws TwitterException {
		log.info("Posting message -> " + message);
		// The factory instance is re-useable and thread safe.
		AccessToken accessToken = GlobalConfiguration.getInstance().getTwitterAccessTokenInstance(); 
		PropertyConfiguration conf = new PropertyConfiguration(new Properties());
		
		Authorization auth = new OAuthAuthorization(conf, GlobalConfiguration.getInstance().getTwitterConsumerId(), 
				GlobalConfiguration.getInstance().getTwitterConsumerSecret(), accessToken);
	    Twitter twitter = new TwitterFactory().getInstance(auth);
	    Status status = twitter.updateStatus(message);
	    log.info("Successfully updated the status to [" + status.getText() + "].");
	}

	private static void storeAccessToken(int useId, AccessToken accessToken){
		//store accessToken.getToken()
		//store accessToken.getTokenSecret()
		System.out.println(accessToken.getToken());
		System.out.println(accessToken.getTokenSecret());
	}

	public static void main(String[] args) {
		try {
			TwitterPoster.requestNewToken();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
