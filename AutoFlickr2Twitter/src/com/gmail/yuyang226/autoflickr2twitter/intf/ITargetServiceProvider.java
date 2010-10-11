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
public interface ITargetServiceProvider extends IServiceAuthorizer, IServiceProvider {
	
	/**
	 * Post an update for a new item
	 * @param item
	 * @throws Exception
	 */
	public void postUpdate(GlobalTargetApplicationService globalAppConfig, 
			UserTargetService targetConfig, List<IItem> items) throws Exception;
}
