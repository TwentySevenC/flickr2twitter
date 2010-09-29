/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.intf;

import java.util.List;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public interface ISourceServiceProvider<T> {
	
	/**
	 * A lower case representation of the source service provider.
	 * @return a string to represent the underlying source service provider.
	 */
	public String getId();
	
	/**
	 * Request a token and store it in the given data store service.
	 * 
	 * @param datastore
	 * @return
	 * @throws Exception
	 */
	public boolean storeToken(IDataStoreService datastore) throws Exception;
	
	/**
	 * Get the list of latest items
	 * @return
	 * @throws Exception
	 */
	public List<T> getLatestItems() throws Exception;

}
