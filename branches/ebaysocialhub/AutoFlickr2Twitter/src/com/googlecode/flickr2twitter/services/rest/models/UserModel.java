/**
 * 
 */
package com.googlecode.flickr2twitter.services.rest.models;

import java.io.Serializable;

/**
 * @author yayu
 *
 */
public class UserModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * this should be the email address
	 */
	private String userId;
	
	private String password;
	
	private String permission;
	
	/**
	 * an optional field
	 */
	private String screenName;

	/**
	 * 
	 */
	public UserModel() {
		super();
	}

	public UserModel(String userId, String password, String screenName) {
		this(userId, password, "NORMAL", screenName);
	}


	public UserModel(String userId, String password, String permission,
			String screenName) {
		super();
		this.userId = userId;
		this.password = password;
		this.permission = permission;
		this.screenName = screenName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserModel other = (UserModel) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserModel [userId=" + userId + ", password=" + password
				+ ", permission=" + permission + ", screenName=" + screenName
				+ "]";
	}
	

}
