/**
 * 
 */
package com.googlecode.flickr2twitter.impl.sina;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import twitter4j.GeoLocation;

import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig;
import com.googlecode.flickr2twitter.intf.ITargetServiceProvider;
import com.googlecode.flickr2twitter.model.IGeoItem;
import com.googlecode.flickr2twitter.model.IItem;
import com.googlecode.flickr2twitter.model.IItemList;
import com.googlecode.flickr2twitter.model.IPhoto;
import com.googlecode.flickr2twitter.model.IShortUrl;
import com.googlecode.flickr2twitter.sina.weibo4j.Weibo;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 * 
 */
public class TargetServiceProviderSina implements ITargetServiceProvider {
	public static final String ID = "sina";
	public static final String DISPLAY_NAME = "Sina";

	private static final Logger log = Logger
			.getLogger(TargetServiceProviderSina.class.getName());

	/**
	 * 
	 */
	public TargetServiceProviderSina() {
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
	 * (com.googlecode.flickr2twitter.datastore.model.
	 * GlobalTargetApplicationService,
	 * com.googlecode.flickr2twitter.datastore
	 * .model.UserTargetServiceConfig, java.util.List)
	 */
	@Override
	public void postUpdate(GlobalTargetApplicationService globalAppConfig,
			UserTargetServiceConfig targetConfig, List<IItemList<IItem>> items)
			throws Exception {
		// api key and secret
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret",
				Weibo.CONSUMER_SECRET);

		Weibo weibo = new Weibo();

		// This is my own sina account, use this will post thing to my sina
		// weibo
		// weibo.setToken("c9e5472c8723cc478a6332ce8b321008",
		// "5c206226f6cd6d87c75c1c60c601d27b");

		weibo.setToken(targetConfig.getServiceAccessToken(),
				targetConfig.getServiceTokenSecret());
		for (IItemList<IItem> itemList : items) {
			log.info("Processing items from: " + itemList.getListTitle());
			for (IItem item : itemList.getItems()) {
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
						if (geoLoc != null) {
							weibo.updateStatus(message, geoLoc.getLatitude(),
									geoLoc.getLongitude());
						} else {
							weibo.updateStatus(message);
						}
					} catch (Exception e) {
						log.warning("Failed posting message ->" + message
								+ ". Cause: " + e);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.gmail.yuyang226.autoflickr2twitter.intf.IServiceAuthorizer#
	 * readyAuthorization(java.lang.String, java.util.Map)
	 */
	@Override
	public String readyAuthorization(String userEmail, Map<String, Object> data)
			throws Exception {
		if (data == null || data.containsKey("token") == false) {
			throw new IllegalArgumentException("Invalid data: " + data);
		}
		StringBuffer buf = new StringBuffer();

		String token = String.valueOf(data.get("token"));
		String secret = String.valueOf(data.get("secret"));

		for (UserTargetServiceConfig service : MyPersistenceManagerFactory
				.getUserTargetServices(userEmail)) {
			if (token.equals(service.getServiceAccessToken())
					&& secret.equals(service.getServiceTokenSecret())) {
				throw new IllegalArgumentException(
						"Target already registered: " + ID);
			}
		}

		UserTargetServiceConfig service = new UserTargetServiceConfig();
		service.setServiceProviderId(ID);
		service.setServiceAccessToken(token);
		service.setServiceTokenSecret(secret);
		service.setServiceUserId(userEmail);
		service.setUserEmail(userEmail);
		service.setServiceUserName(userEmail);
		service.setUserSiteUrl("http://www.sina.com.cn");
		MyPersistenceManagerFactory.addTargetServiceApp(userEmail, service);

		buf.append(ID).append(token).append(secret);
		return buf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.gmail.yuyang226.autoflickr2twitter.intf.IServiceAuthorizer#
	 * requestAuthorization()
	 */
	@Override
	public Map<String, Object> requestAuthorization(String baseUrl) throws Exception {
		// TODO Auto-generated method stub
		return null;
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
		result.setDescription("The MaLeGeBi's leading online micro-blog service");
		result.setTargetAppConsumerId(Weibo.CONSUMER_KEY);
		result.setTargetAppConsumerSecret(Weibo.CONSUMER_SECRET);
		result.setAuthPagePath(null); // TODO set the default auth page path
		result.setImagePath(null); // TODO set the default image path
		return result;
	}
}
