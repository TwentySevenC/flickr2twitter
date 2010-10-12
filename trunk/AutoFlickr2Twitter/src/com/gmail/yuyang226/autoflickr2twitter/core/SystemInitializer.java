/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.core;

import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.gmail.yuyang226.autoflickr2twitter.datastore.MyPersistenceManagerFactory;
import com.gmail.yuyang226.autoflickr2twitter.datastore.MyPersistenceManagerFactory.Permission;
import com.gmail.yuyang226.autoflickr2twitter.datastore.model.User;
import com.gmail.yuyang226.autoflickr2twitter.servlet.OAuthServlet;

/**
 * @author Meng Zang (DeepNightTwo@gmail.com)
 * 
 */
public class SystemInitializer implements ServletContextListener {

	private static final Logger log = Logger.getLogger(OAuthServlet.class
			.getName());

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		log.info("Initlizing Twitter the World System.....");

		Properties props = GlobalDefaultConfiguration.getInstance()
				.getProperties();
		String adminEmail = props
				.getProperty(GlobalDefaultConfiguration.KEY_ADMIN_EMAIL);
		User admin = MyPersistenceManagerFactory.getUser(adminEmail);
		if (admin == null) {
			log.info("Admin account not found. Creating admin account....");

			String adminDisplayname = props
					.getProperty(GlobalDefaultConfiguration.KEY_ADMIN_DISPLAY_NAME);
			String adminPassword = props
					.getProperty(GlobalDefaultConfiguration.KEY_ADMIN_PASSWORD);
			MyPersistenceManagerFactory.createNewUser(adminEmail,
					adminDisplayname, adminPassword, Permission.ADMIN);
			log.info("Admin account created.");
		} else{
			log.info("Admin account already created.");
		}

		log.info("Initlizing Twitter the World System finished.....");

	}

}
