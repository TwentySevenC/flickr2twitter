package com.ebay.socialhub;

import junit.framework.Assert;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import org.expressme.openid.Association;
import org.expressme.openid.Endpoint;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.googlecode.flickr2twitter.services.rest.models.GlobalTargetApplicationServiceModel;
import com.googlecode.flickr2twitter.services.rest.models.UserServiceConfigModel;

public class OAUTHTwitter extends Activity {
	private static final String TAG = "OAUTHTwitter";

	public static final String USER_TOKEN = "user_token";
	public static final String USER_SECRET = "user_secret";
	public static final String REQUEST_TOKEN = "request_token";
	public static final String REQUEST_SECRET = "request_secret";

	public static final String TWITTER_REQUEST_TOKEN_URL = "http://twitter.com/oauth/request_token";
	public static final String TWITTER_ACCESS_TOKEN_URL = "http://twitter.com/oauth/access_token";
	public static final String TWITTER_AUTHORIZE_URL = "http://twitter.com/oauth/authorize";

	private static final Uri CALLBACK_URI = Uri.parse("socialhub-app://twitter");

	public static final String PREFS = "MyPrefsFile";

	private OAuthConsumer mConsumer = null;
	private OAuthProvider mProvider = null;

	SharedPreferences mSettings;

	public void onCreate(Bundle icicle) {
		try {
			super.onCreate(icicle);
			GlobalTargetApplicationServiceModel target = null;
			if (getIntent().hasExtra(AuthorizeActivity.SERVICE_CONFIG_ID)) {
				target = (GlobalTargetApplicationServiceModel)getIntent().getExtras().get(AuthorizeActivity.SERVICE_CONFIG_ID);
			} else {
				finish();
			}
			// We don't need to worry about any saved states: we can reconstruct the state
			mConsumer = new CommonsHttpOAuthConsumer(
					target.getTargetAppConsumerId(), 
					target.getTargetAppConsumerSecret());

			mProvider = new CommonsHttpOAuthProvider (
					TWITTER_REQUEST_TOKEN_URL, 
					TWITTER_ACCESS_TOKEN_URL,
					TWITTER_AUTHORIZE_URL);

			// It turns out this was the missing thing to making standard Activity launch mode work
			mProvider.setOAuth10a(true);

			mSettings = this.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

			Intent i = this.getIntent();
			if (i.getData() == null) {
				// This is really important. If you were able to register your real callback Uri with Twitter, and not some fake Uri
				// like I registered when I wrote this example, you need to send null as the callback Uri in this function call. Then
				// Twitter will correctly process your callback redirection
				String authUrl = mProvider.retrieveRequestToken(mConsumer, CALLBACK_URI.toString());
				saveRequestInformation(mSettings, mConsumer.getToken(), mConsumer.getTokenSecret());
				this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
			}
		} catch (Exception e) {
			Log.e(TAG, "Failed on OAuth create", e);
		}
	}

	@Override
	protected void onResume() {
		try {
			super.onResume();

			Uri uri = getIntent().getData();
			if (uri != null && CALLBACK_URI.getScheme().equals(uri.getScheme())) {
				GlobalTargetApplicationServiceModel target;
				if (getIntent().hasExtra(AuthorizeActivity.SERVICE_CONFIG_ID)) {
					target = (GlobalTargetApplicationServiceModel)getIntent().getExtras().get(AuthorizeActivity.SERVICE_CONFIG_ID);
				} else {
					return;
				}
				String token = mSettings.getString(OAUTHTwitter.REQUEST_TOKEN, null);
				String secret = mSettings.getString(OAUTHTwitter.REQUEST_SECRET, null);
				try {

					mConsumer.setTokenWithSecret(token, secret);

					String otoken = uri.getQueryParameter(OAuth.OAUTH_TOKEN);
					String verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);

					// We send out and save the request token, but the secret is not the same as the verifier
					// Apparently, the verifier is decoded to get the secret, which is then compared - crafty
					// This is a sanity check which should never fail - hence the assertion
					Assert.assertEquals(otoken, mConsumer.getToken());

					// This is the moment of truth - we could throw here
					mProvider.retrieveAccessToken(mConsumer, verifier);
					// Now we can retrieve the goodies
					token = mConsumer.getToken();
					secret = mConsumer.getTokenSecret();
					OAUTHTwitter.saveAuthInformation(mSettings, token, secret);
					// Clear the request stuff, now that we have the real thing
					OAUTHTwitter.saveRequestInformation(mSettings, null, null);
					//i.putExtra(USER_TOKEN, token);
					//i.putExtra(USER_SECRET, secret);
				} catch (OAuthMessageSignerException e) {
					e.printStackTrace();
				} catch (OAuthNotAuthorizedException e) {
					e.printStackTrace();
				} catch (OAuthExpectationFailedException e) {
					e.printStackTrace();
				} catch (OAuthCommunicationException e) {
					e.printStackTrace();
				} finally {
					//startActivity(i); // we either authenticated and have the extras or not, but we're going back
					OAUTHTwitter.this.startActivity(
							new Intent(OAUTHTwitter.this, UserProfileActivity.class));
					finish();
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "Failed on OAuth onResume", e);
		}
	}

