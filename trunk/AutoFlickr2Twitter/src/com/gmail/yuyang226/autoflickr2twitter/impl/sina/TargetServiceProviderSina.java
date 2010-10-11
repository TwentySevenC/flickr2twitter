/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.impl.sina;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import twitter4j.GeoLocation;

import com.gmail.yuyang226.autoflickr2twitter.datastore.MyPersistenceManagerFactory;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.UserTargetService;
import com.gmail.yuyang226.autoflickr2twitter.intf.ITargetServiceProvider;
import com.gmail.yuyang226.autoflickr2twitter.model.IGeoItem;
import com.gmail.yuyang226.autoflickr2twitter.model.IItem;
import com.gmail.yuyang226.autoflickr2twitter.model.IPhoto;
import com.gmail.yuyang226.autoflickr2twitter.model.IShortUrl;
import com.gmail.yuyang226.autoflickr2twitter.sina.weibo4j.Weibo;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 * 
 */
public class TargetServiceProviderSina implements ITargetServiceProvider {
	public static final String ID = "sina";
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
	 * com.gmail.yuyang226.autoflickr2twitter.intf.ITargetServiceProvider#getId
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
	 * com.gmail.yuyang226.autoflickr2twitter.intf.ITargetServiceProvider#postUpdate
	 * (com.gmail.yuyang226.autoflickr2twitter.datastore.model.
	 * GlobalTargetApplicationService,
	 * com.gmail.yuyang226.autoflickr2twitter.datastore.model.UserTargetService,
	 * java.util.List)
	 */
	@Override
	public void postUpdate(GlobalTargetApplicationService globalAppConfig,
			UserTargetService targetConfig, List<IItem> items) throws Exception {
		// api key and secret
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret",
				Weibo.CONSUMER_SECRET);

		Weibo weibo = new Weibo();

		// This is my own sina account, use this will post thing to my sina
		// weibo
		// weibo.setToken("c9e5472c8723cc478a6332ce8b321008",
		// "5c206226f6cd6d87c75c1c60c601d27b");

		weibo.setToken(targetConfig.getServiceAccessToken(), targetConfig
				.getServiceTokenSecret());
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
				message = photo.getTitle();
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
		/**/
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

		for (UserTargetService service : MyPersistenceManagerFactory
				.getUserTargetServices(userEmail)) {
			if (ID.equals(service.getTargetServiceProviderId())) {
				throw new IllegalArgumentException(
						"Target already registered: " + ID);
			}
		}
		UserTargetService service = new UserTargetService();
		service.setTargetServiceProviderId(ID);
		service.setServiceAccessToken(token);
		service.setServiceTokenSecret(secret);
		service.setServiceUserId(userEmail);
		service.setUserEmail(userEmail);
		service.setServiceUserName(userEmail);
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
	public Map<String, Object> requestAuthorization() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.gmail.yuyang226.autoflickr2twitter.intf.IServiceProvider#createDefaultGlobalApplicationConfig()
	 */
	@Override
	public GlobalTargetApplicationService createDefaultGlobalApplicationConfig() {
		GlobalTargetApplicationService result = new GlobalTargetApplicationService();
		result.setAppName("Sina");
		result.setProviderId(ID);
		result.setDescription("The MaLeGeBi's leading online micro-blog service");
		result.setTargetAppConsumerId("test");
		result.setTargetAppConsumerSecret("test");
		result.setAuthPagePath(null); //TODO set the default auth page path
		result.setImagePath(null); //TODO set the default image path
		return result;
	}

}
