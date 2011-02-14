package com.googlecode.flickr2twitter.impl.facebook;

import java.util.List;
import java.util.Map;

import com.googlecode.flickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig;
import com.googlecode.flickr2twitter.intf.ITargetServiceProvider;
import com.googlecode.flickr2twitter.model.IItem;
import com.googlecode.flickr2twitter.model.IItemList;

public class TargetServiceProviderFacebook implements ITargetServiceProvider{
	
	public static final String ID = "eBaySocialHub";
	public static final String DISPLAY_NAME = "eBaySocialHub";
	public static final String CALLBACK_URL = "facebookcallback.jsp";
	


	@Override
	public Map<String, Object> requestAuthorization(String baseUrl)
			throws Exception {
		return null;
	}

	@Override
	public String readyAuthorization(String userEmail, Map<String, Object> data)
			throws Exception {
		return null;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public GlobalTargetApplicationService createDefaultGlobalApplicationConfig() {
		return null;
	}

	@Override
	public void postUpdate(GlobalTargetApplicationService globalAppConfig,
			UserTargetServiceConfig targetConfig, List<IItemList<IItem>> items)
			throws Exception {
		
	}

}
