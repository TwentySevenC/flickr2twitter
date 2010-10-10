/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.datastore.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.gmail.yuyang226.autoflickr2twitter.core.GlobalDefaultConfiguration;
import com.gmail.yuyang226.autoflickr2twitter.impl.flickr.SourceServiceProviderFlickr;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
@PersistenceCapable
public class GlobalSourceApplicationService {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String sourceProviderId = SourceServiceProviderFlickr.ID;

	@Persistent
	private String sourceAppApiKey = GlobalDefaultConfiguration.getInstance().getFlickrApiKey();
	
	@Persistent
	private String sourceAppSecret = GlobalDefaultConfiguration.getInstance().getFlickrSecret();
	
	/**
	 * 
	 */
	public GlobalSourceApplicationService() {
		super();
	}

	/**
	 * @return the sourceProviderId
	 */
	public String getSourceProviderId() {
		return sourceProviderId;
	}

	/**
	 * @param sourceProviderId the sourceProviderId to set
	 */
	public void setSourceProviderId(String sourceProviderId) {
		this.sourceProviderId = sourceProviderId;
	}

	/**
	 * @return the sourceAppApiKey
	 */
	public String getSourceAppApiKey() {
		return sourceAppApiKey;
	}

	/**
	 * @param sourceAppApiKey the sourceAppApiKey to set
	 */
	public void setSourceAppApiKey(String sourceAppApiKey) {
		this.sourceAppApiKey = sourceAppApiKey;
	}

	/**
	 * @return the sourceAppSecret
	 */
	public String getSourceAppSecret() {
		return sourceAppSecret;
	}

	/**
	 * @param sourceAppSecret the sourceAppSecret to set
	 */
	public void setSourceAppSecret(String sourceAppSecret) {
		this.sourceAppSecret = sourceAppSecret;
	}
	
	

}
