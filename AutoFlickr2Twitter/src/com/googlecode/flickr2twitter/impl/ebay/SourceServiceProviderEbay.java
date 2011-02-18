/**
 * 
 */
package com.googlecode.flickr2twitter.impl.ebay;

import java.util.List;
import java.util.logging.Logger;

import com.googlecode.flickr2twitter.core.GlobalDefaultConfiguration;
import com.googlecode.flickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.googlecode.flickr2twitter.datastore.model.GlobalSourceApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.intf.IConfigurableService;
import com.googlecode.flickr2twitter.intf.ISourceServiceProvider;
import com.googlecode.flickr2twitter.model.IItem;

/**
 * @author yayu
 *
 */
public class SourceServiceProviderEbay implements ISourceServiceProvider<IItem>,
		IConfigurableService {
	public static final String ID = "ebay";
	public static final String DISPLAY_NAME = "eBay";
	public static final String PAGE_NAME_CONFIG = "ebay_config.jsp";;

	private static final Logger log = Logger.getLogger(SourceServiceProviderEbay.class.getName());
	/**
	 * 
	 */
	public SourceServiceProviderEbay() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IConfigurableService#getConfigPagePath()
	 */
	@Override
	public String getConfigPagePath() {
		return PAGE_NAME_CONFIG;
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public GlobalSourceApplicationService createDefaultGlobalApplicationConfig() {
		GlobalSourceApplicationService result = new GlobalSourceApplicationService();
		result.setAppName(DISPLAY_NAME);
		result.setProviderId(ID);
		result.setDescription("The world's leading e-commerce site");
		result.setSourceAppApiKey("hello world");
		result.setSourceAppSecret("hello world again");
		result.setAuthPagePath(null);
		result.setConfigPagePath(PAGE_NAME_CONFIG);
		result.setImagePath("/services/ebay/images/ebay_100.gif");
		return result;
	}

	@Override
	public List<IItem> getLatestItems(GlobalServiceConfiguration globalConfig,
			GlobalSourceApplicationService globalSvcConfig,
			UserSourceServiceConfig sourceService, long currentTime)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
