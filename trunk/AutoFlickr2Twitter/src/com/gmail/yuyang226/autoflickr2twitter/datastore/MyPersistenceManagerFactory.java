/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.gmail.yuyang226.autoflickr2twitter.datastore.model.GlobalServiceConfiguration;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.GlobalSourceApplicationService;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.GlobalTargetApplicationService;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.User;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.UserSourceService;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.UserTargetService;
import com.google.appengine.api.datastore.Email;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 * 
 */
public final class MyPersistenceManagerFactory {
	private static final PersistenceManagerFactory pmfInstance = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");
	private static final Logger log = Logger
			.getLogger(MyPersistenceManagerFactory.class.getName());

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

	public static GlobalSourceApplicationService getGlobalSourceAppService(
			String providerId) {
		PersistenceManagerFactory pmf = MyPersistenceManagerFactory
				.getInstance();
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			Query query = pm.newQuery(GlobalSourceApplicationService.class);
			query.setFilter("providerId == id");
			query.declareParameters("String id");
			List<?> data = (List<?>) query.execute(providerId);
			if (data != null && data.isEmpty() == false)
				return (GlobalSourceApplicationService) data.get(0);
		} catch (Exception e) {
			log.warning(e.toString());
		} finally {
			pm.close();
		}
		return null;
	}

	public static GlobalTargetApplicationService getGlobalTargetAppService(
			String providerId) {
		PersistenceManagerFactory pmf = MyPersistenceManagerFactory
				.getInstance();
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			Query query = pm.newQuery(GlobalTargetApplicationService.class);
			query.setFilter("providerId == id");
			query.declareParameters("String id");
			List<?> data = (List<?>) query.execute(providerId);
			if (data != null && data.isEmpty() == false)
				return (GlobalTargetApplicationService) data.get(0);
		} catch (Exception e) {
			log.warning(e.toString());
		} finally {
			pm.close();
		}
		return null;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public static User createNewUser(String userEmail, String password,
			String screenName) {
		return createNewUser(userEmail, password, screenName, Permission.NORMAL);
	}

	public static UserSourceService addSourceServiceApp(String userEmail,
			UserSourceService srcService) {
		return addSourceServiceApp(getUser(userEmail), srcService);
	}

	public static UserSourceService addSourceServiceApp(User user,
			UserSourceService srcService) {
		if (user == null) {
			throw new IllegalArgumentException("Invalid user: " + user);
		}
		List<UserSourceService> services = getUserSourceServices(user);
		int index = services.indexOf(srcService);
		if (index >= 0) {
			return services.get(index);
		}

		PersistenceManagerFactory pmf = MyPersistenceManagerFactory
				.getInstance();
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			user.addSourceService(srcService);
			pm.makePersistent(user);
			return srcService;
		} finally {
			pm.close();
		}
	}

	public static UserTargetService addTargetServiceApp(String userEmail,
			UserTargetService targetService) {
		return addTargetServiceApp(getUser(userEmail), targetService);
	}

	public static UserTargetService addTargetServiceApp(User user,
			UserTargetService targetService) {
		if (user == null) {
			throw new IllegalArgumentException("Invalid user: " + user);
		}
		List<UserTargetService> services = getUserTargetServices(user);
		int index = services.indexOf(targetService);
		if (index >= 0) {
			return services.get(index);
		}

		PersistenceManagerFactory pmf = MyPersistenceManagerFactory
				.getInstance();
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			user.addTargetService(targetService);
			pm.makePersistent(user);
			return targetService;
		} finally {
			pm.close();
		}
	}

	public static User createNewUser(String userEmail, String password,
			String screenName, Permission permission) {
		User user = getUser(userEmail);
		if (user != null) {
			log.warning("User already exist: " + userEmail);
			return user;
		}
		PersistenceManagerFactory pmf = MyPersistenceManagerFactory
				.getInstance();
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			user = new User(new Email(userEmail), password, screenName);
			user.setPermission(permission.name());
			pm.makePersistent(user);
			return user;
		} finally {
			pm.close();
		}
	}

	public static User getUser(String userEmail) {
		PersistenceManagerFactory pmf = MyPersistenceManagerFactory
				.getInstance();
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			Query query = pm.newQuery(User.class);
			query.setFilter("userId == userEmailAddress");
			query.declareParameters("String userEmailAddress");
			List<?> data = (List<?>) query.execute(userEmail);
			if (data != null && data.isEmpty() == false)
				return (User) data.get(0);
		} finally {
			pm.close();
		}
		return null;
	}
	
	public static User getLoginUser(String userEmail, String password) {
		PersistenceManagerFactory pmf = MyPersistenceManagerFactory
				.getInstance();
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			Query query = pm.newQuery(User.class);
			query.setFilter("userId == userEmailAddress && password == userPassword");
			query.declareParameters("String userEmailAddress, String userPassword");
			List<?> data = (List<?>) query.execute(userEmail, password);
			if (data != null && !data.isEmpty()) {
				User u = (User) data.get(0);
				log.log(Level.INFO, u.getScreenName());
				return u;
			}
		} finally {
			pm.close();
		}
		return null;
	}

	public static List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		PersistenceManagerFactory pmf = MyPersistenceManagerFactory
				.getInstance();
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			Query query = pm.newQuery(User.class);
			List<?> results = (List<?>) query.execute();
			for (Object obj : results) {
				User user = (User) obj;
				users.add(user);
				user.getSourceServices();
				user.getTargetServices();
			}
		} finally {
			pm.close();
		}
		for (User user : users) {
			getUserSourceServices(user);
			getUserTargetServices(user);
		}
		return users;
	}

	public static List<UserSourceService> getUserSourceServices(String userEmail) {
		return getUserSourceServices(getUser(userEmail));
	}

	public static List<UserSourceService> getUserSourceServices(User user) {
		if (user == null) {
			throw new IllegalArgumentException("Invalid user: " + user);
		}
		PersistenceManagerFactory pmf = MyPersistenceManagerFactory
				.getInstance();
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			Query query = pm.newQuery(UserSourceService.class);
			query.setFilter("userEmail == userEmailAddress");
			query.declareParameters("String userEmailAddress");
			List<?> data = (List<?>) query.execute(user.getUserId().getEmail());
			if (data != null) {
				for (Object o : data) {
					if (user.getSourceServices().contains(o) == false) {
						user.addSourceService((UserSourceService) o);
					}
				}
			}
		} finally {
			pm.close();
		}
		return user.getSourceServices();
	}

	public static List<UserTargetService> getUserTargetServices(String userEmail) {
		return getUserTargetServices(getUser(userEmail));
	}

	public static List<UserTargetService> getUserTargetServices(User user) {
		if (user == null) {
			throw new IllegalArgumentException("Invalid user: " + user);
		}
		PersistenceManagerFactory pmf = MyPersistenceManagerFactory
				.getInstance();
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			Query query = pm.newQuery(UserTargetService.class);
			query.setFilter("userEmail == userEmailAddress");
			query.declareParameters("String userEmailAddress");
			List<?> data = (List<?>) query.execute(user.getUserId().getEmail());
			if (data != null) {
				for (Object o : data) {
					if (user.getTargetServices().contains(o) == false) {
						user.addTargetService((UserTargetService) o);
					}
				}
			}
		} finally {
			pm.close();
		}
		return user.getTargetServices();
	}

	public static GlobalServiceConfiguration getGlobalConfiguration() {
		PersistenceManagerFactory pmf = MyPersistenceManagerFactory
				.getInstance();
		PersistenceManager pm = pmf.getPersistenceManager();
		GlobalServiceConfiguration conf = null;
		try {
			Query query = pm.newQuery(GlobalServiceConfiguration.class);
			List<?> results = (List<?>) query.execute();
			if (results.isEmpty() == false) {
				conf = (GlobalServiceConfiguration) results.get(0);
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
