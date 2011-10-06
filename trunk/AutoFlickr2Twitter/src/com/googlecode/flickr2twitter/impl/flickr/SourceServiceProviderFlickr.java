/**
 * 
 */
package com.googlecode.flickr2twitter.impl.flickr;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.REST;
import com.gmail.yuyang226.flickr.RequestContext;
import com.gmail.yuyang226.flickr.auth.Permission;
import com.gmail.yuyang226.flickr.oauth.OAuth;
import com.gmail.yuyang226.flickr.oauth.OAuthInterface;
import com.gmail.yuyang226.flickr.oauth.OAuthToken;
import com.gmail.yuyang226.flickr.photos.Extras;
import com.gmail.yuyang226.flickr.photos.Photo;
import com.gmail.yuyang226.flickr.photos.PhotoList;
import com.gmail.yuyang226.flickr.photos.PhotosInterface;
import com.gmail.yuyang226.flickr.tags.Tag;
import com.googlecode.flickr2twitter.core.GlobalDefaultConfiguration;
import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.googlecode.flickr2twitter.datastore.model.GlobalSourceApplicationService;
import com.googlecode.flickr2twitter.datastore.model.User;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.exceptions.TokenAlreadyRegisteredException;
import com.googlecode.flickr2twitter.impl.flickr.model.FlickrPhoto;
import com.googlecode.flickr2twitter.intf.IServiceAuthorizer;
import com.googlecode.flickr2twitter.intf.ISourceServiceProvider;
import com.googlecode.flickr2twitter.model.IPhoto;
import com.googlecode.flickr2twitter.org.apache.commons.lang3.StringUtils;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 * 
 */
