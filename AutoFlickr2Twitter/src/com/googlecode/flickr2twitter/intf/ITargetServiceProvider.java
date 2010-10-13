/**
 * 
 */
package com.googlecode.flickr2twitter.intf;

import java.util.List;

import com.googlecode.flickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.googlecode.flickr2twitter.datastore.model.UserTargetServiceConfig;
import com.googlecode.flickr2twitter.model.IItem;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public interface ITargetServiceProvider extends IServiceAuthorizer, IServiceProvider {
	
	/**
	 * Post an update for a new item
	 * @param item
	 * @throws Exception
	 */
	public void postUpdate(GlobalTargetApplicationService globalAppConfig, 
			UserTargetServiceConfig targetConfig, List<IItem> items) throws Exception;
}
