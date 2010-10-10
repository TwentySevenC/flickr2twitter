/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.impl.sina;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import twitter4j.GeoLocation;
import twitter4j.Status;
import twitter4j.TwitterException;

import com.gmail.yuyang226.autoflickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.UserTargetService;
import com.gmail.yuyang226.autoflickr2twitter.impl.twitter.TwitterPoster;
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
	private static final Logger log = Logger.getLogger(TwitterPoster.class.getName());
	
	/**
	 * 
	 */
	public TargetServiceProviderSina() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.gmail.yuyang226.autoflickr2twitter.intf.ITargetServiceProvider#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}

	/* (non-Javadoc)
	 * @see com.gmail.yuyang226.autoflickr2twitter.intf.ITargetServiceProvider#postUpdate(com.gmail.yuyang226.autoflickr2twitter.datastore.model.GlobalTargetApplicationService, com.gmail.yuyang226.autoflickr2twitter.datastore.model.UserTargetService, java.util.List)
	 */
	@Override
	public void postUpdate(GlobalTargetApplicationService globalAppConfig,
			UserTargetService targetConfig, List<IItem> items) throws Exception {
		//api key and secret
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret",
				Weibo.CONSUMER_SECRET);

		Weibo weibo = new Weibo();
		
		//This is my own sina account, use this will post thing to my sina weibo
		weibo.setToken("c9e5472c8723cc478a6332ce8b321008",
				"5c206226f6cd6d87c75c1c60c601d27b");
		for (IItem item : items) {
	    	log.info("Posting message -> " + item + " for " + targetConfig.getServiceUserName());

	    	GeoLocation geoLoc = null;
	    	if (item instanceof IGeoItem) {
	    		if (((IGeoItem)item).getGeoData() != null) {
	    			IGeoItem geoItem = (IGeoItem)item;
	    			geoLoc = new GeoLocation(geoItem.getGeoData().getLatitude(), 
	    					geoItem.getGeoData().getLongitude());
	    		}
	    	}
	    	String message = null;
	    	if (item instanceof IPhoto) {
	    		IPhoto photo = (IPhoto)item;
	    		message = photo.getTitle();
	    		if (photo instanceof IShortUrl) {
	    			message += " " + ((IShortUrl)photo).getShortUrl();
	    		} else {
	    			message += " " + photo.getUrl();
	    		}

	    	}
	    	if (message != null) {
	    		try {
	    			if (geoLoc != null) {
	    				weibo.updateStatus(message, geoLoc.getLatitude(), geoLoc.getLongitude());
	    			} else {
	    				weibo.updateStatus(message);
	    			}
	    		} catch (Exception e) {
	    			log.warning("Failed posting message ->" + message + ". Cause: " + e);
	    		}
	    	}
	    }
		/**/
	}

	/* (non-Javadoc)
	 * @see com.gmail.yuyang226.autoflickr2twitter.intf.IServiceAuthorizer#readyAuthorization(java.lang.String, java.util.Map)
	 */
	@Override
	public String readyAuthorization(String userEmail, Map<String, Object> data)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.gmail.yuyang226.autoflickr2twitter.intf.IServiceAuthorizer#requestAuthorization()
	 */
	@Override
	public Map<String, Object> requestAuthorization() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
