/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.datastore.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.gmail.yuyang226.autoflickr2twitter.core.GlobalDefaultConfiguration;
import com.gmail.yuyang226.autoflickr2twitter.impl.flickr.SourceServiceProviderFlickr;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
@PersistenceCapable
public class GlobalSourceApplicationService extends GlobalApplicationConfig {
	

	@Persistent
	private String sourceAppApiKey = GlobalDefaultConfiguration.getInstance().getFlickrApiKey();
	
	@Persistent
	private String sourceAppSecret = GlobalDefaultConfiguration.getInstance().getFlickrSecret();
	
	
	/**
	 * 
	 */
	public GlobalSourceApplicationService() {
		super();
		//default values
		super.setAppName("Flickr");
		super.setProviderId(SourceServiceProviderFlickr.ID);
		super.setDescription("The world's leading online photo storage service");
		super.setAuthPagePath(null); //TODO set the default auth page path
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
