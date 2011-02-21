/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest.models;

import org.restlet.resource.Post;


/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public interface ISociaHubServicesResource {
	
	/**
	 * @param data The format is {providerId}/{userEmail}/{token}/{tokenSecret}/{verifier}
	 */
	@Post
	public void addUserTargetServiceConfig(String data);


}
