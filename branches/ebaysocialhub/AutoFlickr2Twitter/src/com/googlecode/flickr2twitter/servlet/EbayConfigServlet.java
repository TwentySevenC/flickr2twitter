/*
 * Created on Feb 19, 2011
 */

package com.googlecode.flickr2twitter.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.User;
import com.googlecode.flickr2twitter.datastore.model.UserSourceServiceConfig;
import com.googlecode.flickr2twitter.impl.ebay.SourceServiceProviderEbay;

/**
 * @author Emac Shen (shen.bin.1983@gmail.com)
 */
public class EbayConfigServlet extends HttpServlet
{

    private static final long  serialVersionUID = 1L;

    public static final String PARA_SELLER_ID   = "seller_id";

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest ,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        User user = (User) req.getSession().getAttribute(UserAccountServlet.PARA_SESSION_USER);

        if ( user == null )
        {
            req.getSession().setAttribute("message", "Please Login first!");
            resp.sendRedirect("/index.jsp");
            return;
        }

        String userEmail = user.getUserId().getEmail();
        String sellerId = req.getParameter(PARA_SELLER_ID);

        // TODO#EMAC.P! check seller's identity
        
        UserSourceServiceConfig serviceConfig = new UserSourceServiceConfig();
        // TODO#EMAC.P1 store seller id in user id temporarily
        serviceConfig.setServiceUserId(sellerId);
        // TODO#EMAC.P? is it possible to get seller name?
        serviceConfig.setServiceUserName(sellerId);
        serviceConfig.setServiceProviderId(SourceServiceProviderEbay.ID);
        serviceConfig.setUserEmail(userEmail);
        // TODO#EMAC.P? is it possible to get seller's url?
        serviceConfig.setUserSiteUrl("");
        MyPersistenceManagerFactory.addSourceServiceApp(userEmail, serviceConfig);

        resp.sendRedirect("/authorize.jsp");
    }

}
