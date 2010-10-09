/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.datastore.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
@PersistenceCapable
public class UserSourceService {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String userEmail;
	
	@Persistent
	private String serviceUserId;
	
	@Persistent
	private String serviceUserName;
	
	@Persistent
	private String serviceAccessToken;
	
	@Persistent
	private String sourceServiceProviderId;

	/**
	 * 
	 */
	public UserSourceService() {
		super();
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getServiceUserId() {
		return serviceUserId;
	}

	public void setServiceUserId(String serviceUserId) {
		this.serviceUserId = serviceUserId;
	}

	public String getServiceUserName() {
		return serviceUserName;
	}

	public void setServiceUserName(String serviceUserName) {
		this.serviceUserName = serviceUserName;
	}

	public String getServiceAccessToken() {
		return serviceAccessToken;
	}

	public void setServiceAccessToken(String serviceAccessToken) {
		this.serviceAccessToken = serviceAccessToken;
	}

	public String getSourceServiceProviderId() {
		return sourceServiceProviderId;
	}

	public void setSourceServiceProviderId(String sourceServiceProviderId) {
		this.sourceServiceProviderId = sourceServiceProviderId;
	}

	public Key getKey() {
		return key;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((serviceAccessToken == null) ? 0 : serviceAccessToken
						.hashCode());
		result = prime * result
				+ ((serviceUserId == null) ? 0 : serviceUserId.hashCode());
		result = prime
				* result
				+ ((sourceServiceProviderId == null) ? 0
						: sourceServiceProviderId.hashCode());
		result = prime * result
				+ ((userEmail == null) ? 0 : userEmail.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserSourceService))
			return false;
		UserSourceService other = (UserSourceService) obj;
		if (serviceAccessToken == null) {
			if (other.serviceAccessToken != null)
				return false;
		} else if (!serviceAccessToken.equals(other.serviceAccessToken))
			return false;
		if (serviceUserId == null) {
			if (other.serviceUserId != null)
				return false;
		} else if (!serviceUserId.equals(other.serviceUserId))
			return false;
		if (sourceServiceProviderId == null) {
			if (other.sourceServiceProviderId != null)
				return false;
		} else if (!sourceServiceProviderId
				.equals(other.sourceServiceProviderId))
			return false;
		if (userEmail == null) {
			if (other.userEmail != null)
				return false;
		} else if (!userEmail.equals(other.userEmail))
			return false;
		return true;
	}
	
	

}
