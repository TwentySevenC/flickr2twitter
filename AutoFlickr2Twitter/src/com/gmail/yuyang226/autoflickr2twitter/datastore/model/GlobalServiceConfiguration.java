/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.datastore.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

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

}