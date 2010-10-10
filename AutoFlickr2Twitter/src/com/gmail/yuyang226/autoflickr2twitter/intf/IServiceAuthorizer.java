/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.intf;

import java.util.Map;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public interface IServiceAuthorizer {
	/**
	 * Request for authorizing the user's source service
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> requestAuthorization() throws Exception;
	
	/**
	 * Request a token and store it in the given data store service.
	 * 
	 * @param datastore
	 * @return
	 * @throws Exception
	 */
	public String readyAuthorization(String userEmail, Map<String, Object> data) throws Exception;

}