	public static void saveRequestInformation(SharedPreferences settings, String token, String secret) {
		// null means to clear the old values
		SharedPreferences.Editor editor = settings.edit();
		if(token == null) {
			editor.remove(OAUTHTwitter.REQUEST_TOKEN);
			Log.d(TAG, "Clearing Request Token");
		}
		else {
			editor.putString(OAUTHTwitter.REQUEST_TOKEN, token);
			Log.d(TAG, "Saving Request Token: " + token);
		}
		if (secret == null) {
			editor.remove(OAUTHTwitter.REQUEST_SECRET);
			Log.d(TAG, "Clearing Request Secret");
		}
		else {
			editor.putString(OAUTHTwitter.REQUEST_SECRET, secret);
			Log.d(TAG, "Saving Request Secret: " + secret);
		}
		editor.commit();

	}

	public static void saveAuthInformation(SharedPreferences settings, String token, String secret) {
		// null means to clear the old values
		SharedPreferences.Editor editor = settings.edit();
		if(token == null) {
			editor.remove(OAUTHTwitter.USER_TOKEN);
			Log.d(TAG, "Clearing OAuth Token");
		}
		else {
			editor.putString(OAUTHTwitter.USER_TOKEN, token);
			Log.d(TAG, "Saving OAuth Token: " + token);
		}
		if (secret == null) {
			editor.remove(OAUTHTwitter.USER_SECRET);
			Log.d(TAG, "Clearing OAuth Secret");
		}
		else {
			editor.putString(OAUTHTwitter.USER_SECRET, secret);
			Log.d(TAG, "Saving OAuth Secret: " + secret);
		}
		editor.commit();

	}

	/*private class OAuthTwitterTask extends AsyncTask<Void, Void, UserServiceConfigModel> {
		ProgressDialog authDialog;

		@Override
		protected void onPreExecute() {
			authDialog = ProgressDialog.show(OAUTHTwitter.this, 
				"Twitter Authentication", 
				"Redirecting to twitter for oauth...", 
				true,	// indeterminate duration
				false); // not cancel-able
		}

		 (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])

		@Override
		protected UserServiceConfigModel doInBackground(Void... params) {
			UserServiceConfigModel model = null;
			try {

				Intent i = OAUTHTwitter.this.getIntent();
				if (i.getData() == null) {
					Endpoint endpoint = manager.lookupEndpoint(ID_GOOGLE);
					Association association = manager.lookupAssociation(endpoint);
					String authUrl = manager.getAuthenticationUrl(endpoint, association);
					Log.i(TAG, "Google OpenID AuthURL: " + authUrl);
					OAUTHTwitter.this.startActivity(
							new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
			return model;
		}

		protected void onPostExecute(UserServiceConfigModel model) {
			authDialog.dismiss();
			if (model != null) {
				OAUTHTwitter.this.startActivity(
						new Intent(OAUTHTwitter.this, UserProfileActivity.class));
			}
		}

	}*/

}
