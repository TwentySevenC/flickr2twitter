/**
 * 
 */
package com.googlecode.flickr2twitter.impl.photobucket;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.googlecode.flickr2twitter.datastore.model.GlobalSourceApplicationService;
import com.googlecode.flickr2twitter.datastore.model.User;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.intf.IAdminServiceProvider;
import com.googlecode.flickr2twitter.intf.ISourceServiceProvider;
import com.googlecode.flickr2twitter.model.IPhoto;
import com.googlecode.flickr2twitter.org.apache.commons.lang3.StringUtils;
import com.photobucket.api.core.PhotobucketAPI;
import com.photobucket.api.rest.RESTfulResponse;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class SourceServiceProviderPhotobucket implements
		ISourceServiceProvider<IPhoto>, IAdminServiceProvider {
	public static final String ID = "photobucket";
	public static final String DISPLAY_NAME = "Photobucket";
	public static final String DEFAULT_APP_KEY = "149830613";
	public static final String DEFAULT_CONSUMER_SECRET = "dfc70435139fa0e6f06443910c8de2e3";
	public static final String SUB_DOMAIN = "api.photobucket.com";
	public static final String KEY_STATUS = "status";
	
	public static final String CALLBACK_URL = "photobucketcallback.jsp";
	private static final Logger log = Logger.getLogger(SourceServiceProviderPhotobucket.class.getName());
	/**
	 * 
	 */
	public SourceServiceProviderPhotobucket() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.ISourceServiceProvider#getLatestItems(com.googlecode.flickr2twitter.datastore.model.GlobalServiceConfiguration, com.googlecode.flickr2twitter.datastore.model.GlobalSourceApplicationService, com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig, long)
	 */
	@Override
	public List<IPhoto> getLatestItems(GlobalServiceConfiguration globalConfig,
			GlobalSourceApplicationService globalSvcConfig,
			UserSourceServiceConfig sourceService, long currentTime)
			throws Exception {
		final List<IPhoto> photos = new ArrayList<IPhoto>();
		return photos;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IServiceAuthorizer#readyAuthorization(java.lang.String, java.util.Map)
	 */
	@Override
	public String readyAuthorization(String userEmail, Map<String, Object> data)
			throws Exception {
		if (data == null || data.containsKey(KEY_OAUTHTOKEN) == false) {
			throw new IllegalArgumentException("Invalid data: " + data);
		}
		User user = MyPersistenceManagerFactory.getUser(userEmail);
		if (user == null) {
			throw new IllegalArgumentException(
					"Can not find the specified user: " + userEmail);
		}
		StringBuffer buf = new StringBuffer();
		PhotobucketAPI api = new PhotobucketAPI();
        api.setOauthConsumerKey(DEFAULT_APP_KEY);
        api.setOauthConsumerSecret(DEFAULT_CONSUMER_SECRET);
        api.setSubdomain(SUB_DOMAIN);
        
		String oauthToken = String.valueOf(data.get(KEY_OAUTHTOKEN));
		String oauthTokenSecret = String.valueOf(data.get("oauth_token_secret"));
		api.setOauthToken(oauthToken);
		api.setOauthTokenSecret(oauthTokenSecret);
		
		/*AuthInterface authInterface = f.getAuthInterface();
		Auth auth = authInterface.getToken(frob);
		
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
		UserSourceServiceConfig serviceConfig = new UserSourceServiceConfig();
		serviceConfig.setServiceUserId(userId);
		serviceConfig.setServiceUserName(auth.getUser().getUsername());
		serviceConfig.setServiceAccessToken(auth.getToken());
		serviceConfig.setServiceProviderId(ID);
		serviceConfig.setUserEmail(userEmail);
		com.googlecode.flickr2twitter.com.aetrion.flickr.people.User flickrUser = 
			f.getPeopleInterface().getInfo(userId);
		if (flickrUser != null) {
			serviceConfig.setUserSiteUrl(flickrUser.getPhotosurl());
		}
		
		serviceConfig.addAddtionalParameter(KEY_FILTER_TAGS, "");
		
		MyPersistenceManagerFactory.addSourceServiceApp(userEmail, serviceConfig);*/

		return buf.toString();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IServiceAuthorizer#requestAuthorization(java.lang.String)
	 */
	@Override
	public Map<String, Object> requestAuthorization(String baseUrl)
	throws Exception {
		GlobalSourceApplicationService globalAppConfig = MyPersistenceManagerFactory
		.getGlobalSourceAppService(ID);
		if (globalAppConfig == null
				|| ID.equalsIgnoreCase(globalAppConfig.getProviderId()) == false) {
			throw new IllegalArgumentException(
					"Invalid source service provider: " + globalAppConfig);
		}
		Map<String, Object> result = new HashMap<String, Object>();

		PhotobucketAPI api = new PhotobucketAPI();
        api.setOauthConsumerKey(DEFAULT_APP_KEY);
        api.setOauthConsumerSecret(DEFAULT_CONSUMER_SECRET);
        api.setSubdomain(SUB_DOMAIN);
        api.setRequestPath("/login/request");
        api.setMethod("post");

        RESTfulResponse response = api.execute();
        if (response.getResponseCode() == 200) {
        	log.fine("Photobucet Request Token Response: " + response.getResponseString());
        	String encoding = System.getProperty("file.encoding");
            for (String data : StringUtils.split(response.getResponseString(), "&")) {
            	String key = StringUtils.substringBefore(data, "=");
            	String value = StringUtils.substringAfter(data, "=");
            	if (value.contains("%")) {
            		value = URLDecoder.decode(value, encoding);
            	}
            	result.put(key, value);
            }
            String authUrl = "http://photobucket.com/apilogin/login?oauth_token=" + result.get("oauth_token"); 
           result.put("url", authUrl);
       	
        }
        log.info("Current Data: " + result);
		return result;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IServiceProvider#createDefaultGlobalApplicationConfig()
	 */
	@Override
	public GlobalSourceApplicationService createDefaultGlobalApplicationConfig() {
		GlobalSourceApplicationService result = new GlobalSourceApplicationService();
		result.setAppName(DISPLAY_NAME);
		result.setProviderId(ID);
		result.setDescription("A popular online photo album service");
		result.setSourceAppApiKey(DEFAULT_APP_KEY);
		result.setSourceAppSecret(DEFAULT_CONSUMER_SECRET);
		result.setAuthPagePath(CALLBACK_URL);
		result.setImagePath(null); // TODO set the default image path
		return result;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IServiceProvider#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}
	
	public static void main(String[] args) {
		 PhotobucketAPI api = new PhotobucketAPI();
         api.setOauthConsumerKey(DEFAULT_APP_KEY);
         api.setOauthConsumerSecret(DEFAULT_CONSUMER_SECRET);
         api.setSubdomain("api.photobucket.com");
         api.setRequestPath("/login/request");
         
         api.setMethod("post");
         
         try {
                 RESTfulResponse response = api.execute();
                 System.out.println(response.getResponseCode());
                 Map<String, String> params = new HashMap<String, String>();
                 String encoding = System.getProperty("file.encoding");
                 for (String data : StringUtils.split(response.getResponseString(), "&")) {
                	 String key = StringUtils.substringBefore(data, "=");
                	 String value = StringUtils.substringAfter(data, "=");
                	 if (value.contains("%")) {
                		 value = URLDecoder.decode(value, encoding);
                	 }
                	 params.put(key, value);
                 }
                 System.out.println(params);
                 String authUrl = "http://photobucket.com/apilogin/login?oauth_token=" + params.get("oauth_token"); 
                 System.out.println(authUrl);
                 
                 /*Date now = Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTime();
                 PhotobucketHttpOAuthConsumer consumer = new PhotobucketHttpOAuthConsumer(DEFAULT_APP_KEY, 
      					DEFAULT_CONSUMER_SECRET, SignatureMethod.HMAC_SHA1);
                 consumer.setOauthTimestamp(now.getTime() / 1000);
                 consumer.setTokenWithSecret(params.get("oauth_token"), params.get("oauth_token_secret"));
                 
                 String scope = "http://api.photobucket.com";
                 OAuthProvider provider = new DefaultOAuthProvider(consumer, 
                		 "http://api.photobucket.com/login/request",
                		 "http://api.photobucket.com/login/access",
                 "http://photobucket.com/apilogin/login");

                 System.out.println("Fetching request token...");

                 String nextStep = params.get("next_step");
                 provider.retrieveAccessToken(nextStep);

                 System.out.println("Request token: " + consumer.getToken());
                 System.out.println("Token secret: " + consumer.getTokenSecret());

                 System.out.println("Now visit:\n" + authUrl + "\n... and grant this app authorization");
                 System.out.println("Enter the verification code and hit ENTER when you're done:");

                 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                 String verificationCode = br.readLine();

                 System.out.println("Fetching access token...");

                 provider.retrieveAccessToken(verificationCode.trim());

                 System.out.println("Access token: " + consumer.getToken());
                 System.out.println("Token secret: " + consumer.getTokenSecret());

                 URL url = new URL("http://www.blogger.com/feeds/default/blogs");
                 HttpURLConnection request = (HttpURLConnection) url.openConnection();

                 consumer.sign(request);

                 System.out.println("Sending request...");
                 request.connect();

                 System.out.println("Response: " + request.getResponseCode() + " "
                		 + request.getResponseMessage());*/
         } catch (IOException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
         } catch (URISyntaxException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
         } catch (OAuthMessageSignerException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
         } catch (OAuthExpectationFailedException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
         } catch (Exception e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
         }

	}

}
