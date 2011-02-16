/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest;

import org.restlet.resource.Delete;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import twitter4j.internal.logging.Logger;

import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory.Permission;
import com.googlecode.flickr2twitter.datastore.model.User;
import com.googlecode.flickr2twitter.services.rest.models.IUserResource;
import com.googlecode.flickr2twitter.services.rest.models.UserModel;

/**
 * @author yayu
 *
 */
public class UserServerResource extends ServerResource implements IUserResource {
	private static final Logger log = Logger.getLogger(UserServerResource.class);
	
	/**
	 * 
	 */
	public UserServerResource() {
		super();
	}

	@Post
	public UserModel retrieve(String userEmail) {
		log.info("Retrieving user information for -> " + userEmail);
		User user = MyPersistenceManagerFactory.getUser(userEmail);
		if (user != null) {
			return new UserModel(user.getUserId().getEmail(), 
					user.getPassword(), user.getPermission(), user.getScreenName());
		}
		return null;
	}

	@Put
	public boolean registerNewUser(UserModel user) {
		if (MyPersistenceManagerFactory.createNewUser(user.getUserId(), 
				user.getPassword(), user.getScreenName(), Permission.valueOf(user.getPermission())) != null)
			return true;
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.services.rest.models.IUserResource#login(java.lang.String, java.lang.String)
	 */
	@Post
	public UserModel login(String userEmail, String password) {
		User user = null;
		log.info("Retrieving user information for -> " + userEmail);
		user = MyPersistenceManagerFactory.getLoginUser(userEmail, password);
		if (user != null) {
			return new UserModel(user.getUserId().getEmail(), 
					user.getPassword(), user.getPermission(), user.getScreenName());
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.services.rest.models.IUserResource#openidLogin(java.lang.String, java.lang.String)
	 */
	@Post
	public UserModel openidLogin(String userEmail) {
		log.info("Retrieving opendid user information for -> " + userEmail);
		User user = MyPersistenceManagerFactory.getOpenIdLoginUser(userEmail);
		if (user != null) {
			return new UserModel(user.getUserId().getEmail(), 
					user.getPassword(), user.getPermission(), user.getScreenName());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.services.rest.models.IUserResource#remove(com.googlecode.flickr2twitter.services.rest.models.UserModel)
	 */
	@Delete
	public boolean remove(UserModel user) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
