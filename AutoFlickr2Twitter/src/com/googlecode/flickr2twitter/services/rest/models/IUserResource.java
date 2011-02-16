/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest.models;

import org.restlet.resource.Delete;
import org.restlet.resource.Post;
import org.restlet.resource.Put;


/**
 * @author yayu
 *
 */
public interface IUserResource {

	@Post
	public UserModel retrieve(String userEmail);
	
	@Post
	public UserModel login(String userEmail, String password);
	
	@Post
	public UserModel openidLogin(String userEmail);

	@Put
	public boolean registerNewUser(UserModel user);

	@Delete
	public boolean remove(UserModel user);

}
