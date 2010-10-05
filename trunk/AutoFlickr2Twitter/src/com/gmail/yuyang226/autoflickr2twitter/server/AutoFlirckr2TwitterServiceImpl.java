/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.yuyang226.autoflickr2twitter.client.AutoFlickr2TwitterService;
import com.gmail.yuyang226.autoflickr2twitter.core.FlickrAuthTokenFetcher;
import com.gmail.yuyang226.autoflickr2twitter.core.ServiceRunner;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author yayu
 *
 */
public class AutoFlirckr2TwitterServiceImpl extends RemoteServiceServlet
		implements AutoFlickr2TwitterService {

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
	public String authorize() throws Exception {
		return FlickrAuthTokenFetcher.authrorize();
	}

	@Override
	public String testToken(String frob) throws Exception {
		return FlickrAuthTokenFetcher.test(frob);
	}

}
