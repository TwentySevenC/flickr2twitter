/**
 * 
 */
package com.googlecode.flickr2twitter.impl.email;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.appengine.api.mail.MailServiceFactory;
import com.google.appengine.api.mail.MailService.Message;
import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.GlobalApplicationConfig;
import com.googlecode.flickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig;
import com.googlecode.flickr2twitter.intf.ITargetServiceProvider;
import com.googlecode.flickr2twitter.model.IItem;
import com.googlecode.flickr2twitter.model.IItemList;
import com.googlecode.flickr2twitter.model.IPhoto;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class TargetServiceProviderEmail implements ITargetServiceProvider {
	public static final String ID = "email";
	public static final String DISPLAY_NAME = "Email";
	public static final String TIMEZONE_CST = "CST";
	private static final Logger log = Logger.getLogger(TargetServiceProviderEmail.class.getName());
	
	/**
	 * 
	 */
	public TargetServiceProviderEmail() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.ITargetServiceProvider#postUpdate(com.googlecode.flickr2twitter.datastore.model.GlobalTargetApplicationService, com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig, java.util.List)
	 */
	@Override
	public void postUpdate(GlobalTargetApplicationService globalAppConfig,
			UserTargetServiceConfig targetConfig, List<IItemList<IItem>> items)
			throws Exception {

		Message msg = new Message();
		msg.setReplyTo(globalAppConfig.getTargetAppConsumerId());
		msg.setSender(globalAppConfig.getTargetAppConsumerSecret());
		msg.setSubject("[flickr2twi] flickr2twitter just found some new updates!");
		msg.setTo(targetConfig.getServiceUserId());
		msg.setCc(targetConfig.getServiceUserId());
		StringBuffer buf = new StringBuffer();
		for (IItemList<IItem> itemList : items) {
			log.info("Processing items from: " + itemList.getListTitle());
			buf.append("<p>");
			buf.append("<b>");
			buf.append(itemList.getListTitle());
			buf.append("</b><br><br>");
			
			for (IItem item : itemList.getItems()) {
				log.info("Posting message -> " + item + " for "
							+ targetConfig.getServiceUserName());
				buf.append(item.getTitle());
				buf.append(": ");
				buf.append(item.getDescription());
				if (item instanceof IPhoto) {
					IPhoto photo = (IPhoto) item;
					buf.append(". <a href=\"");
					buf.append(photo.getUrl());
					buf.append("\">");
					buf.append(photo.getTitle());
					buf.append("</a>");
				}
				buf.append("<br>");
			}
			buf.append("</p>");
		}
		msg.setHtmlBody(buf.toString());
		MailServiceFactory.getMailService().send(msg);
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IServiceAuthorizer#readyAuthorization(java.lang.String, java.util.Map)
	 */
	@Override
	public String readyAuthorization(String userEmail, Map<String, Object> data)
			throws Exception {
		UserTargetServiceConfig service = new UserTargetServiceConfig();
		String email = String.valueOf(data.get("email"));
		service.setServiceProviderId(ID);
		service.setServiceAccessToken("");
		service.setServiceTokenSecret("");
		service.setServiceUserId(email);
		service.setUserEmail(userEmail);
		service.setServiceUserName(email);
		service.setUserSiteUrl(null);
		MyPersistenceManagerFactory.addTargetServiceApp(userEmail, service);
		return "";
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IServiceAuthorizer#requestAuthorization(java.lang.String)
	 */
	@Override
	public Map<String, Object> requestAuthorization(String baseUrl)
			throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("email", "yuyang226@gmail.com");
		return data;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.intf.IServiceProvider#createDefaultGlobalApplicationConfig()
	 */
	@Override
	public GlobalApplicationConfig createDefaultGlobalApplicationConfig() {
		GlobalTargetApplicationService result = new GlobalTargetApplicationService();
		result.setAppName(DISPLAY_NAME);
		result.setProviderId(ID);
		result.setDescription("The Email target service");
		result.setTargetAppConsumerId("flickr2twitter@googlegroups.com");
		result.setTargetAppConsumerSecret("flickr2twitter@googlegroups.com");
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
