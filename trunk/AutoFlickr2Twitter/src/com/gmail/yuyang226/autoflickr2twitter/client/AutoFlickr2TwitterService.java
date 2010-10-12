/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.client;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author yayu
 * @deprecated do not use GWT anymore
 */
@RemoteServiceRelativePath("recheck")
public interface AutoFlickr2TwitterService extends RemoteService {

	public void recheck();
	
	public Map<String, Object> authorize(boolean sourceProvider, String providerId) throws Exception;
	
	public String testToken(boolean sourceProvider, String providerId, String userEmail, Map<String, Object> data) throws Exception;
	
	public void createUser(String userEmail) throws Exception;
}
