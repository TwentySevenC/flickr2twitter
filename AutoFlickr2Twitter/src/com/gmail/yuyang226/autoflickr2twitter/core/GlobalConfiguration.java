/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import twitter4j.http.AccessToken;

import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.util.IOUtilities;

/**
 * @author yayu
 *
 */
public final class GlobalConfiguration {
	public static String KEY_FLICKR_APIKEY = "apiKey";
	public static String KEY_FLICKR_SECRET = "secret";
	
	public static String KEY_TWITTER_CONSUMERID = "consumerId";
	public static String KEY_TWITTER_CONSUMERSECRET = "consumerSecret";
	public static String KEY_TWITTER_ACCESSTOKEN = "accessToken"; 
	public static String KEY_TWITTER_TOKENSECRET = "tokenSecret";
	
	public static String KEY_UPDATE_INTERVAL = "interval";
	
	private String flickrApiKey;
	private String flickrSecret;
	
	private String twitterConsumerId;
	private String twitterConsumerSecret;
//	private String twitterAccessToken;
//	private String twitterTokenSecret;
	
	private long interval = 600000L; //10 mins
	
	
	private Properties properties = null;
	
	private static final GlobalConfiguration INSTANCE;
	static {
		INSTANCE = new GlobalConfiguration();
	}
	
	public static GlobalConfiguration getInstance() {
		return INSTANCE;
	}
	
	/**
	 * 
	 */
	private GlobalConfiguration() {
		super();
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void init() throws IOException {
		InputStream in = null;
        try {
            in = getClass().getResourceAsStream("setup.properties");
            properties = new Properties();
            properties.load(in);
        } finally {
            IOUtilities.close(in);
        }
        this.flickrApiKey = properties.getProperty(KEY_FLICKR_APIKEY, null);
        this.flickrSecret = properties.getProperty(KEY_FLICKR_SECRET, null);
        
        this.twitterConsumerId = properties.getProperty(KEY_TWITTER_CONSUMERID, null);
        this.twitterConsumerSecret = properties.getProperty(KEY_TWITTER_CONSUMERSECRET, null);
        /*this.twitterAccessToken = properties.getProperty(KEY_TWITTER_ACCESSTOKEN, null);
        this.twitterTokenSecret = properties.getProperty(KEY_TWITTER_TOKENSECRET, null);*/
        
        try {
        	this.interval = Long.parseLong(properties.getProperty(KEY_UPDATE_INTERVAL));
        } catch (Exception e) {
        	//ignore
        }
	}

	public String getFlickrApiKey() {
		return flickrApiKey;
	}

	public String getFlickrSecret() {
		return flickrSecret;
	}

	public String getTwitterConsumerId() {
		return twitterConsumerId;
	}

	public String getTwitterConsumerSecret() {
		return twitterConsumerSecret;
	}

	/*public String getTwitterAccessToken() {
		return twitterAccessToken;
	}

	public String getTwitterTokenSecret() {
		return twitterTokenSecret;
	}*/

	public Properties getProperties() {
		return properties;
	}
	
	/*public AccessToken getTwitterAccessTokenInstance() {
		return new AccessToken(this.twitterAccessToken, this.twitterTokenSecret);
	}*/

	public long getInterval() {
		return interval;
	}

}
