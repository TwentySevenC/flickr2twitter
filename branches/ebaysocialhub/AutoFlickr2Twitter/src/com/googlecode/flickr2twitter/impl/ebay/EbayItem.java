/**
 * 
 */
package com.googlecode.flickr2twitter.impl.ebay;

/**
 * @author hochen
 * 
 */
public class EbayItem {

	private final String sellerId;
	private long itemId;
	private String title;
	private String description;
	private String galleryURL;
	private String viewItemURL;

	public EbayItem(String sellerId) {
		this.sellerId = sellerId;
	}

	/**
	 * @return the itemId
	 */
	public long getItemId() {
		return itemId;
	}

	/**
	 * @param itemId
	 *            the itemId to set
	 */
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the galleryURL
	 */
	public String getGalleryURL() {
		return galleryURL;
	}

	/**
	 * @param galleryURL
	 *            the galleryURL to set
	 */
	public void setGalleryURL(String galleryURL) {
		this.galleryURL = galleryURL;
	}

	/**
	 * @return the viewItemURL
	 */
	public String getViewItemURL() {
		return viewItemURL;
	}

	/**
	 * @param viewItemURL
	 *            the viewItemURL to set
	 */
	public void setViewItemURL(String viewItemURL) {
		this.viewItemURL = viewItemURL;
	}

	public String getSellerId() {
		return sellerId;
	}

	@Override
	public String toString() {
		return "EbayItem [sellerId=" + sellerId + ", itemId=" + itemId
				+ ", title=" + title + ", description=" + description
				+ ", galleryURL=" + galleryURL + ", viewItemURL=" + viewItemURL
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((galleryURL == null) ? 0 : galleryURL.hashCode());
		result = prime * result + (int) (itemId ^ (itemId >>> 32));
		result = prime * result
				+ ((sellerId == null) ? 0 : sellerId.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result
				+ ((viewItemURL == null) ? 0 : viewItemURL.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EbayItem other = (EbayItem) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (galleryURL == null) {
			if (other.galleryURL != null)
				return false;
		} else if (!galleryURL.equals(other.galleryURL))
			return false;
		if (itemId != other.itemId)
			return false;
		if (sellerId == null) {
			if (other.sellerId != null)
				return false;
		} else if (!sellerId.equals(other.sellerId))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (viewItemURL == null) {
			if (other.viewItemURL != null)
				return false;
		} else if (!viewItemURL.equals(other.viewItemURL))
			return false;
		return true;
	}

}
