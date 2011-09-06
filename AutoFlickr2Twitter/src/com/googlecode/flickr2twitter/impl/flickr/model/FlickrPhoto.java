/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickr2twitter.impl.flickr.model;

import java.util.Collection;
import java.util.Date;

import com.aetrion.flickr.photos.GeoData;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.tags.Tag;
import com.googlecode.flickr2twitter.impl.flickr.FlickrBaseEncoder;
import com.googlecode.flickr2twitter.model.IGeoItem;
import com.googlecode.flickr2twitter.model.IPhoto;
import com.googlecode.flickr2twitter.model.IShortUrl;
import com.googlecode.flickr2twitter.model.Item;
import com.googlecode.flickr2twitter.model.ItemGeoData;

public class FlickrPhoto extends Item implements IPhoto, IGeoItem, IShortUrl{
    private static final long serialVersionUID = 12L;
    
    private Photo photo;

    private ItemGeoData geoData;

    public FlickrPhoto(Photo photo) {
    	super();
    	this.photo = photo;
    	if (photo.hasGeoData()) {
    		GeoData data = photo.getGeoData();
    		geoData = new ItemGeoData(data.getLongitude(), data.getLatitude());
    	}
    	super.setId(photo.getId());
    	super.setTitle(photo.getTitle());
    	super.setDescription(photo.getDescription());
    }
    

	public boolean isPublicFlag() {
        return this.photo.isPublicFlag();
    }

    public Date getDateAdded() {
        return this.photo.getDateAdded();
    }

    public Date getDatePosted() {
        return this.photo.getDatePosted();
    }

    public Date getDateTaken() {
        return this.photo.getDateTaken();
    }

    public void setDateTaken(Date dateTaken) {
        this.photo.setDateTaken(dateTaken);
    }

    public Date getLastUpdate() {
        return this.photo.getLastUpdate();
    }

    public String getUrl() {
        return this.photo.getUrl();
    }

    public void setUrl(String url) {
    	this.photo.setUrl(url);
    }

    public ItemGeoData getGeoData() {
        return geoData;
    }

    public void setGeoData(ItemGeoData geoData) {
        this.geoData = geoData;
    }

    public boolean hasGeoData() {
        return geoData != null;
    }

	public Collection<Tag> getTags() {
		return this.photo.getTags();
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IShortUrl#getShortUrl()
	 */
	@Override
	public String getShortUrl() {
		return FlickrBaseEncoder.getShortUrl(this);
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.IItem#setDatePosted(java.util.Date)
	 */
	@Override
	public void setDatePosted(Date datePosted) {
		this.photo.setDatePosted(datePosted);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((photo == null) ? 0 : photo.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof FlickrPhoto))
			return false;
		FlickrPhoto other = (FlickrPhoto) obj;
		if (photo == null) {
			if (other.photo != null)
				return false;
		} else if (!photo.equals(other.photo))
			return false;
		return true;
	}

}
