/**
 * 
 */
package com.googlecode.flickr2twitter.datastore.model;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class UserServiceConfig {
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
	private String userSiteUrl;
	
	@Persistent
	private String serviceProviderId;
	
	@Persistent
	private List<ConfigProperty> addtionalParameters;
	
	@Persistent
	private boolean enabled = true;
	
	/**
	 * 
	 */
	public UserServiceConfig() {
		super();
	}

	/**
	 * @return the key
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/**
	 * @return the serviceUserId
	 */
	public String getServiceUserId() {
		return serviceUserId;
	}

	/**
	 * @param serviceUserId the serviceUserId to set
	 */
	public void setServiceUserId(String serviceUserId) {
		this.serviceUserId = serviceUserId;
	}

	/**
	 * @return the serviceUserName
	 */
	public String getServiceUserName() {
		return serviceUserName;
	}

	/**
	 * @param serviceUserName the serviceUserName to set
	 */
	public void setServiceUserName(String serviceUserName) {
		this.serviceUserName = serviceUserName;
	}

	/**
	 * @return the userSiteUrl
	 */
	public String getUserSiteUrl() {
		return userSiteUrl;
	}

	/**
	 * @param userSiteUrl the userSiteUrl to set
	 */
	public void setUserSiteUrl(String userSiteUrl) {
		this.userSiteUrl = userSiteUrl;
	}

	/**
	 * @return the serviceProviderId
	 */
	public String getServiceProviderId() {
		return serviceProviderId;
	}

	/**
	 * @param serviceProviderId the serviceProviderId to set
	 */
	public void setServiceProviderId(String serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}

	/**
	 * @return the addtionalParameters
	 */
	public List<ConfigProperty> getAddtionalParameters() {
		return addtionalParameters;
	}
	
	/**
	 * @param addtionalParameter the addtionalParameter to add
	 */
	public boolean addAddtionalParameter(ConfigProperty addtionalParameter) {
		if (this.addtionalParameters == null) {
			this.addtionalParameters = new ArrayList<ConfigProperty>();
		}
		return this.addtionalParameters.add(addtionalParameter);
	}

	/**
	 * @param addtionalParameters the addtionalParameters to set
	 */
	public void setAddtionalParameters(List<ConfigProperty> addtionalParameters) {
		this.addtionalParameters = addtionalParameters;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
				+ ((serviceProviderId == null) ? 0 : serviceProviderId
						.hashCode());
		result = prime * result
				+ ((serviceUserId == null) ? 0 : serviceUserId.hashCode());
		result = prime * result
				+ ((serviceUserName == null) ? 0 : serviceUserName.hashCode());
		result = prime * result
				+ ((userEmail == null) ? 0 : userEmail.hashCode());
		result = prime * result
				+ ((userSiteUrl == null) ? 0 : userSiteUrl.hashCode());
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
		if (!(obj instanceof UserServiceConfig))
			return false;
		UserServiceConfig other = (UserServiceConfig) obj;
		if (serviceProviderId == null) {
			if (other.serviceProviderId != null)
				return false;
		} else if (!serviceProviderId.equals(other.serviceProviderId))
			return false;
		if (serviceUserId == null) {
			if (other.serviceUserId != null)
				return false;
		} else if (!serviceUserId.equals(other.serviceUserId))
			return false;
		if (serviceUserName == null) {
			if (other.serviceUserName != null)
				return false;
		} else if (!serviceUserName.equals(other.serviceUserName))
			return false;
		if (userEmail == null) {
			if (other.userEmail != null)
				return false;
		} else if (!userEmail.equals(other.userEmail))
			return false;
		if (userSiteUrl == null) {
			if (other.userSiteUrl != null)
				return false;
		} else if (!userSiteUrl.equals(other.userSiteUrl))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserServiceConfig [addtionalParameters=" + addtionalParameters
				+ ", key=" + key + ", serviceProviderId=" + serviceProviderId
				+ ", serviceUserId=" + serviceUserId + ", serviceUserName="
				+ serviceUserName + ", userEmail=" + userEmail
				+ ", userSiteUrl=" + userSiteUrl + "]";
	}

}
