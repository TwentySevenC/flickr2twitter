/**
 * 
 */
package com.gmail.yuyang226.autoflickr2twitter.core;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A wrapper class for logging.
 * @author Yang Yu(yuyang226@gmail.com)
 *
 */
public final class MyLogger extends Logger{ 
	private static Hashtable<String,MyLogger> loggers = new Hashtable<String,MyLogger>();
	
	public static MyLogger getInstance(Class< ? > clazz) {
		return getInstance(clazz.getName());
	}
	
	public static MyLogger getInstance(String clazz) {
		// We want 1 logger per subsystem
		String subsystem = "";
		if (clazz != null) {
			if (subsystem.length() == 0) {
				subsystem = clazz;
			}
		} else {
			subsystem = "";
		}

		return getLogger(subsystem);
	}
	
	private MyLogger(String loggerName) {
		super(loggerName, null);
	}
	
	/**
	 * If the tracing is enabled, then the logging level will be setup accordingly.
	 * @param clazz
	 * @return An instance of <code>LogManager</code>
	 */
	public static synchronized MyLogger getLogger(String clazz) {
		//java.util.logging.LogManager manager = java.util.logging.LogManager.getLogManager();
		Logger result = Logger.getLogger(clazz);//manager.getLogger(clazz);
		if (result instanceof MyLogger) {
		    return (MyLogger)result;
		}
		else if (result != null) {
		//there is an existing logger instance
			if (loggers.keySet().contains(clazz)) {
				return loggers.get(clazz);
			}
			MyLogger logger = new MyLogger(clazz);
			logger.setLevel(result.getLevel());
			logger.setFilter(result.getFilter());
			logger.setParent(result.getParent());
			logger.setUseParentHandlers(logger.getUseParentHandlers());
			loggers.put(clazz, logger);
			return logger;
		} else {
		//can not find a logger, so let's create one.
			return new MyLogger(clazz);
		}
		
		//manager.addLogger(result);
		//return (MyLogger)manager.getLogger(clazz);
	}

	/**
	 * @param clazz The <code>Class</code> instance that is used for constructing <code>Logger</code> instance.
	 * @return
	 */
	public static MyLogger getLogger(Class<?> clazz) {
		MyLogger logger = getLogger(clazz.getName());
		return logger;
	}
	
	/**
	 * The logger name is automatically obtained.
	 * @return An instance of <code>LogManager</code>
	 */
	public static MyLogger getLogger()
	{
		StackTraceElement[] elements = new Throwable().getStackTrace();
		return getLogger(elements[1].getClassName());
	}
	
	
	//////////////////////////////////////////
	//Info Logging
	//////////////////////////////////////////
	/**
	 * @param msgs The messages
	 * @see java.util.logging.Level#INFO
	 */
	public void info(Object... msgs) {
		StackTraceElement[] elements = new Throwable().getStackTrace();
    	logp(Level.INFO, elements[1].getClassName(), elements[1].getMethodName(), toString(msgs));
	}
	/**
	 * @return <code>True</code> if the <code>INFO</code> logging level is enabled, or <code>False</code> otherwise.
	 * @see java.util.logging.Level#INFO
	 */
	public boolean isInfoEnabled()
	{
		return isLoggable(Level.INFO);
	}

	//////////////////////////////////////////
	//Warning Logging
	//////////////////////////////////////////
	/**
	 * @param message The warning message
	 * @see java.util.logging.Level#WARNING
	 */
	public void warning(Object... messages)
    {
		StackTraceElement[] elements = new Throwable().getStackTrace();
    	logp(Level.WARNING, elements[1].getClassName(), elements[1].getMethodName(), 
    			toString(messages));
    }
	/**
	 * @param messages The warning messages
	 * @see java.util.logging.Level#WARNING
	 */
	public void warning(Throwable cause, Object... messages)
    {
		StackTraceElement[] elements = new Throwable().getStackTrace();
    	logp(Level.WARNING, elements[1].getClassName(), elements[1].getMethodName(), 
    			toString(messages), cause);
    }
    /**
     * <p>This method will work if the <code>WARNING</code> logging level is enabled.</p>
     * @param message The message
     * @param cause The cause of the problem
     * @see java.util.logging.Level#WARNING
     */
    public void warning(Object message, Throwable cause)
    {
    	StackTraceElement[] elements = new Throwable().getStackTrace();
    	logp(Level.WARNING, elements[1].getClassName(), elements[1].getMethodName(), String.valueOf(message), cause);
    }
    