public class SourceServiceProviderFlickr implements
		ISourceServiceProvider<IPhoto>, IServiceAuthorizer {
	public static final String ID = "flickr";
	public static final String DISPLAY_NAME = "Flickr";
	public static final String KEY_REQUEST_TOKEN = "request_token";
	public static final String KEY_TOKEN_SECRET = "token_secret";
	public static final String KEY_TOKEN_VERIFIER = "oauth_verifier";
	public static final String KEY_OAUTH_TOKEN = "oauth_token";
	public static final String KEY_FILTER_TAGS = "filter_tags";
	public static final String TAGS_DELIMITER = ",";
	public static final String TIMEZONE_CST = "CST";
	public static final String TIMEZONE_GMT = "GMT";
	
	public static final String HOST_OAUTH = "www.flickr.com";
	public static final String PATH_OAUTH_REQUEST_TOKEN = "/services/oauth/request_token";
	public static final String PATH_OAUTH_ACCESS_TOKEN = "/services/oauth/access_token";
	public static final String PATH_OAUTH_AUTHORIZE = "/services/oauth/authorize";
	public static final String URL_REQUEST_TOKEN = "http://" + HOST_OAUTH + PATH_OAUTH_REQUEST_TOKEN;
	public static final String URL_ACCESS_TOKEN = "http://" + HOST_OAUTH + PATH_OAUTH_ACCESS_TOKEN;
	public static final String URL_AUTHORIZE = "http://" + HOST_OAUTH + PATH_OAUTH_AUTHORIZE;
	
	public static final String CALLBACK_URL = "flickrcallback.jsp";
	private static final Logger log = LoggerFactory.getLogger(SourceServiceProviderFlickr.class);

	/**
	 * 
	 */
	public SourceServiceProviderFlickr() {
		super();
	}

	private List<IPhoto> showRecentPhotos(Flickr f, UserSourceServiceConfig sourceService, 
			long interval, long currentTime) throws IOException, JSONException, FlickrException, InvalidKeyException, NoSuchAlgorithmException {
		String userId = sourceService.getServiceUserId();
		RequestContext requestContext = RequestContext.getRequestContext();
		OAuth auth = new OAuth();
		//auth.setPermission(Permission.READ);
		OAuthToken oauthToken = new OAuthToken(sourceService.getServiceAccessToken(), 
				sourceService.getServiceTokenSecret());
		auth.setToken(oauthToken);
		requestContext.setOAuth(auth);
		List<IPhoto> photos = new ArrayList<IPhoto>();
		List<String> filterTags = new ArrayList<String>();
		Map<String, String> additionalParams = sourceService.getAdditionalParameters();
		if (additionalParams.containsKey(KEY_FILTER_TAGS)) {
			String filterTrags = additionalParams.get(KEY_FILTER_TAGS);
			if (filterTrags != null) {
				filterTags.addAll(Arrays.asList(StringUtils.split(
						filterTrags.trim(), TAGS_DELIMITER)));
			}
		}
		
		PhotosInterface photosFace = f.getPhotosInterface();
		Set<String> extras = new HashSet<String>(2);
		extras.add(Extras.DATE_UPLOAD);
		extras.add(Extras.LAST_UPDATE);
		extras.add(Extras.GEO);
		if (filterTags.isEmpty() == false) {
			extras.add(Extras.TAGS);
		}
		
		Date pastTime = sourceService.getLastUpdateTime();
		Calendar past = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE_GMT));
		if (pastTime == null) {
		Calendar cstTime = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE_GMT));
		cstTime.setTimeInMillis(currentTime);
		log.info("Converted current time: {}", cstTime.getTime());

			//		Calendar past = getFromTime(globalConfig, currentTime);

		long newTime = cstTime.getTime().getTime() - interval;
		past.setTimeInMillis(newTime);
			pastTime = past.getTime();
		} else {
			past.setTimeInMillis(pastTime.getTime());
			pastTime = past.getTime();
		}
		
		PhotoList list = photosFace.recentlyUpdated(pastTime, extras, 100,
				1);
		
		log.info("Trying to find photos uploaded for user {} after {} from {} new photos",
				new Object[]{userId, pastTime.toString(), list.getTotal()});
		for (Object obj : list) {
			if (obj instanceof Photo) {
				Photo photo = (Photo) obj;
				
				log.info("processing photo [{}], date uploaded: {}", 
						photo.getTitle(), photo.getDatePosted());
				if (photo.isPublicFlag() && photo.getDatePosted().after(pastTime)) {
					if (!filterTags.isEmpty() && containsTags(filterTags, photo.getTags()) == false) {
						log.warn("Photo does not contains the required tags, contained tags are: {}" 
								, photo.getTags());
					} else {
						log.info("Photo={}, URL={}, date uploaded={}, GEO={}", 
								new Object[]{photo.getTitle(), photo.getUrl(), photo.getDatePosted(), photo.getGeoData()});
						photos.add(new FlickrPhoto(photo));
					}
				} else {
					log.warn("private photo [{}]will not be posted.", photo.getTitle());
				}
			}
		}
		return photos;
	}
	
	private static boolean containsTags(List<String> filterTags, Collection<Tag> photoTags) {
		if (photoTags == null)
			return false;
		
		int matchCount = 0;
		
		for (Tag tag : photoTags) {
			if (filterTags.contains(tag.getValue())) {
				matchCount++;
			}
			if (matchCount == filterTags.size()) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.flickr2twitter.intf.ISourceServiceProvider#getId
	 * ()
	 */
	@Override
	public String getId() {
		return ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.flickr2twitter.intf.ISourceServiceProvider#
	 * getLatestItems()
	 */
	@Override
	public List<IPhoto> getLatestItems(GlobalServiceConfiguration globalConfig,
			GlobalSourceApplicationService globalSvcConfig, 
			UserSourceServiceConfig sourceService, 
			long currentTime) throws Exception {
		if (globalSvcConfig == null
				|| ID.equalsIgnoreCase(globalSvcConfig.getProviderId()) == false) {
			throw new IllegalArgumentException(
					"Invalid source service provider: " + globalSvcConfig);
		}
		REST transport = new REST();
		Flickr f = new Flickr(globalSvcConfig.getSourceAppApiKey(),
				globalSvcConfig.getSourceAppSecret(), transport);
		return showRecentPhotos(f, sourceService,
				globalConfig.getMinUploadTime(), currentTime);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.flickr2twitter.intf.ISourceServiceProvider#storeToken
	 * (com.googlecode.flickr2twitter.intf.IDataStoreService)
	 */
	@Override
	public String readyAuthorization(String userEmail, Map<String, Object> data)
			throws Exception {
		if (data == null || data.containsKey(KEY_TOKEN_VERIFIER) == false
				|| data.containsKey(KEY_OAUTH_TOKEN) == false
				|| data.containsKey(KEY_TOKEN_SECRET) == false) {
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
		String token = String.valueOf(data.get(KEY_OAUTH_TOKEN));
		String tokenSecret = String.valueOf(data.get(KEY_TOKEN_SECRET));
		String oauthVerifier = String.valueOf(data.get(KEY_TOKEN_VERIFIER));
		OAuthInterface authInterface = f.getOAuthInterface();
		OAuth oauth = authInterface.getAccessToken(token, tokenSecret, oauthVerifier);
		com.gmail.yuyang226.flickr.people.User flickrUser = oauth.getUser();
		RequestContext.getRequestContext().setOAuth(oauth);
		if (flickrUser == null) {
			flickrUser = authInterface.testLogin();
		}
		String photosUrl = flickrUser.getPhotosurl();
		if (photosUrl == null) {
			photosUrl = "http://www.flickr.com/photos/" + flickrUser.getId();
		}
		StringBuffer buf = new StringBuffer();
		buf.append("Authentication success\n");
		// This token can be used until the user revokes it.
		buf.append("Token: " + oauth.getToken());
		buf.append("\n");
		buf.append("nsid: " + flickrUser.getId());
		buf.append("\n");
		buf.append("User Site: " + photosUrl);
		buf.append("\n");
		buf.append("Username: " + flickrUser.getUsername());

		String userId = flickrUser.getId();
		for (UserSourceServiceConfig service : MyPersistenceManagerFactory
				.getUserSourceServices(user)) {
			if (oauth.getToken().getOauthToken().equals(service.getServiceAccessToken())) {
				throw new TokenAlreadyRegisteredException(oauth.getToken().getOauthToken(), oauth
						.getUser().getUsername());
			}
		}
		UserSourceServiceConfig serviceConfig = new UserSourceServiceConfig();
		serviceConfig.setServiceUserId(userId);
		serviceConfig.setServiceUserName(flickrUser.getUsername());
		serviceConfig.setServiceAccessToken(oauth.getToken().getOauthToken());
		serviceConfig.setServiceTokenSecret(oauth.getToken().getOauthTokenSecret());
		serviceConfig.setServiceProviderId(ID);
		serviceConfig.setUserEmail(userEmail);
		serviceConfig.setUserSiteUrl(photosUrl);
		
		serviceConfig.addAddtionalParameter(KEY_FILTER_TAGS, "");
		
		MyPersistenceManagerFactory.addSourceServiceApp(userEmail, serviceConfig);

		return buf.toString();
	}
	
	public static void setFilterTags(UserSourceServiceConfig serviceConfig, String filterTags) {
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.flickr2twitter.intf.ISourceServiceProvider#
	 * requestAuthorization()
	 */
	@Override
	public Map<String, Object> requestAuthorization(String baseUrl) throws Exception {
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
		OAuthInterface authInterface = f.getOAuthInterface();
		if (baseUrl.endsWith("/oauth")) {
			baseUrl = StringUtils.left(baseUrl, baseUrl.length() - "/oauth".length());
		}
		String callbackUrl = baseUrl + "/" + CALLBACK_URL;
		OAuthToken oauthToken = authInterface.getRequestToken(callbackUrl);
		URL url = authInterface.buildAuthenticationUrl(Permission.READ, oauthToken);
		log.info("OAuthToken: {}, Token URL: {}", oauthToken, url.toExternalForm());
		result.put(KEY_OAUTH_TOKEN, oauthToken);
		
		result.put("url", url);
		result.put(KEY_OAUTH_TOKEN, oauthToken.getOauthToken());
		result.put(KEY_TOKEN_SECRET, oauthToken.getOauthTokenSecret());
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.flickr2twitter.intf.IServiceProvider#
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
		result.setAuthPagePath(CALLBACK_URL);
		result.setImagePath("/services/flickr/images/flickr_100.gif");
		return result;
	}

}
