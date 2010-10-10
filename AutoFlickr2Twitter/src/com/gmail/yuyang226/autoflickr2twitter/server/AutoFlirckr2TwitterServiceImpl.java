/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.server;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gmail.yuyang226.autoflickr2twitter.client.AutoFlickr2TwitterService;
import com.gmail.yuyang226.autoflickr2twitter.core.ServiceRunner;
import com.gmail.yuyang226.autoflickr2twitter.core.TwitterPoster;
import com.gmail.yuyang226.autoflickr2twitter.datastore.MyPersistenceManagerFactory;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.User;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.UserSourceService;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.UserTargetService;
import com.gmail.yuyang226.autoflickr2twitter.impl.flickr.FlickrAuthTokenFetcher;
import com.gmail.yuyang226.autoflickr2twitter.impl.flickr.SourceServiceProviderFlickr;
import com.gmail.yuyang226.autoflickr2twitter.impl.twitter.TargetServiceProviderTwitter;
import com.google.appengine.api.datastore.Email;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author yayu
 *
 */
public class AutoFlirckr2TwitterServiceImpl extends RemoteServiceServlet
		implements AutoFlickr2TwitterService {
	private TwitterPoster twitterPoster = new TwitterPoster();

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

	/* (non-Javadoc)
	 * @see com.gmail.yuyang226.autoflickr2twitter.client.AutoFlickr2TwitterService#authorizeTwitter()
	 */
	@Override
	public String authorizeTwitter() throws Exception {
		return this.twitterPoster.requestNewToken();
	}

	/* (non-Javadoc)
	 * @see com.gmail.yuyang226.autoflickr2twitter.client.AutoFlickr2TwitterService#readyTwitterToken()
	 */
	@Override
	public String readyTwitterToken() throws Exception {
		return this.twitterPoster.readyTwitterAuthorization();
	}

	@Override
	public void generateTestData() throws Exception {
			
			User user = MyPersistenceManagerFactory.createNewUser(
					"yuyang226@gmail.com", "hello world", "Yayu");
			
			UserSourceService source = new UserSourceService();
			source.setServiceAccessToken("72157624934440413-81a5395969daa49e");
			source.setServiceUserId("54374999@N05");
			source.setServiceUserName("Toby Yu");
			source.setSourceServiceProviderId(SourceServiceProviderFlickr.ID);
			source.setUserEmail(user.getUserId().getEmail());
			MyPersistenceManagerFactory.addSourceServiceApp(user, source);
			
			
			UserTargetService target = new UserTargetService();
			target.setServiceAccessToken("196514413-IlwVQ5fVMJKLsqCJXLxFXuzYCOYsEVjuMvVhHJcW");
			target.setServiceTokenSecret("q2SnSzyUj0dZNsDuftpOZg3XMYgsF6szUe8zpRYBU");
			target.setServiceUserId("yayu226");
			target.setServiceUserName("Yuy");
			target.setTargetServiceProviderId(TargetServiceProviderTwitter.ID);
			target.setUserEmail(user.getUserId().getEmail());
			MyPersistenceManagerFactory.addTargetServiceApp(user, target);
		
		
	}

}