    //////////////////////////////////////////
	//Error Logging
	//////////////////////////////////////////
	/**
	 * @param msg The error message
	 * @see java.util.logging.Level#SEVERE
	 */
	public void error(String msg) {
		StackTraceElement[] elements = new Throwable().getStackTrace();
        logp(Level.SEVERE, elements[1].getClassName(), elements[1].getMethodName(), String.valueOf(msg));
	}
	/**
	 * @param message The error message
	 * @param cause The cause of the error
	 * @see java.util.logging.Level#SEVERE
	 */
	public void error(Object message, Throwable cause)
    {
		StackTraceElement[] elements = new Throwable().getStackTrace();
        logp(Level.SEVERE, elements[1].getClassName(), elements[1].getMethodName(), String.valueOf(message), cause);
    }
	/**
	 * @param cause The cause of the error
	 * @see java.util.logging.Level#SEVERE
	 */
	public void error(Throwable cause)
    {
		StackTraceElement[] elements = new Throwable().getStackTrace();
        logp(Level.SEVERE, elements[1].getClassName(), elements[1].getMethodName(),cause.getLocalizedMessage(), cause);
    }
	/**
	 * @param thrown The instance of <code>Throwable</code>
	 * @see java.util.logging.Level#SEVERE
	 */
	public void throwing(Throwable thrown) {
		StackTraceElement[] elements = new Throwable().getStackTrace();
		throwing(elements[1].getClassName(), elements[1].getMethodName(), thrown);
	}
    
    //////////////////////////////////////////
	//Debug Logging
	//////////////////////////////////////////
    /**
     * @param msg The debug message
     * @see java.util.logging.Level#FINE
     */
    public void debug(Object... msgs)
    {
    	StackTraceElement[] elements = new Throwable().getStackTrace();
        logp(Level.FINE, elements[1].getClassName(), elements[1].getMethodName(), toString(msgs));
    }
    
	//////////////////////////////////////////
	//Logging Entry/Exit
	//////////////////////////////////////////
	/**
	 * @param params The multiple parameters that passed in
	 * @see java.util.logging.Level#FINER
	 */
	public void entering(Object... params)
	{
		StackTraceElement[] elements = new Throwable().getStackTrace();
		if (params != null)
			entering(elements[1].getClassName(), elements[1].getMethodName(), params);
		else 
			entering(elements[1].getClassName(), elements[1].getMethodName());
	}
	
	/**
	 * <p>Suitable for logging methods with <code>void</code> return type.</p>
	 * @see java.util.logging.Level#FINER
	 */
	public void exiting()
	{
		StackTraceElement[] elements = new Throwable().getStackTrace();
		exiting(elements[1].getClassName(), elements[1].getMethodName());
	}

	/**
	 * @param result The result of the method call
	 * @see java.util.logging.Level#FINER
	 */
	public void exiting(Object result)
	{
		StackTraceElement[] elements = new Throwable().getStackTrace();
		exiting(elements[1].getClassName(), elements[1].getMethodName(), result);
	}
	
	/**
	 * <p>
	 * </p>
	 * 
	 * @param messages
	 * @return A String to represent the message
	 */
	public static String toString(Object... messages)
			throws IllegalArgumentException {
		final StringBuffer buf = new StringBuffer();
		if (messages == null || messages.length == 0) {
			return buf.toString();
		}
		for (Object msg : messages) {
			buf.append(String.valueOf(msg));
		}
		return buf.toString();
	}
}
