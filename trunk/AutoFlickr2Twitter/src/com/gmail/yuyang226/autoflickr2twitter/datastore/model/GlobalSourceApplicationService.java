/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.datastore.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
@PersistenceCapable
public class GlobalSourceApplicationService extends GlobalApplicationConfig {
	

	@Persistent
	private String sourceAppApiKey;
	
	@Persistent
	private String sourceAppSecret;
	
	
	/**
	 * 
	 */
	public GlobalSourceApplicationService() {
		super();
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
