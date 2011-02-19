package com.googlecode.flickr2twitter.impl.ebay;

import com.googlecode.flickr2twitter.model.IMsgItem;
import com.googlecode.flickr2twitter.urlshorteners.BitLyUtils;

public class EbayItemAdapter implements IMsgItem{

	private final EbayItem ebayItem;
	
	public EbayItemAdapter(EbayItem item) {
		this.ebayItem = item;
	}

	@Override
	public String getMessage() {
		// sellerId + "published new item in ebay.com, " + url
		String msg = ebayItem.getSellerId();
		msg += " published a new item (" + getTitle() + ") in ebay.com. ";
		msg += BitLyUtils.shortenUrl(ebayItem.getViewItemURL());
		
		return msg;
	}

	@Override
	public String getId() {
		return "" + ebayItem.getItemId();
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
