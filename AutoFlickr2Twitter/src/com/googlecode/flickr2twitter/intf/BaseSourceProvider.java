package com.googlecode.flickr2twitter.intf;

import java.util.Calendar;
import java.util.TimeZone;

import com.googlecode.flickr2twitter.core.ServiceRunner;
import com.googlecode.flickr2twitter.datastore.model.GlobalServiceConfiguration;

public abstract class BaseSourceProvider<T> implements ISourceServiceProvider<T>{

	protected Calendar getFromTime(GlobalServiceConfiguration globalConfig, long to) {
		Calendar past = Calendar.getInstance(TimeZone.getTimeZone(ServiceRunner.TIMEZONE_UTC));
		long newTime = to - globalConfig.getMinUploadTime();
		past.setTimeInMillis(newTime);
		
		return past;
	}
	
}