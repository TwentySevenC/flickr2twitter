/**
 * 
 */
package com.googlecode.flickr2twitter.core;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions.Builder;
import com.google.appengine.api.labs.taskqueue.TaskOptions.Method;
import com.googlecode.flickr2twitter.datastore.MyPersistenceManagerFactory;
import com.googlecode.flickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.googlecode.flickr2twitter.datastore.model.User;
import com.googlecode.flickr2twitter.servlet.ServiceWorkerServlet;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class ServiceRunner {
	private static final Logger log = Logger.getLogger(ServiceRunner.class.getName());
	public static final String TIMEZONE_UTC = "UTC";
	public static final String KEY_USER = "user";
	public static final String KEY_TIMESTAMP = "timestamp";
	public static final String KEY_INTERNVAL = "interval";
	
	/**
	 * 
	 */
	public ServiceRunner() {
		super();
	}

	public static void execute() {
		List<User> users = MyPersistenceManagerFactory.getAllUsers();
		if (users.isEmpty()) {
			log.info("No user configured, skip the execution");
			return;
		}
		
		GlobalServiceConfiguration globalConfig = MyPersistenceManagerFactory.getGlobalConfiguration();
		Date now = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE_UTC)).getTime();
		log.info("Current time: " + now);
		for (User user : users) {
			if (user.getSourceServices() == null || user.getSourceServices().isEmpty()) {
				log.warning("No source services configured for the user: " + user);
				continue;
			} else if (user.getTargetServices() == null || user.getTargetServices().isEmpty()) {
				log.warning("No target services configured for the user: " + user);
				continue;
			}
			Queue queue = QueueFactory.getQueue(ServiceWorkerServlet.QUEUE_NAME_WORKER);
		    queue.add(Builder.url("/tasks/worker")
		    		.param(KEY_USER, user.getUserId().getEmail())
		    		.param(KEY_TIMESTAMP, String.valueOf(now.getTime()))
		    		.param(KEY_INTERNVAL, String.valueOf(globalConfig.getMinUploadTime()))
		    		.method(Method.POST));
		}
    }
	
	public static void main(String[] args) {
		ServiceRunner.execute();
	}

}
