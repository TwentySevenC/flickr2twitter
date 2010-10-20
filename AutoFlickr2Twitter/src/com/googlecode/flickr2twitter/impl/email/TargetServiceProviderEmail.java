/**
 * 
 */
package com.googlecode.flickr2twitter.impl.email;

import java.util.List;
import java.util.Map;

import com.googlecode.flickr2twitter.datastore.model.GlobalApplicationConfig;
import com.googlecode.flickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig;
import com.googlecode.flickr2twitter.intf.ITargetServiceProvider;
import com.googlecode.flickr2twitter.model.IItem;
import com.googlecode.flickr2twitter.model.IItemList;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class TargetServiceProviderEmail implements ITargetServiceProvider {

	/**
	 * 
	 */
	public TargetServiceProviderEmail() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.ITargetServiceProvider#postUpdate(com.googlecode.flickr2twitter.datastore.model.GlobalTargetApplicationService, com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig, java.util.List)
	 */
	@Override
	public void postUpdate(GlobalTargetApplicationService globalAppConfig,
			UserTargetServiceConfig targetConfig, List<IItemList<IItem>> items)
			throws Exception {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IServiceProvider#createDefaultGlobalApplicationConfig()
	 */
	@Override
	public GlobalApplicationConfig createDefaultGlobalApplicationConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IServiceProvider#getId()
	 */
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
