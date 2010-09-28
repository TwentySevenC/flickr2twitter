package com.gmail.yuyang226.autoflickr2twitter.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.Flickr;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.FlickrException;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.REST;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.RequestContext;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.auth.Auth;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.auth.AuthInterface;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.auth.Permission;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.util.IOUtilities;

/**
 * Demonstrates the authentication-process.<p>
 * 
 * If you registered API keys, you find them with the shared secret at your
 * <a href="http://www.flickr.com/services/api/registered_keys.gne">list of API keys</a>
 * 
 * @author mago
 * @version $Id: AuthExample.java,v 1.5 2008/07/05 22:19:48 x-mago Exp $
 */
public class FlickrAuthTokenFetcher {
	Flickr f;
	RequestContext requestContext;
	String frob = "";
	String token = "";
	private static final Logger log = Logger.getLogger(FlickrAuthTokenFetcher.class.getName());

	public FlickrAuthTokenFetcher() {
		super();
	}

	public static String authrorize() throws ParserConfigurationException, IOException, SAXException, FlickrException {
		Flickr f = new Flickr(
				GlobalConfiguration.getInstance().getFlickrApiKey(),
				GlobalConfiguration.getInstance().getFlickrSecret(),
				new REST()
		);
		
		AuthInterface authInterface = f.getAuthInterface();

		String frob = authInterface.getFrob();
		System.out.println("frob: " + frob);
		URL url = authInterface.buildAuthenticationUrl(Permission.READ, frob);
		System.out.println("Press return after you granted access at this URL:");
		System.out.println(url.toExternalForm());
		return frob;
	}
	
	public static void test(String frob) throws ParserConfigurationException, IOException, SAXException, FlickrException {
		Flickr f = new Flickr(
				GlobalConfiguration.getInstance().getFlickrApiKey(),
				GlobalConfiguration.getInstance().getFlickrSecret(),
				new REST()
		);
		AuthInterface authInterface = f.getAuthInterface();
		Auth auth = authInterface.getToken(frob);
		log.info("Authentication success");
		// This token can be used until the user revokes it.
		log.info("Token: " + auth.getToken());
		log.info("nsid: " + auth.getUser().getId());
		log.info("Realname: " + auth.getUser().getRealName());
		log.info("Username: " + auth.getUser().getUsername());
		log.info("Permission: " + auth.getPermission().getType());
	}

	public static void main(String[] args) {
		try {
			FlickrAuthTokenFetcher t = new FlickrAuthTokenFetcher();
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
