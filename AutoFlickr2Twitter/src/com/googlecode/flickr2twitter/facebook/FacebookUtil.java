package com.googlecode.flickr2twitter.facebook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class FacebookUtil {

	private static final Logger log = Logger.getLogger(FacebookUtil.class
			.getName());

	public static final String APP_ID = "199812620030608";

	public static final String APP_SECRET_KEY = "e520dcd104dd009edfc8d4432fc3f60b";

	public static final String APP_SECRET = "a1ceccb82ffc5702e00b608c4b733620";

	public static final String AUTH_URL = "https://www.facebook.com/dialog/oauth?client_id="
			+ APP_ID + "&redirect_uri={0}&&scope=status_update,publish_stream";

	public static final String TOKEN_URL = "https://graph.facebook.com/oauth/access_token?"
			+ "client_id="
			+ APP_ID
			+ "&redirect_uri={0}&"
			+ "client_secret="
			+ APP_SECRET + "&" + "code={1}";

	public static final String TOKEN_PARAM = "access_token";

	public static String getToken(String code) throws Exception {
		HttpClient client = new HttpClient();
		GetMethod getMethod = new GetMethod(
				"https://graph.facebook.com/oauth/access_token");
		NameValuePair[] params = new NameValuePair[4];
		params[0] = new NameValuePair("client_id", APP_ID);
		params[1] = new NameValuePair("redirect_uri",
				"http://www.facebook.com/connect/login_success.html");
		params[2] = new NameValuePair("client_secret", APP_SECRET_KEY);
		params[3] = new NameValuePair("code", code);

		getMethod.setQueryString(params);

		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		int statusCode = client.executeMethod(getMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.err.println("Method failed: " + getMethod.getStatusLine());
		}
		byte[] responseBody = getMethod.getResponseBody();
		String retStr = new String(responseBody);
		getMethod.releaseConnection();

		log.info("Return String from Token URL request:\r\n" + responseBody);

		String tokenPara = "access_token=";
		if (retStr.startsWith(tokenPara) == false) {
			throw new Exception("Fail to retrieve token! Error content:\r\n"
					+ retStr);
		}

		String token = retStr.substring(tokenPara.length());
		log.info("Get user token:\t" + responseBody);

		return token;
	}

	public static String postMessage(String message, String token)
			throws HttpException, IOException {
		HttpClient client = new HttpClient();
		GetMethod getMethod = new GetMethod(
				"https://graph.facebook.com/oauth/access_token");
		NameValuePair[] params = new NameValuePair[3];
		params[0] = new NameValuePair("status", message);
		params[1] = new NameValuePair("access_token", token);
		params[2] = new NameValuePair("format", "json");

		getMethod.setQueryString(params);

		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		int statusCode = client.executeMethod(getMethod);
		if (statusCode != HttpStatus.SC_OK) {
			System.err.println("Method failed: " + getMethod.getStatusLine());
		}
		byte[] responseBody = getMethod.getResponseBody();
		String retStr = new String(responseBody);
		getMethod.releaseConnection();

		return retStr;
	}

	public static String gaePostMessage(String message, String token)
			throws HttpException, IOException {
		// https://api.facebook.com/method/status.set?status=asdfasdfasdfasdfasdfasdfasdfasdf&access_token=199812620030608|2920b2600e1a0ac4f29428f4-100001872430428|tMMmzsAv_4noicwf6nQakNULrCQ&format=json
		StringBuffer sb = new StringBuffer();
		message = URLEncoder.encode(message, "UTF-8");
		try {
			log.info("Trying to update user status using token: \"" + token
					+ "\". Message is" + message);

			String fullURL = "https://api.facebook.com/method/status.set?"
					+ "status=" + message + "&" + TOKEN_PARAM + "=" + token
					+ "&format=json";

			log.info("Post message url is: " + fullURL);

			URL url = new URL(fullURL);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String line;

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\r\n");
			}
			reader.close();
			return sb.toString();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}

		return null;
	}

	public static String gaeGetToken(String code) {

		StringBuffer sb = new StringBuffer();

		try {
			log.info("Trying to generate user token using code " + code);

			String fullURL = "https://graph.facebook.com/oauth/access_token?client_id="
					+ APP_ID
					+ "&redirect_uri=http://testspotapp.appspot.com/facebookcallback.jsp&client_secret="
					+ APP_SECRET + "&code=" + code;

			log.info("Token Generation url: " + fullURL);

			URL url = new URL(fullURL);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String line;

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\r\n");
			}
			reader.close();

		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}

		String tokenString = sb.toString();
		log.info("Token String from facebook: " + tokenString);
		String[] parameters = tokenString.split("&");
		for (String paraPair : parameters) {
			int index = paraPair.indexOf("=");
			String key = paraPair.substring(0, index);
			log.info("A key from token string: " + key);
			if (TOKEN_PARAM.equals(key) == false) {
				continue;
			}
			String value = "";
			if (paraPair.length() > index) {
				value = paraPair.substring(index + 1);
			}
			log.info("Token Value is: " + value);
			return value;
		}

		return null;
	}

}
