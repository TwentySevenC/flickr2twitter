/**
 * 
 */
package com.googlecode.flickr2twitter.com.aetrion.flickr.auth;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class OAuth extends Auth {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tokenSecret;

	/**
	 * 
	 */
	public OAuth() {
		super();
	}

	/**
	 * @param tokenSecret
	 */
	public OAuth(String oauthToken, String tokenSecret) {
		super();
		super.setToken(oauthToken);
		this.tokenSecret = tokenSecret;
	}



	/**
	 * @return the tokenSecret
	 */
	public String getTokenSecret() {
		return tokenSecret;
	}

	/**
	 * @param tokenSecret the tokenSecret to set
	 */
	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}
	
	

}
