/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author yayu
 *
 */
@RemoteServiceRelativePath("recheck")
public interface AutoFlickr2TwitterService extends RemoteService {

	public void recheck();
}
