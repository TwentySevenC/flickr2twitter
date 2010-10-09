/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.datastore;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.gmail.yuyang226.autoflickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.User;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public final class MyPersistenceManagerFactory {
	private static final PersistenceManagerFactory pmfInstance =
		JDOHelper.getPersistenceManagerFactory("transactions-optional");

	public static enum Permission {
		ADMIN, NORMAL
	}
	
	/**
	 * 
	 */
	private MyPersistenceManagerFactory() {
		super();
	}

	public static PersistenceManagerFactory getInstance() {
		return pmfInstance;
	}


	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public static List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		PersistenceManagerFactory pmf = MyPersistenceManagerFactory.getInstance();
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			Query query = pm.newQuery(User.class);
			List<User> results = (List<User>)query.execute();
			users.addAll(results);
		} finally {
			pm.close();
		}
		return users;
	}
	
	public static GlobalServiceConfiguration getGlobalConfiguration() {
		PersistenceManagerFactory pmf = MyPersistenceManagerFactory.getInstance();
		PersistenceManager pm = pmf.getPersistenceManager();
		GlobalServiceConfiguration conf = null;
		try {
			Query query = pm.newQuery(GlobalServiceConfiguration.class);
			List<?> results = (List<?>)query.execute();
			if (results.isEmpty() == false) {
				conf = (GlobalServiceConfiguration)results.get(0);
			} else {
				conf = new GlobalServiceConfiguration();
				conf.setKey("1");
				pm.makePersistent(conf);
			}
		} finally {
			pm.close();
		}
		return conf;
	}

}
