package com.gmail.yuyang226.autoflickr2twitter.impl.flickr;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.gmail.yuyang226.autoflickr2twitter.client.FlickrException;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.Flickr;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.REST;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.auth.Auth;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.auth.AuthInterface;
import com.gmail.yuyang226.autoflickr2twitter.com.aetrion.flickr.auth.Permission;
import com.gmail.yuyang226.autoflickr2twitter.core.GlobalDefaultConfiguration;

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
	private static final Logger log = Logger.getLogger(FlickrAuthTokenFetcher.class.getName());

	public FlickrAuthTokenFetcher() {
		super();
	}

	public static String authrorize() throws ParserConfigurationException, IOException, SAXException, FlickrException {
		Flickr f = new Flickr(
				GlobalDefaultConfiguration.getInstance().getFlickrApiKey(),
				GlobalDefaultConfiguration.getInstance().getFlickrSecret(),
				new REST()
		);
		
		AuthInterface authInterface = f.getAuthInterface();

		String frob = authInterface.getFrob();
		
		URL url = authInterface.buildAuthenticationUrl(Permission.READ, frob);
		log.info("frob: " + frob + ", Token URL: " + url.toExternalForm());
		return frob + ":" + url.toExternalForm();
	}
	
	public static String test(String frob) throws ParserConfigurationException, IOException, SAXException, FlickrException {
		Flickr f = new Flickr(
				GlobalDefaultConfiguration.getInstance().getFlickrApiKey(),
				GlobalDefaultConfiguration.getInstance().getFlickrSecret(),
				new REST()
		);
		AuthInterface authInterface = f.getAuthInterface();
		Auth auth = authInterface.getToken(frob);
		StringBuffer buf = new StringBuffer();
		buf.append("Authentication success\n");
		// This token can be used until the user revokes it.
		buf.append("Token: " + auth.getToken());
		buf.append("\n");
		buf.append("nsid: " + auth.getUser().getId());
		buf.append("\n");
		buf.append("Realname: " + auth.getUser().getRealName());
		buf.append("\n");
		buf.append("Username: " + auth.getUser().getUsername());
		buf.append("\n");
		buf.append("Permission: " + auth.getPermission().getType());
		return buf.toString();
	}

	public static void main(String[] args) {
		try {
			//System.out.println(FlickrAuthTokenFetcher.test("72157625091311678-926a0552054564ea-1041539"));
			System.out.println(FlickrAuthTokenFetcher.authrorize());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
