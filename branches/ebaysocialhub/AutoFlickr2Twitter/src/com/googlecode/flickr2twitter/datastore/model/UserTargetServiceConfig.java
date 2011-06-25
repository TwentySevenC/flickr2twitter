/**
 * 
 */
package com.googlecode.flickr2twitter.datastore.model;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 * 
 */
@PersistenceCapable
public class UserTargetServiceConfig extends UserServiceConfig implements
		Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public UserTargetServiceConfig() {
		super();
	}

}
