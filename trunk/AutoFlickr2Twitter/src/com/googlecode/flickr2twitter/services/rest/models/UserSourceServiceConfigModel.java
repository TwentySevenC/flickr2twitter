/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest.models;

import java.util.Map;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class UserSourceServiceConfigModel extends UserServiceConfigModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	/**
	 * 
	 */
	public UserSourceServiceConfigModel() {
		super();
	}

	/**
	 * @param userEmail
	 * @param serviceUserId
	 * @param serviceUserName
	 * @param userSiteUrl
	 * @param serviceProviderId
	 * @param additionalParamsPersitent
	 * @param additionalParameters
	 * @param enabled
	 */
	public UserSourceServiceConfigModel(String userEmail, String serviceUserId,
			String serviceUserName, String userSiteUrl,
			String serviceProviderId, 
			Map<String, String> additionalParameters, boolean enabled, String serviceAccessToken) {
		super(userEmail, serviceUserId, serviceUserName, userSiteUrl,
				serviceProviderId, additionalParameters, enabled);
		this.setServiceAccessToken(serviceAccessToken);
	}

	
}
