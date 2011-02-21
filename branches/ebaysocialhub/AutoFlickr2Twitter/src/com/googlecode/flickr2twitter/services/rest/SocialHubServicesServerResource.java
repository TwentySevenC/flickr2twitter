/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.googlecode.flickr2twitter.core.ServiceFactory;
import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig;
import com.googlecode.flickr2twitter.impl.twitter.AbstractServiceProviderTwitter;
import com.googlecode.flickr2twitter.intf.ITargetServiceProvider;
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
			if (values.length >= 6 && values[0].equalsIgnoreCase("twitter")) {
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

