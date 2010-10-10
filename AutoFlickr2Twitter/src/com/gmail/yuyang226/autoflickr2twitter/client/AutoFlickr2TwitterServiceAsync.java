/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.client;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author yayu
 *
 */
public interface AutoFlickr2TwitterServiceAsync {

	void recheck(AsyncCallback<Void> callback);

	void authorize(boolean sourceProvider, String providerId,
			AsyncCallback<Map<String, Object>> callback);

	void testToken(boolean sourceProvider, String providerId, String userEmail,
			Map<String, Object> data, AsyncCallback<String> callback);

	void createUser(String userEmail, AsyncCallback<Void> callback);


	
}
