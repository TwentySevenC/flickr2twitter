/**
 * 
 */
package com.ebay.socialhub;

import org.expressme.openid.Association;
import org.expressme.openid.Endpoint;
import org.expressme.openid.OpenIdManager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

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
			manager = new OpenIdManager();
	        manager.setReturnTo(GAE_CALLBACK_URL);
			
			Intent i = this.getIntent();
			if (i.getData() == null) {
				Endpoint endpoint = manager.lookupEndpoint(ID_GOOGLE);
				Association association = manager.lookupAssociation(endpoint);
				String authUrl = manager.getAuthenticationUrl(endpoint, association);
				Log.i(TAG, "Google OpenID AuthURL: " + authUrl);
				//saveRequestInformation(mSettings, mConsumer.getToken(), mConsumer.getTokenSecret());
				this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
			}
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
					loginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
					loginActivity.putExtra(KEY_USER_EMAIL, userEmail);
					setResult(RESULT_OK, loginActivity);
					this.startActivity(loginActivity);
//					Toast.makeText(GoogleOpenIDActivity.this, "Login Successful - " + userEmail ,Toast.LENGTH_LONG).show();
//					super.finish();
				}
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
	}
	
	

}
