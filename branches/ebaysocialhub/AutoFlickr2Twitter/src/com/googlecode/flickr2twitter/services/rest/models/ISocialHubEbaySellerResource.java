/*
 * Created on Mar 15, 2011
 */

package com.googlecode.flickr2twitter.services.rest.models;

import org.restlet.resource.Post;

/**
 * @author Emac Shen (shen.bin.1983@gmail.com)
 */
public interface ISocialHubEbaySellerResource
{

    /**
     * Registers an eBay seller source.
     * 
     * @param data format: {userEmail}/{sellerId}
     */
    @Post
    public void addEbaySellerSourceServiceConfig(String data);

}
