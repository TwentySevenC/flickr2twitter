/**
 * 
 */
package com.googlecode.flickr2twitter.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public final class ExceptionUtils {

	/**
	 * 
	 */
	private ExceptionUtils() {
		super();
	}

	public static String converToString(Throwable t) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

}
