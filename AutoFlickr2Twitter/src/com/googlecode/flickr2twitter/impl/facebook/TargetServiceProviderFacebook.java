package com.googlecode.flickr2twitter.impl.facebook;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.googlecode.flickr2twitter.com.aetrion.flickr.Flickr;
import com.googlecode.flickr2twitter.com.aetrion.flickr.REST;
import com.googlecode.flickr2twitter.com.aetrion.flickr.auth.Auth;
import com.googlecode.flickr2twitter.com.aetrion.flickr.auth.AuthInterface;
import com.googlecode.flickr2twitter.core.GlobalDefaultConfiguration;
import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.googlecode.flickr2twitter.datastore.model.User;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig;
import com.googlecode.flickr2twitter.exceptions.TokenAlreadyRegisteredException;
import com.googlecode.flickr2twitter.facebook.FacebookUtil;
import com.googlecode.flickr2twitter.intf.ITargetServiceProvider;
import com.googlecode.flickr2twitter.model.IItem;
import com.googlecode.flickr2twitter.model.IItemList;
import com.googlecode.flickr2twitter.org.apache.commons.lang3.StringUtils;

public class TargetServiceProviderFacebook implements ITargetServiceProvider {

	private static final Logger log = Logger
			.getLogger(TargetServiceProviderFacebook.class.getName());

	public static final String ID = "Facebook";
	public static final String DISPLAY_NAME = "Facebook";
	public static final String CALLBACK_URL = "facebookcallback.jsp";
	public static final String PARA_CODE = "code";

	@Override
	public Map<String, Object> requestAuthorization(String baseUrl)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		if (baseUrl.endsWith("/oauth")) {
			baseUrl = StringUtils.left(baseUrl, baseUrl.length() - "oauth".length());
		}
		
		if (baseUrl.endsWith("/") == false) {
			baseUrl += "/";
		}
		String callbackURL = URLEncoder.encode(baseUrl + CALLBACK_URL, "UTF-8");
		String facebookautURL = MessageFormat.format(FacebookUtil.AUTH_URL, callbackURL);
		log.info("Facebook auth URL: " + facebookautURL);
		result.put("url", facebookautURL);
		return result;
	}

	@Override
	public String readyAuthorization(String userEmail, Map<String, Object> data)
			throws Exception {
		if (data == null || data.containsKey(PARA_CODE) == false) {
			throw new IllegalArgumentException("Invalid data: " + data);
		}
		User user = MyPersistenceManagerFactory.getUser(userEmail);
		if (user == null) {
			throw new IllegalArgumentException(
					"Can not find the specified user: " + userEmail);
		}
		
		String code = (String)data.get(PARA_CODE);
		
		StringBuffer buf = new StringBuffer();
		buf.append("Facebook Authentication success\n");
		// This token can be used until the user revokes it.
		buf.append("Code: " + code);

//		String userId = auth.getUser().getId();
//		for (UserSourceServiceConfig service : MyPersistenceManagerFactory
//				.getUserSourceServices(user)) {
//			if (auth.getToken().equals(service.getServiceAccessToken())) {
//				throw new TokenAlreadyRegisteredException(auth.getToken(), auth
//						.getUser().getUsername());
//			}
//		}
//		UserSourceServiceConfig serviceConfig = new UserSourceServiceConfig();
//		serviceConfig.setServiceUserId(userId);
//		serviceConfig.setServiceUserName(auth.getUser().getUsername());
//		serviceConfig.setServiceAccessToken(auth.getToken());
//		serviceConfig.setServiceProviderId(ID);
//		serviceConfig.setUserEmail(userEmail);
//		com.googlecode.flickr2twitter.com.aetrion.flickr.people.User flickrUser = 
//			f.getPeopleInterface().getInfo(userId);
//		if (flickrUser != null) {
//			serviceConfig.setUserSiteUrl(flickrUser.getPhotosurl());
//		}
//		
//		serviceConfig.addAddtionalParameter(KEY_FILTER_TAGS, "");
//		
//		MyPersistenceManagerFactory.addSourceServiceApp(userEmail, serviceConfig);
//
//		return buf.toString();
//	
		return null;
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public GlobalTargetApplicationService createDefaultGlobalApplicationConfig() {
		GlobalTargetApplicationService result = new GlobalTargetApplicationService();
		result.setAppName(DISPLAY_NAME);
		result.setProviderId(ID);
		result.setDescription("Facebook Status service");
		result.setTargetAppConsumerId(FacebookUtil.APP_ID);
		result.setTargetAppConsumerSecret(FacebookUtil.APP_SECRET);
		result.setAuthPagePath(CALLBACK_URL);
		result.setImagePath(null); // TODO set the default image path
		return result;
	}

	@Override
	public void postUpdate(GlobalTargetApplicationService globalAppConfig,
			UserTargetServiceConfig targetConfig, List<IItemList<IItem>> items)
			throws Exception {

	}

}
