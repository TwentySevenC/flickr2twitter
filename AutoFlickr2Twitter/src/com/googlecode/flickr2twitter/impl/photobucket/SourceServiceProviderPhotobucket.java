/**
 * 
 */
package com.googlecode.flickr2twitter.impl.photobucket;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;

import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.signature.SignatureMethod;

import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.googlecode.flickr2twitter.datastore.model.GlobalSourceApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.intf.ISourceServiceProvider;
import com.googlecode.flickr2twitter.model.IPhoto;
import com.photobucket.api.core.PhotobucketAPI;
import com.photobucket.api.oauth.PhotobucketHttpOAuthConsumer;
import com.photobucket.api.rest.RESTfulResponse;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class SourceServiceProviderPhotobucket implements
		ISourceServiceProvider<IPhoto> {
	public static final String ID = "photobucket";
	public static final String DISPLAY_NAME = "Photobucket";
	public static final String DEFAULT_APP_KEY = "149830613";
	public static final String DEFAULT_CONSUMER_SECRET = "dfc70435139fa0e6f06443910c8de2e3";
	
	
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
		// TODO Auto-generated method stub
		return null;
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
		PhotobucketHttpOAuthConsumer oauthConsumer = 
			new PhotobucketHttpOAuthConsumer(globalAppConfig.getSourceAppApiKey(), 
					globalAppConfig.getSourceAppSecret(), SignatureMethod.HMAC_SHA1);
		Date now = Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTime();
		
		api.setOauthConsumerKey(globalAppConfig.getSourceAppApiKey());
		/*//api.setOauthConsumerSecret(global)
		AuthInterface authInterface = f.getAuthInterface();

		String frob = authInterface.getFrob();
		if (baseUrl.endsWith("/oauth")) {
			baseUrl = StringUtils.left(baseUrl, baseUrl.length() - "/oauth".length());
		}
		String nextUrl = baseUrl + "/" + CALLBACK_URL;
		URL url = authInterface.buildAuthenticationUrl(Permission.READ, frob, nextUrl);
		log.info("frob: " + frob + ", Token URL: " + url.toExternalForm());
		
		result.put("url", url.toExternalForm());*/
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
         api.setRequestPath("/ping");
         
         api.setMethod("get");
         
         try {
                 RESTfulResponse response = api.execute();
                 System.out.println(response.getResponseString());
                 
                 PhotobucketHttpOAuthConsumer oauthConsumer = 
         			new PhotobucketHttpOAuthConsumer(DEFAULT_APP_KEY, 
         					DEFAULT_CONSUMER_SECRET, SignatureMethod.HMAC_SHA1);
                 
                 HttpRequest req = oauthConsumer.sign(null);
                 System.out.println(req);
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
