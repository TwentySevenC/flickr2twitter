/*
 * Copyright (c) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.flickr2twitter.impl.picasa;

import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.googleapis.auth.authsub.AuthSubHelper;
import com.google.api.client.googleapis.auth.authsub.AuthSubSingleUseTokenRequestUrl;
import com.google.api.client.googleapis.auth.authsub.AuthSubHelper.TokenInfoResponse;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthGetAccessToken;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthGetTemporaryToken;
import com.google.api.client.http.HttpTransport;

/**
 * Implements OAuth authentication.
 *
 * @author Yaniv Inbar
 */
public class Auth {

  private static final String APP_NAME =
      "Picasa Web Albums Data API Java Client";

  private static final String SCOPE = "http://picasaweb.google.com/data";

  private static OAuthHmacSigner signer;

  private static OAuthCredentialsResponse credentials;

  static void authorize(HttpTransport transport) throws Exception {
    // callback server
    String verifier = null;
    String tempToken = null;
    try {
      // temporary token
      GoogleOAuthGetTemporaryToken temporaryToken =
          new GoogleOAuthGetTemporaryToken();
      signer = new OAuthHmacSigner();
      signer.clientSharedSecret = "anonymous";
      temporaryToken.signer = signer;
      temporaryToken.consumerKey = "anonymous";
      temporaryToken.scope = SCOPE;
      temporaryToken.displayName = APP_NAME;
      //temporaryToken.callback = callbackServer.getCallbackUrl();
      OAuthCredentialsResponse tempCredentials = temporaryToken.execute();
      signer.tokenSharedSecret = tempCredentials.tokenSecret;
      // authorization URL
      GoogleOAuthAuthorizeTemporaryTokenUrl authorizeUrl =
          new GoogleOAuthAuthorizeTemporaryTokenUrl();
      authorizeUrl.temporaryToken = tempToken = tempCredentials.token;
      String authorizationUrl = authorizeUrl.build();
      // launch in browser
      boolean browsed = false;
      if (!browsed) {
        String browser = "google-chrome";
        Runtime.getRuntime().exec(new String[] {browser, authorizationUrl});
      }
     // verifier = callbackServer.waitForVerifier(tempToken);
    } finally {
      /*if (callbackServer != null) {
        callbackServer.stop();
      }*/
    }
    GoogleOAuthGetAccessToken accessToken = new GoogleOAuthGetAccessToken();
    accessToken.temporaryToken = tempToken;
    accessToken.signer = signer;
    accessToken.consumerKey = "anonymous";
    accessToken.verifier = verifier;
    credentials = accessToken.execute();
    signer.tokenSharedSecret = credentials.tokenSecret;
    createOAuthParameters().signRequestsUsingAuthorizationHeader(transport);
  }

  static void revoke() {
    if (credentials != null) {
      try {
        GoogleOAuthGetAccessToken.revokeAccessToken(createOAuthParameters());
      } catch (Exception e) {
        e.printStackTrace(System.err);
      }
    }
  }

  private static OAuthParameters createOAuthParameters() {
    OAuthParameters authorizer = new OAuthParameters();
    authorizer.consumerKey = "anonymous";
    authorizer.signer = signer;
    authorizer.token = credentials.token;
    return authorizer;
  }
  
  public static void main(String[] args) {
	  try {
		  
		  String hostedDomain = "flickr2twitter.googlecode.com";
		  String nextUrl = "http://autoflickr2twitter.appspot.com/";
		  String scope = SCOPE;
		  boolean secure = false;  // set secure=true to request AuthSub tokens
		  boolean session = true;
		  AuthSubSingleUseTokenRequestUrl authorizeUrl = new AuthSubSingleUseTokenRequestUrl();
		  authorizeUrl.hostedDomain = hostedDomain;
		  authorizeUrl.nextUrl = nextUrl;
		  authorizeUrl.scope = scope;
		  authorizeUrl.session = 1;
		  //authorizeUrl.secure = AuthSub.
		  //String authSubUrl = AuthSubUtil.getRequestUrl(hostedDomain, nextUrl, scope, secure, session);
		  String authorizationUrl = authorizeUrl.build();
		  System.out.println(authorizationUrl);
		  
		 /* AuthSubHelper helper = new AuthSubHelper();
		  
		  helper.setToken("SlDP_NSucGPZr17Kunt5lurog1WmnvLXZ-I4iSALAUg");
		  TokenInfoResponse response = helper.requestTokenInfo();
		  System.out.println(response);*/
		  //System.out.println(tempToken);
		  
		  /*GoogleOAuthGetAccessToken token = new GoogleOAuthGetAccessToken();
		  
		  signer = new OAuthHmacSigner();
		  signer.clientSharedSecret = "anonymous";
	      token.signer = signer;
	      token.consumerKey = "anonymous"; //"flickr2twitter.googlecode.com";
	      OAuthCredentialsResponse tempCredentials = token.execute();
	      GoogleOAuthAuthorizeTemporaryTokenUrl authorizeUrl =
	          new GoogleOAuthAuthorizeTemporaryTokenUrl();
	      authorizeUrl.temporaryToken = tempCredentials.token;
	      String authorizationUrl = authorizeUrl.build();
	      System.out.println(authorizationUrl);*/
	  } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
  }
}
