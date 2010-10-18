/**
 * 
 */
package com.googlecode.flickr2twitter.impl.twitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import twitter4j.GeoLocation;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.PropertyConfiguration;
import twitter4j.http.AccessToken;
import twitter4j.http.Authorization;
import twitter4j.http.OAuthAuthorization;
import twitter4j.http.RequestToken;

import com.googlecode.flickr2twitter.core.GlobalDefaultConfiguration;
import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig;
import com.googlecode.flickr2twitter.intf.ITargetServiceProvider;
import com.googlecode.flickr2twitter.model.IGeoItem;
import com.googlecode.flickr2twitter.model.IItem;
import com.googlecode.flickr2twitter.model.IPhoto;
import com.googlecode.flickr2twitter.model.IShortUrl;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 * 
 */
public class TargetServiceProviderTwitter implements ITargetServiceProvider {
	public static final String ID = "twitter";
	public static final String DISPLAY_NAME = "Twitter";

	private static final Logger log = Logger.getLogger(TwitterPoster.class
			.getName());

	/**
	 * 
	 */
	public TargetServiceProviderTwitter() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.flickr2twitter.intf.ITargetServiceProvider#getId
	 * ()
	 */
	@Override
	public String getId() {
		return ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.flickr2twitter.intf.ITargetServiceProvider#postUpdate
	 * (com.googlecode.flickr2twitter.model.IItem)
	 */
	@Override
	public void postUpdate(GlobalTargetApplicationService globalAppConfig,
			UserTargetServiceConfig targetConfig, List<IItem> items)
			throws Exception {
		// The factory instance is re-useable and thread safe.
		AccessToken accessToken = new AccessToken(
				targetConfig.getServiceAccessToken(),
				targetConfig.getServiceTokenSecret());
		PropertyConfiguration conf = new PropertyConfiguration(new Properties());

		Authorization auth = new OAuthAuthorization(conf,
				globalAppConfig.getTargetAppConsumerId(),
				globalAppConfig.getTargetAppConsumerSecret(), accessToken);
		Twitter twitter = new TwitterFactory().getInstance(auth);

		for (IItem item : items) {
			log.info("Posting message -> " + item + " for "
					+ targetConfig.getServiceUserName());

			GeoLocation geoLoc = null;
			if (item instanceof IGeoItem) {
				if (((IGeoItem) item).getGeoData() != null) {
					IGeoItem geoItem = (IGeoItem) item;
					geoLoc = new GeoLocation(
							geoItem.getGeoData().getLatitude(), geoItem
									.getGeoData().getLongitude());
				}
			}
			String message = null;
			if (item instanceof IPhoto) {
				IPhoto photo = (IPhoto) item;
				message = "My new photo: " + photo.getTitle();
				if (photo instanceof IShortUrl) {
					message += " " + ((IShortUrl) photo).getShortUrl();
				} else {
					message += " " + photo.getUrl();
				}

			}
			if (message != null) {
				try {
					Status status = geoLoc == null ? twitter
							.updateStatus(message) : twitter.updateStatus(
							message, geoLoc);
					log.info("Successfully updated the status ["
							+ status.getText() + "] to user @"
							+ targetConfig.getServiceUserName());
				} catch (TwitterException e) {
					log.warning("Failed posting message ->" + message
							+ ". Cause: " + e);
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.flickr2twitter.intf.IServiceAuthorizer#
	 * readyAuthorization(java.lang.String, java.util.Map)
	 */
	@Override
	public String readyAuthorization(String userEmail, Map<String, Object> data)
			throws Exception {
		if (data == null || data.containsKey("token") == false) {
			throw new IllegalArgumentException("Invalid data: " + data);
		}
		StringBuffer buf = new StringBuffer();
		GlobalTargetApplicationService globalAppConfig = MyPersistenceManagerFactory
				.getGlobalTargetAppService(ID);
		Twitter twitter = new TwitterFactory().getOAuthAuthorizedInstance(
				globalAppConfig.getTargetAppConsumerId(),
				globalAppConfig.getTargetAppConsumerSecret());

		String token = String.valueOf(data.get("token"));
		String secret = String.valueOf(data.get("secret"));
		RequestToken requestToken = new RequestToken(token, secret);
		AccessToken accessToken = twitter.getOAuthAccessToken(requestToken);
		buf.append(" User Id: " + accessToken.getUserId());
		buf.append(" User Screen Name: " + accessToken.getScreenName());
		buf.append(" Access Token: " + accessToken.getToken());
		buf.append(" Token Secret: " + accessToken.getTokenSecret());

		for (UserTargetServiceConfig service : MyPersistenceManagerFactory
				.getUserTargetServices(userEmail)) {
			if (accessToken.getToken().equals(service.getServiceAccessToken())) {
				throw new IllegalArgumentException("Token already registered: "
						+ accessToken.getToken());
			}
		}
		UserTargetServiceConfig service = new UserTargetServiceConfig();
		service.setServiceProviderId(ID);
		service.setServiceAccessToken(accessToken.getToken());
		service.setServiceTokenSecret(accessToken.getTokenSecret());
		service.setServiceUserId(String.valueOf(accessToken.getUserId()));
		service.setUserEmail(userEmail);
		service.setServiceUserName(accessToken.getScreenName());
		service.setUserSiteUrl("http://twitter.com/"
				+ accessToken.getScreenName());
		MyPersistenceManagerFactory.addTargetServiceApp(userEmail, service);
		return buf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.flickr2twitter.intf.IServiceAuthorizer#
	 * requestAuthorization()
	 */
	@Override
	public Map<String, Object> requestAuthorization() throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			GlobalTargetApplicationService globalAppConfig = MyPersistenceManagerFactory
					.getGlobalTargetAppService(ID);
			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(globalAppConfig.getTargetAppConsumerId(),
					globalAppConfig.getTargetAppConsumerSecret());
			RequestToken requestToken = twitter.getOAuthRequestToken();
			log.info("Open the following URL and grant access to your account:");
			log.info(requestToken.getAuthorizationURL());
			result.put("url", requestToken.getAuthorizationURL());
			result.put("token", requestToken.getToken());
			result.put("secret", requestToken.getTokenSecret());
		} catch (TwitterException e) {
			throw e;
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.flickr2twitter.intf.IServiceProvider#
	 * createDefaultGlobalApplicationConfig()
	 */
	@Override
	public GlobalTargetApplicationService createDefaultGlobalApplicationConfig() {
		GlobalTargetApplicationService result = new GlobalTargetApplicationService();
		result.setAppName(DISPLAY_NAME);
		result.setProviderId(ID);
		result.setDescription("The world's leading online micro-blog service");
		result.setTargetAppConsumerId(GlobalDefaultConfiguration.getInstance()
				.getTwitterConsumerId());
		result.setTargetAppConsumerSecret(GlobalDefaultConfiguration
				.getInstance().getTwitterConsumerSecret());
		result.setAuthPagePath(null); // TODO set the default auth page path
		result.setImagePath(null); // TODO set the default image path
		return result;
	}
}