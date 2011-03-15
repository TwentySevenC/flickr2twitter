/*
 * Created on Mar 15, 2011
 */

package com.googlecode.flickr2twitter.services.rest;

import java.util.Arrays;
import java.util.logging.Logger;

import org.restlet.resource.ServerResource;

import com.googlecode.flickr2twitter.org.apache.commons.lang3.StringUtils;
import com.googlecode.flickr2twitter.services.rest.models.ISocialHubEbaySellerResource;
import com.googlecode.flickr2twitter.servlet.EbayConfigServlet;

/**
 * @author Emac Shen (shen.bin.1983@gmail.com)
 */
public class SocialHubEbaySellerResource extends ServerResource
        implements ISocialHubEbaySellerResource
{

    private static final Logger log = Logger.getLogger(SocialHubEbaySellerResource.class.getName());

    /**
     * 
     */
    public SocialHubEbaySellerResource()
    {
        super();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.googlecode.flickr2twitter.services.rest.models.ISocialHubEbaySellerResource#addEbaySellerSourceServiceConfig
     * (java.lang.String)
     */
    @Override
    public void addEbaySellerSourceServiceConfig(String data)
    {
        log.info("adding new eBay seller source->" + data);

        if ( data != null )
        {
            try
            {
                String[] values = StringUtils.split(data, "/");
                log.info("Received data->" + Arrays.asList(values));
                if ( values.length == 2 )
                {
                    String userEmail = values[0];
                    String sellerId = values[1];

                    new EbayConfigServlet().registerNewSellerSourceServiceConfig(userEmail, sellerId, false);
                    
                    log.info("new eBay seller source added->" + data);
                }
                else
                {
                    log.info("Unsupported source config->" + data);
                }
            }
            catch (Exception e)
            {
                log.throwing(SocialHubEbaySellerResource.class.getName(), "addEbaySellerSourceServiceConfig", e);
            }
        }
    }

}
