/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.impl.flickr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.gmail.yuyang226.autoflickr2twitter.client.FlickrException;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.Flickr;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.REST;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.RequestContext;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.auth.Auth;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.auth.Permission;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.photos.Extras;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.photos.Photo;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.photos.PhotoList;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.photos.PhotosInterface;
import com.gmail.yuyang226.autoflickr2twitter.core.GlobalConfiguration;
import com.gmail.yuyang226.autoflickr2twitter.datastore.MyPersistenceManagerFactory;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.UserConfiguration;
import com.gmail.yuyang226.autoflickr2twitter.intf.IDataStoreService;
import com.gmail.yuyang226.autoflickr2twitter.intf.ISourceServiceProvider;
import com.gmail.yuyang226.autoflickr2twitter.model.IItem;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class SourceServiceProviderFlickr implements ISourceServiceProvider<IItem> {
	public static final String ID = "flickr";
    private Flickr f;
    private static final Logger log = Logger.getLogger(SourceServiceProviderFlickr.class.getName());

	/**
	 * 
	 */
	public SourceServiceProviderFlickr() throws ParserConfigurationException, IOException {
		super();
		REST transport = new REST();
        f = new Flickr(
        		GlobalConfiguration.getInstance().getFlickrApiKey(),
        		GlobalConfiguration.getInstance().getFlickrSecret(),
        		transport
        );
        transport.setAllowCache(false);
       
        Flickr.debugRequest = false;
        Flickr.debugStream = false;
	}
    
    private List<IItem> showRecentPhotos(String userId, String token, long interval) throws IOException, SAXException, FlickrException {
    	 RequestContext requestContext = RequestContext.getRequestContext();
         Auth auth = new Auth();
         auth.setPermission(Permission.READ);
         auth.setToken(token);
         requestContext.setAuth(auth);
    	List<IItem> photos = new ArrayList<IItem>();
    	//PeopleInterface pface =  f.getPeopleInterface();
    	PhotosInterface photosFace = f.getPhotosInterface();
    	Set<String> extras = new HashSet<String>(2);
    	extras.add(Extras.DATE_UPLOAD);
    	extras.add(Extras.LAST_UPDATE);
    	extras.add(Extras.GEO);
    	
    	//pface.getPublicPhotos(userId, extras, 10, 1);
    	Date now = Calendar.getInstance(TimeZone.getTimeZone("CST"), Locale.UK).getTime();
    	log.info("Current time: " + now);
    	Calendar past = Calendar.getInstance(TimeZone.getTimeZone("CST"), Locale.UK);
    	long newTime = now.getTime() - interval;
    	past.setTimeInMillis(newTime);
    	PhotoList list = photosFace.recentlyUpdated(past.getTime(), extras, 20, 1); 
		
    	log.info("Trying to find photos uploaded for user " + userId + " after " + past.getTime().toString() + " from " 
    			+ list.getTotal() + " new photos");
    	for (Object obj : list) {
    		if (obj instanceof Photo) {
    			Photo photo = (Photo)obj;
    			log.fine("processing photo: " + photo.getTitle() + ", date uploaded: " + photo.getDatePosted());
    			if (photo.getDatePosted().after(past.getTime())) {
    				log.info(photo.getTitle() + ", URL: " + photo.getUrl() 
    						+ ", date uploaded: " + photo.getDatePosted() + ", GEO: " + photo.getGeoData());
    				photos.add(photo);
    			}
    		}
    	}
    	return photos;
    }

	/* (non-Javadoc)
	 * @see com.gmail.yuyang226.autoflickr2twitter.intf.ISourceServiceProvider#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}

	/* (non-Javadoc)
	 * @see com.gmail.yuyang226.autoflickr2twitter.intf.ISourceServiceProvider#getLatestItems()
	 */
	@Override
	public List<IItem> getLatestItems() throws Exception {
		List<UserConfiguration> results = MyPersistenceManagerFactory.getAllUsers();
		if (results.isEmpty() == false) {
			//we assume that there is only one registered user
			UserConfiguration user = results.get(0);
			return showRecentPhotos(user.getFlickrUserId(), user.getFlickrToken()
					, GlobalConfiguration.getInstance().getInterval());
		}
		return Collections.emptyList();
	}

	/* (non-Javadoc)
	 * @see com.gmail.yuyang226.autoflickr2twitter.intf.ISourceServiceProvider#storeToken(com.gmail.yuyang226.autoflickr2twitter.intf.IDataStoreService)
	 */
	@Override
	public boolean storeToken(IDataStoreService datastore) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
