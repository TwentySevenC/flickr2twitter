/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest.models;

import java.util.Map;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class UserTargetServiceConfigModel extends UserServiceConfigModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public UserTargetServiceConfigModel() {
		super();
	}

	/**
	 * @param userEmail
	 * @param serviceUserId
	 * @param serviceUserName
	 * @param userSiteUrl
	 * @param serviceProviderId
	 * @param additionalParameters
	 * @param enabled
	 */
	public UserTargetServiceConfigModel(String userEmail, String serviceUserId,
			String serviceUserName, String userSiteUrl,
			String serviceProviderId, 
			Map<String, String> additionalParameters, boolean enabled, 
			String serviceAccessToken, String serviceTokenSecret) {
		super(userEmail, serviceUserId, serviceUserName, userSiteUrl,
				serviceProviderId, 
				additionalParameters, enabled);
		this.setServiceAccessToken(serviceAccessToken);
		this.setServiceTokenSecret(serviceTokenSecret);
	}
	
}
