/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.datastore.model;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 * 
 */
@PersistenceCapable
public class UserSourceServiceConfig extends UserServiceConfig implements
		Serializable {

	private static final long serialVersionUID = 1L;

	@Persistent
	private String serviceAccessToken;

	/**
	 * 
	 */
	public UserSourceServiceConfig() {
		super();
	}

	public String getServiceAccessToken() {
		return serviceAccessToken;
	}

	public void setServiceAccessToken(String serviceAccessToken) {
		this.serviceAccessToken = serviceAccessToken;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((serviceAccessToken == null) ? 0 : serviceAccessToken
						.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof UserSourceServiceConfig))
			return false;
		UserSourceServiceConfig other = (UserSourceServiceConfig) obj;
		if (serviceAccessToken == null) {
			if (other.serviceAccessToken != null)
				return false;
		} else if (!serviceAccessToken.equals(other.serviceAccessToken))
			return false;
		return true;
	}

}
