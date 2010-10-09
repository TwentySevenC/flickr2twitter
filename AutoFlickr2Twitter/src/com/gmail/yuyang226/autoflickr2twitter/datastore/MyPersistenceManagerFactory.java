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
			List<?> results = (List<?>)query.execute();
			for (Object obj : results) {
				User user = (User)obj;
				users.add(user);
				//calling the method would also retrieve the children data
				user.getSourceServices();
				user.getTargetServices();
				/*query = pm.newQuery(UserSourceService.class);
			    query.setFilter("userEmail == userEmailAddress");
			    query.declareParameters("String userEmailAddress");
			    List<?> data = (List<?>)query.execute(user.getUserId().getEmail());
			    if (data != null) {
			    	for (Object o : data) {
			    		if(user.getSourceServices().contains(o) == false) {
			    			user.addSourceService((UserSourceService)o);
			    		}
			    	}
			    }
			    
			    query = pm.newQuery(UserTargetService.class);
			    query.setFilter("userEmail == userEmailAddress");
			    query.declareParameters("String userEmailAddress");
			    data = (List<?>)query.execute(user.getUserId().getEmail());
			    if (data != null) {
			    	for (Object o : data) {
			    		if(user.getTargetServices().contains(o) == false) {
			    			user.addTargetService((UserTargetService)o);
			    		}
			    	}
			    }*/
			}
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
