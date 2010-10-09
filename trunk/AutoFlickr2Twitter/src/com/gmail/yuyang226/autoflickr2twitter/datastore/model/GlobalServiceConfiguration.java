/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.datastore.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.gmail.yuyang226.autoflickr2twitter.impl.flickr.SourceServiceProviderFlickr;
import com.gmail.yuyang226.autoflickr2twitter.impl.twitter.TargetServiceProviderTwitter;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
@PersistenceCapable
public class GlobalServiceConfiguration {
	
	
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String key;
	
	/**
	 * The minimum upload time to be synched to your target services
	 */
	@Persistent
	private long minUploadTime = 602000L;
	
	@Persistent
	private String sourceProviderId = SourceServiceProviderFlickr.ID;
	
	@Persistent
	private String sourceAppApiKey = "cf133e9bab9b574fa5f8166c9ecf6455";
	
	@Persistent
	private String sourceAppSecret = "d9b66ded5812c3a8";
	
	@Persistent
	private String targetProviderId = TargetServiceProviderTwitter.ID;
	
	@Persistent
	private String targetAppConsumerId = "uYXnouUfdW3W8FWQYs6TrQ";
	
	@Persistent
	private String targetAppConsumerSecret = "lGwnkGZay86PkoeYKnDdqiz6tq9iHBTiUpWm33iP7d0";

	/**
	 * 
	 */
	public GlobalServiceConfiguration() {
		super();
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the minUploadTime
	 */
	public long getMinUploadTime() {
		return minUploadTime;
	}

	/**
	 * @param minUploadTime the minUploadTime to set
	 */
	public void setMinUploadTime(long minUploadTime) {
		this.minUploadTime = minUploadTime;
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
