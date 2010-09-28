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
public final class MyConfiguration {
	public static String KEY_FLICRK_USERID = "userId";
	public static String KEY_FLICKR_APIKEY = "apiKey";
	public static String KEY_FLICKR_SECRET = "secret";
	public static String KEY_FLICKR_TOKEN = "token";
	
	public static String KEY_TWITTER_CONSUMERID = "consumerId";
	public static String KEY_TWITTER_CONSUMERSECRET = "consumerSecret";
	public static String KEY_TWITTER_ACCESSTOKEN = "accessToken"; 
	public static String KEY_TWITTER_TOKENSECRET = "tokenSecret";
	
	public static String KEY_UPDATE_INTERVAL = "interval";
	
	private String flickrUserId;
	private String flickrApiKey;
	private String flickrSecret;
	private String flickrToken;
	
	private String twitterConsumerId;
	private String twitterConsumerSecret;
	private String twitterAccessToken;
	private String twitterTokenSecret;
	
	private long interval = 300000L; //5 mins
	
	
	private Properties properties = null;
	
	private static final MyConfiguration INSTANCE;
	static {
		INSTANCE = new MyConfiguration();
	}
	
	public static MyConfiguration getInstance() {
		return INSTANCE;
	}
	
	/**
	 * 
	 */
	private MyConfiguration() {
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
        this.flickrUserId = properties.getProperty(KEY_FLICRK_USERID, null);
        this.flickrApiKey = properties.getProperty(KEY_FLICKR_APIKEY, null);
        this.flickrSecret = properties.getProperty(KEY_FLICKR_SECRET, null);
        this.flickrToken = properties.getProperty(KEY_FLICKR_TOKEN, null);
        
        this.twitterConsumerId = properties.getProperty(KEY_TWITTER_CONSUMERID, null);
        this.twitterConsumerSecret = properties.getProperty(KEY_TWITTER_CONSUMERSECRET, null);
        this.twitterAccessToken = properties.getProperty(KEY_TWITTER_ACCESSTOKEN, null);
        this.twitterTokenSecret = properties.getProperty(KEY_TWITTER_TOKENSECRET, null);
        
        try {
        	this.interval = Long.parseLong(properties.getProperty(KEY_UPDATE_INTERVAL));
        } catch (Exception e) {
        	//ignore
        }
	}

	public String getFlickrUserId() {
		return flickrUserId;
	}

	public String getFlickrApiKey() {
		return flickrApiKey;
	}

	public String getFlickrSecret() {
		return flickrSecret;
	}

	public String getFlickrToken() {
		return flickrToken;
	}

	public String getTwitterConsumerId() {
		return twitterConsumerId;
	}

	public String getTwitterConsumerSecret() {
		return twitterConsumerSecret;
	}

	public String getTwitterAccessToken() {
		return twitterAccessToken;
	}

	public String getTwitterTokenSecret() {
		return twitterTokenSecret;
	}

	public Properties getProperties() {
		return properties;
	}
	
	public AccessToken getTwitterAccessTokenInstance() {
		return new AccessToken(this.twitterAccessToken, this.twitterTokenSecret);
	}

	public long getInterval() {
		return interval;
	}

}