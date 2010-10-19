/**
 * 
 */
package com.googlecode.flickr2twitter.impl.picasa;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.api.client.googleapis.auth.authsub.AuthSubSingleUseTokenRequestUrl;
import com.googlecode.flickr2twitter.com.aetrion.flickr.Flickr;
import com.googlecode.flickr2twitter.com.aetrion.flickr.REST;
import com.googlecode.flickr2twitter.com.aetrion.flickr.auth.AuthInterface;
import com.googlecode.flickr2twitter.com.aetrion.flickr.auth.Permission;
import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.GlobalApplicationConfig;
import com.googlecode.flickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.googlecode.flickr2twitter.datastore.model.GlobalSourceApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.intf.ISourceServiceProvider;
import com.googlecode.flickr2twitter.model.IPhoto;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class SourceServiceProviderPicasa implements ISourceServiceProvider<IPhoto> {
	public static final String ID = "picasa";
	public static final String DISPLAY_NAME = "Picasa Web Album";
	public static final String KEY_FROB = "frob";
	public static final String TIMEZONE_CST = "CST";
	private static final Logger log = Logger.getLogger(SourceServiceProviderPicasa.class.getName());
	
	public static final String CONSUMER_KEY = "anonymous";
	public static final String CONSUMER_SECRET = "anonymous";
	
	private static final String SCOPE = "http://picasaweb.google.com/data";
	
	/**
	 * 
	 */
	public SourceServiceProviderPicasa() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.ISourceServiceProvider#getLatestItems(com.googlecode.flickr2twitter.datastore.model.GlobalServiceConfiguration, com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig)
	 */
	@Override
	public List<IPhoto> getLatestItems(GlobalServiceConfiguration globalConfig,
			UserSourceServiceConfig sourceService) throws Exception {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IServiceAuthorizer#readyAuthorization(java.lang.String, java.util.Map)
	 */
	@Override
	public String readyAuthorization(String userEmail, Map<String, Object> data)
			throws Exception {
		// TODO Auto-generated method stub
		return "";
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IServiceAuthorizer#requestAuthorization()
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

		/*Flickr f = new Flickr(globalAppConfig.getSourceAppApiKey(),
				globalAppConfig.getSourceAppSecret(), new REST());
		AuthInterface authInterface = f.getAuthInterface();

		String frob = authInterface.getFrob();

		URL url = authInterface.buildAuthenticationUrl(Permission.READ, frob);
		log.info("frob: " + frob + ", Token URL: " + url.toExternalForm());
		result.put(KEY_FROB, frob);
		result.put("url", url.toExternalForm());
		return result;*/
		String hostedDomain = "flickr2twitter.googlecode.com";
		String nextUrl = "http://localhost:8888/picasacallback.jsp";
		String scope = SCOPE;
		AuthSubSingleUseTokenRequestUrl authorizeUrl = new AuthSubSingleUseTokenRequestUrl();
		authorizeUrl.hostedDomain = hostedDomain;
		authorizeUrl.nextUrl = nextUrl;
		authorizeUrl.scope = scope;
		authorizeUrl.session = 1;
		//authorizeUrl.secure = AuthSub.
		//String authSubUrl = AuthSubUtil.getRequestUrl(hostedDomain, nextUrl, scope, secure, session);
		String authorizationUrl = authorizeUrl.build();
		
		System.out.println(authorizationUrl);
		result.put("url", authorizationUrl);
		return result;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IServiceProvider#createDefaultGlobalApplicationConfig()
	 */
	@Override
	public GlobalApplicationConfig createDefaultGlobalApplicationConfig() {
		GlobalSourceApplicationService result = new GlobalSourceApplicationService();
		result.setAppName(DISPLAY_NAME);
		result.setProviderId(ID);
		result.setDescription("The Google's online photo storage service");
		result.setSourceAppApiKey(CONSUMER_KEY);
		result.setSourceAppSecret(CONSUMER_SECRET);
		result.setAuthPagePath(null); // TODO set the default auth page path
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

}
