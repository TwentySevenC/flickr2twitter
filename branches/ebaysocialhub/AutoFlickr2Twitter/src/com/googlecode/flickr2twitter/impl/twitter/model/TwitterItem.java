/**
 * 
 */
package com.googlecode.flickr2twitter.impl.twitter.model;

import twitter4j.Status;

import com.googlecode.flickr2twitter.model.IGeoItem;
import com.googlecode.flickr2twitter.model.Item;
import com.googlecode.flickr2twitter.model.ItemGeoData;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class TwitterItem extends Item implements IGeoItem {
	private ItemGeoData geoData;

	/**
	 * 
	 */
	public TwitterItem(Status status) {
		super();
		setTitle(status.getText());
		setId(String.valueOf(status.getId()));
		if (status.getGeoLocation() != null) {
			geoData = new ItemGeoData(status.getGeoLocation().getLongitude(), 
					status.getGeoLocation().getLatitude());
		}
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IGeoItem#getGeoData()
	 */
	@Override
	public ItemGeoData getGeoData() {
		return geoData;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IGeoItem#setGeoData(com.googlecode.flickr2twitter.model.ItemGeoData)
	 */
	@Override
	public void setGeoData(ItemGeoData geoData) {
		
	}

}
