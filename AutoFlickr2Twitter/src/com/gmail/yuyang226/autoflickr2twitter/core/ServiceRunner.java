/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.core;

import java.util.List;
import java.util.logging.Logger;

import twitter4j.GeoLocation;

import com.gmail.yuyang226.autoflickr2twitter.datastore.MyPersistenceManagerFactory;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.UserConfiguration;
import com.gmail.yuyang226.autoflickr2twitter.model.IGeoItem;
import com.gmail.yuyang226.autoflickr2twitter.model.IItem;
import com.gmail.yuyang226.autoflickr2twitter.model.IPhoto;
import com.gmail.yuyang226.autoflickr2twitter.model.IShortUrl;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class ServiceRunner {
	private static final Logger log = Logger.getLogger(ServiceRunner.class.getName());
	
	/**
	 * 
	 */
	public ServiceRunner() {
		super();
	}

	public static void execute() {
		if (MyPersistenceManagerFactory.getAllUsers().isEmpty()) {
			log.info("No user configured, skip the execution");
			return;
		}
		//TODO currently only support one user
		UserConfiguration user = MyPersistenceManagerFactory.getAllUsers().get(0);
        try {
            List<IItem> items = ServiceFactory.getSourceServiceProvider(user.getSourceServiceProviderId()).getLatestItems();
            if (items.size() == 0) {
            	log.info("No new uploaded items found");
            }
            for (IItem item: items) {
            	try {
            		String msg = null;
            		GeoLocation geoLoc = null;
            		if (item instanceof IPhoto) {
            			IPhoto photo = (IPhoto)item;
            			msg = photo.getTitle()+ " ";
            			if (photo instanceof IShortUrl) {
            				msg += ((IShortUrl)photo).getShortUrl();
            			} else {
            				msg += photo.getUrl();
            			}
            			 
            		}
            		if (item instanceof IGeoItem) {
            			if (((IGeoItem)item).getGeoData() != null) {
            				IGeoItem geoItem = (IGeoItem)item;
            				geoLoc = new GeoLocation(geoItem.getGeoData().getLatitude(), 
            						geoItem.getGeoData().getLongitude());
            			}
            		}
            		if (msg != null) {
            			if (user.getTwitterUserId() == null || user.getTwitterUserId().equals("")) {
            				log.warning("Twitter account not specified yet, skip the execution.");
            				return;
            			}
            			TwitterPoster.updateTwitterStatus(user, msg, 
            					geoLoc);
            		}
            	} catch (Exception e) {
            		log.warning(e.toString());
            	}
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	log.throwing(ServiceRunner.class.getName(), "", e);
        }
    }
	
	public static void main(String[] args) {
		ServiceRunner.execute();
	}

}
