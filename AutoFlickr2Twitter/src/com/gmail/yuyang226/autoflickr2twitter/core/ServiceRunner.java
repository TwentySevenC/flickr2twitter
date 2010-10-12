/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.gmail.yuyang226.autoflickr2twitter.datastore.MyPersistenceManagerFactory;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.User;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.UserSourceServiceConfig;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.UserTargetServiceConfig;
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
			for (UserSourceServiceConfig source : user.getSourceServices()) {
				ISourceServiceProvider<IItem> sourceProvider = 
					ServiceFactory.getSourceServiceProvider(source.getServiceProviderId());
				if (sourceProvider == null) {
					log.warning("Invalid source service provider configured: " + source.getServiceProviderId());
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
				for (UserTargetServiceConfig target : user.getTargetServices()) {
					ITargetServiceProvider targetProvider = 
						ServiceFactory.getTargetServiceProvider(target.getServiceProviderId());
					GlobalTargetApplicationService globalAppConfig = 
						MyPersistenceManagerFactory.getGlobalTargetAppService(target.getServiceProviderId());
					if (targetProvider == null || globalAppConfig == null) {
						log.warning("Invalid target service provider configured: " + target.getServiceProviderId());
					} else {
						try {
							targetProvider.postUpdate(globalAppConfig, target, items);
						} catch (Exception e) {
							log.throwing(ServiceRunner.class.getName(), "", e);
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
