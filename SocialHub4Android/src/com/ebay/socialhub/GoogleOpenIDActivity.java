/**
 * 
 */
package com.ebay.socialhub;

import org.expressme.openid.Association;
import org.expressme.openid.Endpoint;
import org.expressme.openid.OpenIdManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class GoogleOpenIDActivity extends Activity {
	public static final String TAG = "SocialHub";
	public static final String ID_GOOGLE = "Google";
	private OpenIdManager manager;
	
	private static final Uri CALLBACK_URI = Uri.parse("socialhub-openid://google");
	
	private static final String GAE_CALLBACK_URL = "http://ebaysocialhub.appspot.com/google_openid_callback.jsp";
	public static final String KEY_USER_EMAIL = "userEmail";

	
	/**
	 * 
	 */
	public GoogleOpenIDActivity() {
		super();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			new AuthGoogleOpenIDTask().execute();
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		try {
			super.onResume();
			Uri uri = getIntent().getData();
			
			if (uri != null && CALLBACK_URI.getScheme().equals(uri.getScheme())) {
				String query = uri.getQuery();
				if (query.startsWith(KEY_USER_EMAIL)) {
					String userEmail = query.substring(KEY_USER_EMAIL.length() + 1, query.length());
					Intent loginActivity = new Intent(this, Login.class);
					//loginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
					loginActivity.putExtra(KEY_USER_EMAIL, userEmail);
					loginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					this.startActivity(loginActivity);
				}
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
	}
	
	private class AuthGoogleOpenIDTask extends AsyncTask<Void, Void, String> {
		ProgressDialog authDialog;
		 
		@Override
		protected void onPreExecute() {
			authDialog = ProgressDialog.show(GoogleOpenIDActivity.this, 
				getText(R.string.auth_progress_title), 
				getText(R.string.google_openid_oauth_message), 
				true,	// indeterminate duration
				false); // not cancel-able
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(Void... params) {
			String message = null;
			try {
				manager = new OpenIdManager();
		        manager.setReturnTo(GAE_CALLBACK_URL);
				
				Intent i = GoogleOpenIDActivity.this.getIntent();
				if (i.getData() == null) {
					Endpoint endpoint = manager.lookupEndpoint(ID_GOOGLE);
					Association association = manager.lookupAssociation(endpoint);
					String authUrl = manager.getAuthenticationUrl(endpoint, association);
					Log.i(TAG, "Google OpenID AuthURL: " + authUrl);
					GoogleOpenIDActivity.this.startActivity(
							new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
				message = e.toString();
			}
			return message;
		}
		
		protected void onPostExecute(String result) {
			authDialog.dismiss();
			if (result != null) {
				Toast.makeText(GoogleOpenIDActivity.this, 
					"Google OpenID OAuth - " + result,Toast.LENGTH_LONG).show();
			}
		}
		
	}

}
