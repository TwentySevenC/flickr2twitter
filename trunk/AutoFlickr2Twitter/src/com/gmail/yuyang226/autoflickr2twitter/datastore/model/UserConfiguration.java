/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.datastore.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.gmail.yuyang226.autoflickr2twitter.impl.flickr.SourceServiceProviderFlickr;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
@PersistenceCapable
public class UserConfiguration {
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String flickrUserId;
	
	@Persistent
	private String flickrUserName;
	
	@Persistent
	private String flickrToken;
	
	@Persistent
	private String sourceServiceProviderId = SourceServiceProviderFlickr.ID;

	/**
	 * 
	 */
	public UserConfiguration() {
		super();
	}

	public UserConfiguration(String flickrUserId, String flickrUserName,
			String flickrToken) {
		super();
		this.flickrUserId = flickrUserId;
		this.flickrUserName = flickrUserName;
		this.flickrToken = flickrToken;
	}

	public String getFlickrUserId() {
		return flickrUserId;
	}

	public void setFlickrUserId(String flickrUserId) {
		this.flickrUserId = flickrUserId;
	}

	public String getFlickrUserName() {
		return flickrUserName;
	}

	public void setFlickrUserName(String flickrUserName) {
		this.flickrUserName = flickrUserName;
	}

	public String getFlickrToken() {
		return flickrToken;
	}

	public void setFlickrToken(String flickrToken) {
		this.flickrToken = flickrToken;
	}

	public void setSourceServiceProviderId(String sourceServiceProviderId) {
		this.sourceServiceProviderId = sourceServiceProviderId;
	}

	public String getSourceServiceProviderId() {
		return sourceServiceProviderId;
	}

	@Override
	public String toString() {
		return "UserConfiguration [flickrToken=" + flickrToken
				+ ", flickrUserId=" + flickrUserId + ", flickrUserName="
				+ flickrUserName + ", sourceServiceProviderId="
				+ sourceServiceProviderId + "]";
	}

}
