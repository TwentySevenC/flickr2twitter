/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import com.gmail.yuyang226.autoflickr2twitter.datastore.MyPersistenceManagerFactory;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.GlobalSourceApplicationService;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.gmail.yuyang226.autoflickr2twitter.impl.flickr.SourceServiceProviderFlickr;
import com.gmail.yuyang226.autoflickr2twitter.impl.twitter.TargetServiceProviderTwitter;
import com.gmail.yuyang226.autoflickr2twitter.intf.ISourceServiceProvider;
import com.gmail.yuyang226.autoflickr2twitter.intf.ITargetServiceProvider;
import com.gmail.yuyang226.autoflickr2twitter.model.IItem;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class ServiceFactory {
	private static final Logger log = Logger.getLogger(ServiceFactory.class.getName());
	private static final Map<String, ISourceServiceProvider<IItem>> SOURCE_PROVIDERS;
	private static final Map<String, ITargetServiceProvider> TARGET_PROVIDERS;
	
	static {
		Map<String, ISourceServiceProvider<IItem>> data = new HashMap<String, ISourceServiceProvider<IItem>>(2);
		try {
			data.put(SourceServiceProviderFlickr.ID, new SourceServiceProviderFlickr());
			if (MyPersistenceManagerFactory.getGlobalSourceAppService(SourceServiceProviderFlickr.ID) == null) {
				PersistenceManagerFactory pmf = MyPersistenceManagerFactory.getInstance();
				PersistenceManager pm = pmf.getPersistenceManager();
				try {
					GlobalSourceApplicationService srcApp = new GlobalSourceApplicationService();
					srcApp.setSourceProviderId(SourceServiceProviderFlickr.ID);
					pm.makePersistent(srcApp);
				} catch (Exception e) {
					log.throwing(ServiceFactory.class.getName(), "<init_db>", e);
				} finally {
					pm.close();
				}
			}
		} catch (Exception e) {
			log.throwing(ServiceFactory.class.getName(), "<init_source>", e);
		}
		SOURCE_PROVIDERS = Collections.unmodifiableMap(data);
		
		Map<String, ITargetServiceProvider> targetData = new HashMap<String, ITargetServiceProvider>(5);
		try {
			targetData.put(TargetServiceProviderTwitter.ID, new TargetServiceProviderTwitter());
			if (MyPersistenceManagerFactory.getGlobalTargetAppService(TargetServiceProviderTwitter.ID) == null) {
				PersistenceManagerFactory pmf = MyPersistenceManagerFactory.getInstance();
				PersistenceManager pm = pmf.getPersistenceManager();
				try {
					GlobalTargetApplicationService targetApp = new GlobalTargetApplicationService();
					pm.makePersistent(targetApp);
				} catch (Exception e) {
					log.throwing(ServiceFactory.class.getName(), "<init_db>", e);
				} finally {
					pm.close();
				}
			}
		} catch (Exception e) {
			log.throwing(ServiceFactory.class.getName(), "<init_target>", e);
		}
		TARGET_PROVIDERS = Collections.unmodifiableMap(targetData);
	}

	/**
	 * 
	 */
	public ServiceFactory() {
		super();
	}
	
	public static ISourceServiceProvider<IItem> getSourceServiceProvider(String sourceServiceProviderId) {
		return SOURCE_PROVIDERS.get(sourceServiceProviderId);
	}
	
	public static ITargetServiceProvider getTargetServiceProvider(String targetServiceProviderId) {
		return TARGET_PROVIDERS.get(targetServiceProviderId);
	}

}
