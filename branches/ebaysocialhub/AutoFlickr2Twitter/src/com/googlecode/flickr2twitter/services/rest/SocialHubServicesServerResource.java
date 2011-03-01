/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest;

import java.util.logging.Logger;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig;
import com.googlecode.flickr2twitter.impl.ebay.FindItemsDAO;
import com.googlecode.flickr2twitter.impl.ebay.SourceServiceProviderEbayKeywords;
import com.googlecode.flickr2twitter.impl.twitter.AbstractServiceProviderTwitter;
import com.googlecode.flickr2twitter.org.apache.commons.lang3.StringUtils;
import com.googlecode.flickr2twitter.services.rest.models.ISociaHubServicesResource;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class SocialHubServicesServerResource extends ServerResource implements ISociaHubServicesResource {
	private static final Logger log = Logger.getLogger(SocialHubServicesServerResource.class.getName());
	
	/**
	 * 
	 */
	public SocialHubServicesServerResource() {
		super();
	}


	/*@Override
	protected Representation put(Representation entity)
			throws ResourceException {
		log.info("put representation->" + entity.getClass());
		try {
			log.info(", data->" + entity.getText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.put(entity);
	}*/
	
	@Post
	public void addUserTargetServiceConfig(String data) {
		log.info("user target service config data->" + data);
		if (data != null) {
			String[] values = StringUtils.split(data, "/");
			if (values.length >= 6 && values[0].equalsIgnoreCase(
					AbstractServiceProviderTwitter.ID)) {
				String providerId = values[0];
				String userEmail = values[1];
				String token = values[2];
				String tokenSecret = values[3];
				String userId = values[4];
				String screenName = values[5];
				
				UserTargetServiceConfig targetConfig = new UserTargetServiceConfig();
				targetConfig.setServiceProviderId(providerId);
				targetConfig.setUserEmail(userEmail);
				targetConfig.setServiceUserId(userId);
				targetConfig.setUserSiteUrl("http://twitter.com/" + screenName);
				targetConfig.setServiceUserName(screenName);
				targetConfig.setServiceAccessToken(token);
				targetConfig.setServiceTokenSecret(tokenSecret);
				log.info("Adding new user target config to database->" + targetConfig);
				MyPersistenceManagerFactory.addTargetServiceApp(userEmail, targetConfig);
			
				
			}
		}
	}
	
	@Post
	public void addUserSourceServiceConfig(String data) {
		log.info("user source service config data->" + data);
		if (data != null) {
			String[] values = StringUtils.split(data, "/");
			if (values.length >= 3 && values[0].equalsIgnoreCase(
					SourceServiceProviderEbayKeywords.ID)) {
				String providerId = values[0];
				String userEmail = values[1];
				String keywords = values[2];
				
				
				UserSourceServiceConfig sourceConfig = new UserSourceServiceConfig();
				sourceConfig.setServiceProviderId(providerId);
				sourceConfig.setUserEmail(userEmail);
				sourceConfig.setServiceUserId(keywords);
				String userSiteUrl = "http://shop.ebay.com/i.html?_trkparms=65%253A12%257C66%253A2%257C39%253A1%257C72%253A4831&rt=nc&_nkw=" +
						new FindItemsDAO().encodeKeywords(keywords) + 
						"&_ipg=&_sc=1&_sticky=1&_trksid=p3286.c0.m14&_sop=10&_sc=1";
						
				sourceConfig.setUserSiteUrl(userSiteUrl);
				sourceConfig.setServiceUserName(keywords);
				log.info("Adding new user target config to database->" + sourceConfig);
				MyPersistenceManagerFactory.addSourceServiceApp(userEmail, sourceConfig);
			
				
			}
		}
	}


	/*@Put
	public void addUserTargetServiceConfig(UserTargetServiceConfigModel targetModel) {
		log.info("Saving user target service config->" + targetModel);
		if (targetModel != null) {
			UserTargetServiceConfig targetService = new UserTargetServiceConfig();
			targetService.setServiceUserId(targetModel.getServiceUserId());
			targetService.setServiceUserName(targetModel.getServiceUserName());
			targetService.setServiceProviderId(targetModel.getServiceProviderId());
			targetService.setUserEmail(targetModel.getUserEmail());
			try {
				targetService.setAdditionalParameters(targetModel.getAdditionalParameters());
			} catch (UnsupportedEncodingException e) {
				log.throwing(this.getClass().getName(), "addUserTargetServiceConfig", e);
			}
			targetService.setUserSiteUrl(targetModel.getUserSiteUrl());
			targetService.setServiceAccessToken(targetModel.getServiceAccessToken());
			targetService.setServiceTokenSecret(targetModel.getServiceTokenSecret());
			MyPersistenceManagerFactory.addTargetServiceApp(targetModel.getUserEmail(), targetService);
		}
	}*/

}

