package com.googlecode.flickr2twitter.impl.facebook;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.googlecode.flickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig;
import com.googlecode.flickr2twitter.facebook.FacebookUtil;
import com.googlecode.flickr2twitter.intf.ITargetServiceProvider;
import com.googlecode.flickr2twitter.model.IItem;
import com.googlecode.flickr2twitter.model.IItemList;
import com.googlecode.flickr2twitter.org.apache.commons.lang3.StringUtils;

public class TargetServiceProviderFacebook implements ITargetServiceProvider {

	private static final Logger log = Logger
			.getLogger(TargetServiceProviderFacebook.class.getName());

	public static final String ID = "eBaySocialHub";
	public static final String DISPLAY_NAME = "eBaySocialHub";
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
		log.info("Facebook auth callback URL: " + FacebookUtil.AUTH_URL + callbackURL);
		result.put("url", FacebookUtil.AUTH_URL + callbackURL);
		return result;
	}

	@Override
	public String readyAuthorization(String userEmail, Map<String, Object> data)
			throws Exception {
		System.out.println(data.get(PARA_CODE));
		System.out.println(userEmail);

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
