/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.core;

import com.gmail.yuyang226.autoflickr2twitter.impl.flickr.SourceServiceProviderFlickr;
import com.gmail.yuyang226.autoflickr2twitter.intf.ISourceServiceProvider;
import com.gmail.yuyang226.autoflickr2twitter.model.IItem;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class ServiceFactory {

	/**
	 * 
	 */
	public ServiceFactory() {
		super();
	}
	
	public static ISourceServiceProvider<IItem> getSourceServiceProvider() throws Exception {
		return new SourceServiceProviderFlickr();
	}

}
