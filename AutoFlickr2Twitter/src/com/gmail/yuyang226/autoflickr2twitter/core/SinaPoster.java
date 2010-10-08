package com.gmail.yuyang226.autoflickr2twitter.core;

import twitter4j.GeoLocation;

import com.gmail.yuyang226.autoflickr2twitter.datastore.model.UserConfiguration;
import com.gmail.yuyang226.autoflickr2twitter.sina.weibo4j.Weibo;

/**
 * @author Qiang Zhang
 *
 */
public final class SinaPoster {

	public static void postMessage(UserConfiguration user, String msg,
			GeoLocation loc) throws Exception {

		//api key and secret
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret",
				Weibo.CONSUMER_SECRET);

		Weibo weibo = new Weibo();
		
		//This is my own sina account, use this will post thing to my sina weibo
		weibo.setToken("c9e5472c8723cc478a6332ce8b321008",
				"5c206226f6cd6d87c75c1c60c601d27b");

		if (loc != null) {
			weibo.updateStatus(msg, loc.getLatitude(), loc.getLongitude());
		} else {
			weibo.updateStatus(msg);
		}
	}
}
