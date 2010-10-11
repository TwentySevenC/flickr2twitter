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
import com.gmail.yuyang226.autoflickr2twitter.impl.flickr.SourceServiceProviderFlickr;
import com.gmail.yuyang226.autoflickr2twitter.impl.sina.TargetServiceProviderSina;
import com.gmail.yuyang226.autoflickr2twitter.impl.twitter.TargetServiceProviderTwitter;
import com.gmail.yuyang226.autoflickr2twitter.intf.IServiceProvider;
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
	
	private static final Class<?>[] PROVIDER_CLASSES = 
	{SourceServiceProviderFlickr.class, TargetServiceProviderTwitter.class, TargetServiceProviderSina.class}; 
	
	static {
		Map<String, ISourceServiceProvider<IItem>> sourceData = new HashMap<String, ISourceServiceProvider<IItem>>(2);
		Map<String, ITargetServiceProvider> targetData = new HashMap<String, ITargetServiceProvider>(5);
		
		for (Class<?> _class : PROVIDER_CLASSES) {
			PersistenceManagerFactory pmf = MyPersistenceManagerFactory.getInstance();
			PersistenceManager pm = pmf.getPersistenceManager();
			try {
				IServiceProvider provider = (IServiceProvider)_class.newInstance();
				
				if (provider instanceof ISourceServiceProvider) {
					ISourceServiceProvider<IItem> srcProvider = (ISourceServiceProvider<IItem>)provider;
					sourceData.put(provider.getId(), srcProvider);
					if (MyPersistenceManagerFactory.getGlobalSourceAppService(srcProvider.getId()) == null) {
						pm.makePersistent(srcProvider.createDefaultGlobalApplicationConfig());
					}
				} else if (provider instanceof ITargetServiceProvider) {
					ITargetServiceProvider targetProvider = (ITargetServiceProvider)provider;
					targetData.put(provider.getId(), targetProvider);
					if (MyPersistenceManagerFactory.getGlobalTargetAppService(targetProvider.getId()) == null) {
						pm.makePersistent(targetProvider.createDefaultGlobalApplicationConfig());
					}
				} else {
					log.warning("Unsupported provider ->" + _class);
				}
				
			} catch (Exception e) {
				log.throwing(ServiceFactory.class.getName(), "<init>", e);
			} finally {
				pm.close();
			}
		}
		
		SOURCE_PROVIDERS = Collections.unmodifiableMap(sourceData);
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
