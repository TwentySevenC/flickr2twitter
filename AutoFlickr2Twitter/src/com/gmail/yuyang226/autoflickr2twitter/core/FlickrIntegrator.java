package com.gmail.yuyang226.autoflickr2twitter.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.Flickr;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.FlickrException;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.REST;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.RequestContext;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.auth.Auth;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.auth.Permission;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.people.PeopleInterface;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.photos.Extras;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.photos.Photo;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.photos.PhotoList;

/**
 * Demonstration of howto use the ActivityInterface.
 *
 * @author mago
 * @version $Id: ActivityExample.java,v 1.3 2008/07/05 22:19:48 x-mago Exp $
 */
public class FlickrIntegrator {
    static String apiKey;
    static String sharedSecret;
    Flickr f;
    REST rest;
    RequestContext requestContext;
    private static final Logger log = Logger.getLogger(FlickrIntegrator.class.getName());
    public static final String FLICKR_SHORT_URL_PREFIX = "http://flic.kr/p/";


    public FlickrIntegrator()
      throws ParserConfigurationException, IOException {
    	REST transport = new REST();
        f = new Flickr(
        		GlobalConfiguration.getInstance().getFlickrApiKey(),
        		GlobalConfiguration.getInstance().getFlickrSecret(),
        		transport
        );
        
        requestContext = RequestContext.getRequestContext();
        Auth auth = new Auth();
        auth.setPermission(Permission.READ);
        auth.setToken(GlobalConfiguration.getInstance().getFlickrToken());
        requestContext.setAuth(auth);
        Flickr.debugRequest = false;
        Flickr.debugStream = false;
    }
    
    public String getShortUrl(Photo photo) {
    	String url = photo.getUrl();
    	String id = photo.getId();
    	if (id == null || id.length() == 0) {
    		int index = url.lastIndexOf("/");
    		if (index > 0) {
    			id = url.substring(index + 1, url.length()).trim();
    		} else {
    			return url;
    		}
    	}
    	String suffix = FlickrBaseEncoder.encode(Long.parseLong(id));
		url = FLICKR_SHORT_URL_PREFIX + suffix;
    	return url;
    }
    
    public List<Photo> showRecentPhotos(String userId, long interval) throws IOException, SAXException, FlickrException {
    	List<Photo> photos = new ArrayList<Photo>();
    	PeopleInterface pface =  f.getPeopleInterface();
    	Set<String> extras = new HashSet<String>(2);
    	extras.add(Extras.DATE_UPLOAD);
    	extras.add(Extras.LAST_UPDATE);
    	extras.add(Extras.GEO);
    	log.info("Get latest uploads for user: " + userId);
    	PhotoList list = pface.getPublicPhotos(userId, extras, 10, 1);//pface.search(params, 10, 1);
    	Date now = Calendar.getInstance(TimeZone.getTimeZone("CST"), Locale.UK).getTime();
    	log.info("Current time: " + now);
    	Calendar past = Calendar.getInstance(TimeZone.getTimeZone("CST"), Locale.UK);
    	long newTime = now.getTime() - interval;
    	past.setTimeInMillis(newTime);
		
		log.info("Trying to find photos uploaded after " + past.getTime().toString());
    	for (Object obj : list) {
    		if (obj instanceof Photo) {
    			Photo photo = (Photo)obj;
    			log.fine("processing photo: " + photo.getTitle() + ", date uploaded: " + photo.getDatePosted());
    			if (photo.getDatePosted().after(past.getTime())) {
    				log.info(photo.getTitle() + ", short URL: " + getShortUrl(photo) 
    						+ ", date uploaded: " + photo.getDatePosted() + ", GEO: " + photo.getGeoData());
    				photos.add(photo);
    			}
    		}
    	}
    	return photos;
    }

    public static void main(String[] args) {
        try {
            FlickrIntegrator t = new FlickrIntegrator();
            List<Photo> photos = t.showRecentPhotos(GlobalConfiguration.getInstance().getFlickrUserId(), 
            		GlobalConfiguration.getInstance().getInterval());
            if (photos.size() == 0) {
            	log.info("No new uploaded images/videos found");
            }
            for (Photo photo: photos) {
            	try {
					TwitterPoster.updateTwitterStatus(photo.getTitle() + " " + t.getShortUrl(photo));
				} catch (Exception e) {
					log.severe(e.toString());
				}
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	log.throwing(FlickrIntegrator.class.getName(), "", e);
        }
    }

}
