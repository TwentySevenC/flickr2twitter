/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.intf;

import com.gmail.yuyang226.autoflickr2twitter.datastore.model.GlobalApplicationConfig;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 * 
 */
public interface IServiceProvider<T extends GlobalApplicationConfig> {

	/**
	 * A lower case representation of the source service provider.
	 * 
	 * @return a string to represent the underlying source service provider.
	 */
	public String getId();

	public T createDefaultGlobalApplicationConfig();

}
