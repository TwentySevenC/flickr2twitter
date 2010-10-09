/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.gmail.yuyang226.autoflickr2twitter.datastore.MyPersistenceManagerFactory;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.User;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.UserSourceService;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.UserTargetService;
import com.gmail.yuyang226.autoflickr2twitter.intf.ISourceServiceProvider;
import com.gmail.yuyang226.autoflickr2twitter.intf.ITargetServiceProvider;
import com.gmail.yuyang226.autoflickr2twitter.model.IItem;

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
		GlobalServiceConfiguration globalConfig = MyPersistenceManagerFactory.getGlobalConfiguration();
		List<User> users = MyPersistenceManagerFactory.getAllUsers();
		if (users.isEmpty()) {
			log.info("No user configured, skip the execution");
			return;
		}
		
		for (User user : users) {
			if (user.getSourceServices() == null || user.getSourceServices().isEmpty()) {
				log.warning("No source services configured for the user: " + user);
				continue;
			} else if (user.getTargetServices() == null || user.getTargetServices().isEmpty()) {
				log.warning("No target services configured for the user: " + user);
				continue;
			}
			log.info("Retrieving latest updates for user: " + user);
			List<IItem> items = new ArrayList<IItem>();
			for (UserSourceService source : user.getSourceServices()) {
				ISourceServiceProvider<IItem> sourceProvider = 
					ServiceFactory.getSourceServiceProvider(source.getSourceServiceProviderId());
				if (sourceProvider == null) {
					log.warning("Invalid source service provider configured: " + source.getSourceServiceProviderId());
				} else {
					try {
						items.addAll(sourceProvider.getLatestItems(globalConfig, source));
					} catch (Exception e) {
						log.throwing(ServiceRunner.class.getName(), "", e);
					}
				}
			}
			if (items.isEmpty()) {
				log.info("No recent updates found for user: " + user);
			} else {
				for (UserTargetService target : user.getTargetServices()) {
					ITargetServiceProvider targetProvider = 
						ServiceFactory.getTargetServiceProvider(target.getTargetServiceProviderId());
					if (targetProvider == null) {
						log.warning("Invalid target service provider configured: " + target.getTargetServiceProviderId());
					} else {
						for (IItem item : items) {
							try {
								targetProvider.postUpdate(globalConfig, target, item);
							} catch (Exception e) {
								log.throwing(ServiceRunner.class.getName(), "", e);
							}
						}
					}
				}
			}
		}
    }
	
	public static void main(String[] args) {
		ServiceRunner.execute();
	}

}
