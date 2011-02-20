/*
 * Created on Feb 19, 2011
 */

package com.googlecode.flickr2twitter.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xml.sax.SAXException;

import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.User;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.impl.ebay.EbayUser;
import com.googlecode.flickr2twitter.impl.ebay.GetUserProfileDAO;
import com.googlecode.flickr2twitter.impl.ebay.SourceServiceProviderEbay;

/**
 * @author Emac Shen (shen.bin.1983@gmail.com)
 */
public class EbayConfigServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final String PARA_SELLER_ID = "seller_id";

	public static final String PARA_SEARCH_SELLER_ID = "search_seller_id";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(
				UserAccountServlet.PARA_SESSION_USER);

		if (user == null) {
			req.getSession().setAttribute("message", "Please Login first!");
			resp.sendRedirect("/index.jsp");
			return;
		}

		String userEmail = user.getUserId().getEmail();
		String sellerId = req.getParameter(PARA_SELLER_ID);

		GetUserProfileDAO getUserProfileDAO = new GetUserProfileDAO();

		EbayUser ebayUser = null;
		try {
			ebayUser = getUserProfileDAO.getUserProfile(true, sellerId);
		} catch (SAXException e) {
			throw new ServletException("Unable to found user profile for id: "
					+ sellerId, e);
		}

		UserSourceServiceConfig serviceConfig = new UserSourceServiceConfig();
		// TODO#EMAC.P1 store seller id in user id temporarily
		serviceConfig.setServiceUserId(sellerId);

		String userDisplayName = sellerId;
		String storeName = ebayUser.getStoreName();
		if ((storeName != null) && (storeName.length() > 0)) {
			userDisplayName += " (" + storeName + ")";
		}
		serviceConfig.setServiceUserName(userDisplayName);
		serviceConfig.setServiceProviderId(SourceServiceProviderEbay.ID);
		serviceConfig.setUserEmail(userEmail);

		serviceConfig.setUserSiteUrl(ebayUser.getStoreURL());
		MyPersistenceManagerFactory.addSourceServiceApp(userEmail,
				serviceConfig);

		resp.sendRedirect("/authorize.jsp");
	}

}
