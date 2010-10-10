/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.intf;

import java.util.List;

import com.gmail.yuyang226.autoflickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.UserSourceService;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public interface ISourceServiceProvider<T> extends IServiceAuthorizer{
	
	/**
	 * A lower case representation of the source service provider.
	 * @return a string to represent the underlying source service provider.
	 */
	public String getId();
	
	/**
	 * Get the list of latest items
	 * @return
	 * @throws Exception
	 */
	public List<T> getLatestItems(GlobalServiceConfiguration globalConfig, UserSourceService sourceService) throws Exception;

}
