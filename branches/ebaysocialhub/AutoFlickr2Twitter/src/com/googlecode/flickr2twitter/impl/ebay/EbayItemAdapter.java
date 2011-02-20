package com.googlecode.flickr2twitter.impl.ebay;

import com.googlecode.flickr2twitter.model.ILinkableItem;

/**
 * @author John Liu(zhhong.liu@gmail.com)
 *
 */
public class EbayItemAdapter implements ILinkableItem{

	private final EbayItem ebayItem;
	
	public EbayItemAdapter(EbayItem item) {
		this.ebayItem = item;
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.ILinkableItem#setUrl(java.lang.String)
	 */
	@Override
	public void setUrl(String url) {
		this.ebayItem.setViewItemURL(url);
	}

	/* (non-Javadoc)
	 * @see com.googlecode.flickr2twitter.model.ILinkableItem#getUrl()
	 */
	@Override
	public String getUrl() {
		return ebayItem.getViewItemURL();
	}

	@Override
	public String getId() {
		return String.valueOf(ebayItem.getItemId());
	}

	@Override
	public String getTitle() {
		return ebayItem.getTitle();
	}

	@Override
	public String getDescription() {
		return ebayItem.getDescription();
	}
	
}
