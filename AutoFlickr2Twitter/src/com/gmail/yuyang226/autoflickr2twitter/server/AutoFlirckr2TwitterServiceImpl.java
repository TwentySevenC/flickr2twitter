/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.server;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.yuyang226.autoflickr2twitter.client.AutoFlickr2TwitterService;
import com.gmail.yuyang226.autoflickr2twitter.core.ServiceFactory;
import com.gmail.yuyang226.autoflickr2twitter.core.ServiceRunner;
import com.gmail.yuyang226.autoflickr2twitter.datastore.MyPersistenceManagerFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author yayu
 *
 */
public class AutoFlirckr2TwitterServiceImpl extends RemoteServiceServlet
		implements AutoFlickr2TwitterService {
//	private TwitterPoster twitterPoster = new TwitterPoster();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//super.doGet(req, resp);
		recheck();
		resp.setStatus(HttpServletResponse.SC_OK);
	}

	/**
	 * 
	 */
	public AutoFlirckr2TwitterServiceImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.gmail.yuyang226.autoflickr2twitter.client.AutoFlickr2TwitterService#recheck()
	 */
	@Override
	public void recheck() {
		ServiceRunner.execute();
	}

	@Override
	public Map<String, Object> authorize(boolean sourceProvider, String providerId) throws Exception {
		if (sourceProvider == true) {
			return ServiceFactory.getSourceServiceProvider(providerId).requestAuthorization();
		} else {
			return ServiceFactory.getTargetServiceProvider(providerId).requestAuthorization();
		}
	}

	@Override
	public String testToken(boolean sourceProvider, String providerId, String userEmail, Map<String, Object> data) throws Exception {
		if (sourceProvider == true) {
			return ServiceFactory.getSourceServiceProvider(providerId).readyAuthorization(userEmail, data);
		} else {
			return ServiceFactory.getTargetServiceProvider(providerId).readyAuthorization(userEmail, data);
		}
	}

	/* (non-Javadoc)
	 * @see com.gmail.yuyang226.autoflickr2twitter.client.AutoFlickr2TwitterService#createUser(java.lang.String)
	 */
	@Override
	public void createUser(String userEmail) throws Exception {
		MyPersistenceManagerFactory.createNewUser(
				userEmail, userEmail, userEmail.substring(0, userEmail.indexOf("@")));
	}

}
