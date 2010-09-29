/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.impl.flickr;

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
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.people.PeopleInterface;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.photos.Extras;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.photos.Photo;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.photos.PhotoList;
import com.gmail.yuyang226.autoflickr2twitter.core.GlobalConfiguration;
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
    //RequestContext requestContext;
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
        
        /*requestContext = RequestContext.getRequestContext();
        Auth auth = new Auth();
        auth.setPermission(Permission.READ);
        auth.setToken(GlobalConfiguration.getInstance().getFlickrToken());
        requestContext.setAuth(auth);*/
        Flickr.debugRequest = false;
        Flickr.debugStream = false;
	}
    
    private List<IItem> showRecentPhotos(String userId, long interval) throws IOException, SAXException, FlickrException {
    	List<IItem> photos = new ArrayList<IItem>();
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
		return showRecentPhotos(GlobalConfiguration.getInstance().getFlickrUserId()
				, GlobalConfiguration.getInstance().getInterval());
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
