/**
 * 
 */
package com.googlecode.flickr2twitter.model;

import java.util.Date;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public interface IPhoto extends IItem{
	
	public void setDatePosted(Date datePosted);
	public Date getDatePosted();
	
	public void setDateTaken(Date dateTaken);
	public Date getDateTaken();
	
	public void setUrl(String url);
	public String getUrl();
	
}
