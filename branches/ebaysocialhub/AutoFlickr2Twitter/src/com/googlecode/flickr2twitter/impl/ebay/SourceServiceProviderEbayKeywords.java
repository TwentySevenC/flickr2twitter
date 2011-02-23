/**
 * 
 */
package com.googlecode.flickr2twitter.impl.ebay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import twitter4j.internal.logging.Logger;

import com.googlecode.flickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.googlecode.flickr2twitter.datastore.model.GlobalSourceApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.impl.flickr.SourceServiceProviderFlickr;
import com.googlecode.flickr2twitter.model.IItem;

/**
 * @author yayu
 *
 */
public class SourceServiceProviderEbayKeywords extends
		SourceServiceProviderEbay {
	public static final String ID = "ebay_keywords";
	public static final String DISPLAY_NAME = "eBay Keywords";
	public static final String PAGE_NAME_CONFIG = "ebay_config_keywords.jsp";
	private static final Logger log = Logger.getLogger(SourceServiceProviderEbayKeywords.class);
	
	private FindItemsDAO dao = new FindItemsDAO();

	/**
	 * 
	 */
	public SourceServiceProviderEbayKeywords() {
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
	public List<IItem> getLatestItems(GlobalServiceConfiguration globalConfig,
			GlobalSourceApplicationService globalSvcConfig,
			UserSourceServiceConfig sourceService, long currentTime)
			throws Exception {
		
		String keywords = sourceService.getServiceUserId();
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone(
				SourceServiceProviderFlickr.TIMEZONE_GMT));
		now.setTimeInMillis(currentTime);
		log.info("Converted current time: " + now.getTime());
			
		Calendar past = Calendar.getInstance(TimeZone.getTimeZone(SourceServiceProviderFlickr.TIMEZONE_GMT));
		long newTime = now.getTime().getTime() - globalConfig.getMinUploadTime();
		past.setTimeInMillis(newTime);
		
		log.info("Fetching latest listing for eBay keywords->" + keywords 
				+ " from " + past.getTime() + " to " + now.getTime());
		List<EbayItem> ebayItems = dao.findItemsByKeywordsFromSandbox(keywords, 1);
		
		log.info("found " + ebayItems.size() + " items being listed recently");
		List<IItem> items = new ArrayList<IItem>(1);
		if (ebayItems.isEmpty() == false) {
			EbayItem ebayItem = ebayItems.get(0);
			log.info("The most recent listed ebay item->" + ebayItem);
			if (ebayItem.getStartTime().after(past.getTime())) {
				items.add(new EbayKeywordsItem(sourceService.getUserSiteUrl(), keywords, 
					"Found new listing for keywords: " + keywords, null));
			}
		}
		return items;
	}

	List<IItem> convert(List<EbayItem> ebayItems) {
		List<IItem> items = new ArrayList<IItem>();
		for(EbayItem each :ebayItems) {
			IItem itm = new EbayItemAdapter(each);
			items.add(itm);
		}
		
		return items;
	}
	
	@Override
	public GlobalSourceApplicationService createDefaultGlobalApplicationConfig() {
		GlobalSourceApplicationService result = new GlobalSourceApplicationService();
		result.setAppName(DISPLAY_NAME);
		result.setProviderId(ID);
		result.setDescription("The world's leading e-commerce site");
		result.setSourceAppApiKey("no_app_api_key");
		result.setSourceAppSecret("no_app_api_secret");
		result.setAuthPagePath(null);
		result.setConfigPagePath(PAGE_NAME_CONFIG);
		result.setImagePath("/services/ebay/images/ebay_200.gif");
		return result;
	}

}
