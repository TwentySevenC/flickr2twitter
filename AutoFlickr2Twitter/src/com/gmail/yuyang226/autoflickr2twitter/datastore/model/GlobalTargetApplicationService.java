/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.datastore.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.gmail.yuyang226.autoflickr2twitter.core.GlobalDefaultConfiguration;
import com.gmail.yuyang226.autoflickr2twitter.impl.twitter.TargetServiceProviderTwitter;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
@PersistenceCapable
public class GlobalTargetApplicationService extends GlobalApplicationConfig {

	@Persistent
	private String targetAppConsumerId = GlobalDefaultConfiguration.getInstance().getTwitterConsumerId();
	
	@Persistent
	private String targetAppConsumerSecret = GlobalDefaultConfiguration.getInstance().getTwitterConsumerSecret();

	
	/**
	 * 
	 */
	public GlobalTargetApplicationService() {
		super();
		//default values
		super.setAppName("Twitter");
		super.setProviderId(TargetServiceProviderTwitter.ID);
		super.setDescription("The world's leading online micro-blog service");
		super.setAuthPagePath(null); //TODO set the default auth page path
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
