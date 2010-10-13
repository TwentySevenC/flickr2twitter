/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.impl.flickr;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.xml.sax.SAXException;

import com.gmail.yuyang226.autoflickr2twitter.client.FlickrException;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.Flickr;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.REST;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.RequestContext;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.auth.Auth;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.auth.AuthInterface;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.auth.Permission;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.photos.Extras;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.photos.Photo;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.photos.PhotoList;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.photos.PhotosInterface;
import com.gmail.yuyang226.autoflickr2twitter.core.GlobalDefaultConfiguration;
import com.gmail.yuyang226.autoflickr2twitter.datastore.MyPersistenceManagerFactory;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.GlobalSourceApplicationService;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.User;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.UserSourceServiceConfig;
import com.gmail.yuyang226.autoflickr2twitter.exceptions.TokenAlreadyRegisteredException;
import com.gmail.yuyang226.autoflickr2twitter.intf.ISourceServiceProvider;
import com.gmail.yuyang226.autoflickr2twitter.model.IItem;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 * 
 */
public class SourceServiceProviderFlickr implements
		ISourceServiceProvider<IItem> {
	public static final String ID = "flickr";
	public static final String DISPLAY_NAME = "Flickr";
	private static final Logger log = Logger
			.getLogger(SourceServiceProviderFlickr.class.getName());

	/**
	 * 
	 */
	public SourceServiceProviderFlickr() {
		super();
	}

	private List<IItem> showRecentPhotos(Flickr f, String userId, String token,
			long interval) throws IOException, SAXException, FlickrException {
		RequestContext requestContext = RequestContext.getRequestContext();
		Auth auth = new Auth();
		auth.setPermission(Permission.READ);
		auth.setToken(token);
		requestContext.setAuth(auth);
		List<IItem> photos = new ArrayList<IItem>();
		// PeopleInterface pface = f.getPeopleInterface();
		PhotosInterface photosFace = f.getPhotosInterface();
		Set<String> extras = new HashSet<String>(2);
		extras.add(Extras.DATE_UPLOAD);
		extras.add(Extras.LAST_UPDATE);
		extras.add(Extras.GEO);

		// pface.getPublicPhotos(userId, extras, 10, 1);
		Date now = Calendar.getInstance(TimeZone.getTimeZone("CST"), Locale.UK)
				.getTime();
		log.info("Current time: " + now);
		Calendar past = Calendar.getInstance(TimeZone.getTimeZone("CST"),
				Locale.UK);
		long newTime = now.getTime() - interval;
		past.setTimeInMillis(newTime);
		PhotoList list = photosFace.recentlyUpdated(past.getTime(), extras, 20,
				1);

		log.info("Trying to find photos uploaded for user " + userId
				+ " after " + past.getTime().toString() + " from "
				+ list.getTotal() + " new photos");
		for (Object obj : list) {
			if (obj instanceof Photo) {
				Photo photo = (Photo) obj;
				log.fine("processing photo: " + photo.getTitle()
						+ ", date uploaded: " + photo.getDatePosted());
				if (photo.isPublicFlag()
						&& photo.getDatePosted().after(past.getTime())) {
					log.info(photo.getTitle() + ", URL: " + photo.getUrl()
							+ ", date uploaded: " + photo.getDatePosted()
							+ ", GEO: " + photo.getGeoData());
					photos.add(photo);
				}
			}
		}
		return photos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gmail.yuyang226.autoflickr2twitter.intf.ISourceServiceProvider#getId
	 * ()
	 */
	@Override
	public String getId() {
		return ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gmail.yuyang226.autoflickr2twitter.intf.ISourceServiceProvider#
	 * getLatestItems()
	 */
	@Override
	public List<IItem> getLatestItems(GlobalServiceConfiguration globalConfig,
			UserSourceServiceConfig sourceService) throws Exception {
		GlobalSourceApplicationService globalAppConfig = MyPersistenceManagerFactory
				.getGlobalSourceAppService(ID);
		if (globalAppConfig == null
				|| ID.equalsIgnoreCase(globalAppConfig.getProviderId()) == false) {
			throw new IllegalArgumentException(
					"Invalid source service provider: " + globalAppConfig);
		}
		REST transport = new REST();
		Flickr f = new Flickr(globalAppConfig.getSourceAppApiKey(),
				globalAppConfig.getSourceAppSecret(), transport);
		transport.setAllowCache(false);

		Flickr.debugRequest = false;
		Flickr.debugStream = false;
		return showRecentPhotos(f, sourceService.getServiceUserId(),
				sourceService.getServiceAccessToken(),
				globalConfig.getMinUploadTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gmail.yuyang226.autoflickr2twitter.intf.ISourceServiceProvider#storeToken
	 * (com.gmail.yuyang226.autoflickr2twitter.intf.IDataStoreService)
	 */
	@Override
	public String readyAuthorization(String userEmail, Map<String, Object> data)
			throws Exception {
		if (data == null || data.containsKey("frob") == false) {
			throw new IllegalArgumentException("Invalid data: " + data);
		}
		User user = MyPersistenceManagerFactory.getUser(userEmail);
		if (user == null) {
			throw new IllegalArgumentException(
					"Can not find the specified user: " + userEmail);
		}
		Flickr f = new Flickr(GlobalDefaultConfiguration.getInstance()
				.getFlickrApiKey(), GlobalDefaultConfiguration.getInstance()
				.getFlickrSecret(), new REST());
		String frob = String.valueOf(data.get("frob"));
		AuthInterface authInterface = f.getAuthInterface();
		Auth auth = authInterface.getToken(frob);
		StringBuffer buf = new StringBuffer();
		buf.append("Authentication success\n");
		// This token can be used until the user revokes it.
		buf.append("Token: " + auth.getToken());
		buf.append("\n");
		buf.append("nsid: " + auth.getUser().getId());
		buf.append("\n");
		buf.append("Realname: " + auth.getUser().getRealName());
		buf.append("\n");
		buf.append("User Site: " + auth.getUser().getPhotosurl());
		buf.append("\n");
		buf.append("Username: " + auth.getUser().getUsername());
		buf.append("\n");
		buf.append("Permission: " + auth.getPermission().getType());

		String userId = auth.getUser().getId();
		for (UserSourceServiceConfig service : MyPersistenceManagerFactory
				.getUserSourceServices(user)) {
			if (auth.getToken().equals(service.getServiceAccessToken())) {
				throw new TokenAlreadyRegisteredException(auth.getToken(), auth
						.getUser().getUsername());
			}
		}
		UserSourceServiceConfig service = new UserSourceServiceConfig();
		service.setServiceUserId(userId);
		service.setServiceUserName(auth.getUser().getUsername());
		service.setServiceAccessToken(auth.getToken());
		service.setServiceProviderId(ID);
		service.setUserEmail(userEmail);
		service.setUserSiteUrl(auth.getUser().getPhotosurl());
		MyPersistenceManagerFactory.addSourceServiceApp(userEmail, service);

		return buf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gmail.yuyang226.autoflickr2twitter.intf.ISourceServiceProvider#
	 * requestAuthorization()
	 */
	@Override
	public Map<String, Object> requestAuthorization() throws Exception {
		GlobalSourceApplicationService globalAppConfig = MyPersistenceManagerFactory
				.getGlobalSourceAppService(ID);
		if (globalAppConfig == null
				|| ID.equalsIgnoreCase(globalAppConfig.getProviderId()) == false) {
			throw new IllegalArgumentException(
					"Invalid source service provider: " + globalAppConfig);
		}
		Map<String, Object> result = new HashMap<String, Object>();

		Flickr f = new Flickr(globalAppConfig.getSourceAppApiKey(),
				globalAppConfig.getSourceAppSecret(), new REST());
		AuthInterface authInterface = f.getAuthInterface();

		String frob = authInterface.getFrob();

		URL url = authInterface.buildAuthenticationUrl(Permission.READ, frob);
		log.info("frob: " + frob + ", Token URL: " + url.toExternalForm());
		result.put("frob", frob);
		result.put("url", url.toExternalForm());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gmail.yuyang226.autoflickr2twitter.intf.IServiceProvider#
	 * createDefaultGlobalApplicationConfig()
	 */
	@Override
	public GlobalSourceApplicationService createDefaultGlobalApplicationConfig() {
		GlobalSourceApplicationService result = new GlobalSourceApplicationService();
		result.setAppName(DISPLAY_NAME);
		result.setProviderId(ID);
		result.setDescription("The world's leading online photo storage service");
		result.setSourceAppApiKey(GlobalDefaultConfiguration.getInstance()
				.getFlickrApiKey());
		result.setSourceAppSecret(GlobalDefaultConfiguration.getInstance()
				.getFlickrSecret());
		result.setAuthPagePath(null); // TODO set the default auth page path
		result.setImagePath(null); // TODO set the default image path
		return result;
	}

}
