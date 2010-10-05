/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.datastore;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.gmail.yuyang226.autoflickr2twitter.datastore.model.UserConfiguration;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public final class MyPersistenceManagerFactory {
	private static final PersistenceManagerFactory pmfInstance =
		JDOHelper.getPersistenceManagerFactory("transactions-optional");

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

	public static List<UserConfiguration> getAllUsers() {
		List<UserConfiguration> users = new ArrayList<UserConfiguration>();
		PersistenceManagerFactory pmf = MyPersistenceManagerFactory.getInstance();
		Query query = pmf.getPersistenceManager().newQuery(UserConfiguration.class);
		List<UserConfiguration> results = (List<UserConfiguration>)query.execute();
		users.addAll(results);
		return users;
	}

}
