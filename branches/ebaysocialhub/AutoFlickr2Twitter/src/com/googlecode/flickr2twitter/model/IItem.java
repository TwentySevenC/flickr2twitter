/**
 * 
 */
package com.googlecode.flickr2twitter.model;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public interface IItem {
	public abstract String getId();

	public abstract void setId(String id);

	public abstract String getTitle();

	public abstract void setTitle(String title);

	public abstract String getDescription();

	public abstract void setDescription(String description);

}