/**
 * 
 */
package com.googlecode.flickr2twitter.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.flickr2twitter.core.ServiceRunner;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class ServiceExecutionServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(ServiceExecutionServlet.class);
	/**
	 * 
	 */
	public ServiceExecutionServlet() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {
			ServiceRunner.execute();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
		}
	}
	

}
