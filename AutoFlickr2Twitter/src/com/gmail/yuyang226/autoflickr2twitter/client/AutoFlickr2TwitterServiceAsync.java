/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author yayu
 *
 */
public interface AutoFlickr2TwitterServiceAsync {

	void recheck(AsyncCallback<Void> callback);

	void authorize(AsyncCallback<String> callback) throws Exception;

	void testToken(String frob, AsyncCallback<String> callback);

	void authorizeTwitter(AsyncCallback<String> callback);

	void readyTwitterToken(AsyncCallback<String> callback);

	void generateTestData(AsyncCallback<Void> callback);


	
}
