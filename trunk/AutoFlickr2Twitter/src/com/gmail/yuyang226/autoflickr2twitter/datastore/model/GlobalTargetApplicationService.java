/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.datastore.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.gmail.yuyang226.autoflickr2twitter.core.GlobalDefaultConfiguration;
import com.gmail.yuyang226.autoflickr2twitter.impl.twitter.TargetServiceProviderTwitter;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
@PersistenceCapable
public class GlobalTargetApplicationService {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String targetProviderId = TargetServiceProviderTwitter.ID;

	@Persistent
	private String targetAppConsumerId = GlobalDefaultConfiguration.getInstance().getTwitterConsumerId();
	
	@Persistent
	private String targetAppConsumerSecret = GlobalDefaultConfiguration.getInstance().getTwitterConsumerSecret();

	/**
	 * 
	 */
	public GlobalTargetApplicationService() {
		super();
	}

	/**
	 * @return the targetProviderId
	 */
	public String getTargetProviderId() {
		return targetProviderId;
	}

	/**
	 * @param targetProviderId the targetProviderId to set
	 */
	public void setTargetProviderId(String targetProviderId) {
		this.targetProviderId = targetProviderId;
	}

	/**
	 * @return the targetAppConsumerId
	 */
	public String getTargetAppConsumerId() {
		return targetAppConsumerId;
	}

	/**
	 * @param targetAppConsumerId the targetAppConsumerId to set
	 */
	public void setTargetAppConsumerId(String targetAppConsumerId) {
		this.targetAppConsumerId = targetAppConsumerId;
	}

	/**
	 * @return the targetAppConsumerSecret
	 */
	public String getTargetAppConsumerSecret() {
		return targetAppConsumerSecret;
	}

	/**
	 * @param targetAppConsumerSecret the targetAppConsumerSecret to set
	 */
	public void setTargetAppConsumerSecret(String targetAppConsumerSecret) {
		this.targetAppConsumerSecret = targetAppConsumerSecret;
	}

	
}
