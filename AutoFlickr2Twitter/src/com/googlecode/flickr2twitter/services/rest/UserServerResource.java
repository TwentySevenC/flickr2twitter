/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

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

	/**
	 * 
	 */
	public UserServerResource() {
		super();
	}

	@Get
	public UserModel retrieve(String userEmail) {
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

	@Delete
	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
