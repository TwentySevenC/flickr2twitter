/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.impl.twitter;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

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

import com.gmail.yuyang226.autoflickr2twitter.core.GlobalConfiguration;
import com.gmail.yuyang226.autoflickr2twitter.core.TwitterPoster;
import com.gmail.yuyang226.autoflickr2twitter.datastore.MyPersistenceManagerFactory;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.User;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.UserTargetService;
import com.gmail.yuyang226.autoflickr2twitter.intf.IDataStoreService;
import com.gmail.yuyang226.autoflickr2twitter.intf.ITargetServiceProvider;
import com.gmail.yuyang226.autoflickr2twitter.model.IGeoItem;
import com.gmail.yuyang226.autoflickr2twitter.model.IItem;
import com.gmail.yuyang226.autoflickr2twitter.model.IPhoto;
import com.gmail.yuyang226.autoflickr2twitter.model.IShortUrl;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class TargetServiceProviderTwitter implements ITargetServiceProvider {
	public static final String ID = "twitter";
	private static final Logger log = Logger.getLogger(TwitterPoster.class.getName());
	private Twitter twitter = null;

	/**
	 * 
	 */
	public TargetServiceProviderTwitter() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.gmail.yuyang226.autoflickr2twitter.intf.ITargetServiceProvider#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}
	
	public String requestNewToken() throws TwitterException {
		try {
			twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(GlobalConfiguration.getInstance().getTwitterConsumerId(), 
					GlobalConfiguration.getInstance().getTwitterConsumerSecret());
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
			PersistenceManagerFactory pmf = MyPersistenceManagerFactory.getInstance();
			PersistenceManager pm = pmf.getPersistenceManager();

			try {
				List<User> users = MyPersistenceManagerFactory.getAllUsers();
				if (users.isEmpty() == false) {
					User user = users.get(0);
					/*user = pm.getObjectById(UserConfiguration.class, user.getFlickrUserId());
					user.setTwitterUserId(String.valueOf(twitter.verifyCredentials().getId()));
					user.setTwitterUserName(accessToken.getScreenName());
					user.setTwitterAccessToken(accessToken.getToken());
					user.setTwitterTokenSecret(accessToken.getTokenSecret());*/
				}
			} finally {
				pm.close();
			}
		}
		return buf.toString();
	}

	/* (non-Javadoc)
	 * @see com.gmail.yuyang226.autoflickr2twitter.intf.ITargetServiceProvider#storeToken(com.gmail.yuyang226.autoflickr2twitter.intf.IDataStoreService)
	 */
	@Override
	public boolean storeToken(IDataStoreService datastore) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.gmail.yuyang226.autoflickr2twitter.intf.ITargetServiceProvider#postUpdate(com.gmail.yuyang226.autoflickr2twitter.model.IItem)
	 */
	@Override
	public void postUpdate(GlobalServiceConfiguration globalConfig, 
			UserTargetService targetConfig, IItem item) throws Exception {
		log.info("Posting message -> " + item + " for " + targetConfig.getServiceUserName());
		// The factory instance is re-useable and thread safe.
		AccessToken accessToken = new AccessToken(targetConfig.getServiceAccessToken(), 
				targetConfig.getServiceTokenSecret()); 
		PropertyConfiguration conf = new PropertyConfiguration(new Properties());
		
		Authorization auth = new OAuthAuthorization(conf, globalConfig.getTargetAppConsumerId(), 
				globalConfig.getTargetAppConsumerSecret(), accessToken);
	    Twitter twitter = new TwitterFactory().getInstance(auth);
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
	    	Status status = geoLoc == null ? twitter.updateStatus(message) : twitter.updateStatus(message, geoLoc);
	    	log.info("Successfully updated the status [" + status.getText() + "] to user @" + targetConfig.getServiceUserName());
	    }

	}

}
