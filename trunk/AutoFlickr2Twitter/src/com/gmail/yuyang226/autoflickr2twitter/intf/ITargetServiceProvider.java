/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.intf;

import java.util.List;

import com.gmail.yuyang226.autoflickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.UserTargetService;
import com.gmail.yuyang226.autoflickr2twitter.model.IItem;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public interface ITargetServiceProvider {

	/**
	 * A lower case representation of the source service provider.
	 * @return a string to represent the underlying target service provider.
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
	 * Post an update for a new item
	 * @param item
	 * @throws Exception
	 */
	public void postUpdate(GlobalTargetApplicationService globalAppConfig, 
			UserTargetService targetConfig, List<IItem> items) throws Exception;
}
