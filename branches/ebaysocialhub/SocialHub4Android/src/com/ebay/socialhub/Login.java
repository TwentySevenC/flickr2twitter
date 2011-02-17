/**
 * 
 */
package com.ebay.socialhub;

import org.apache.http.HttpVersion;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.restlet.resource.ClientResource;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.flickr2twitter.services.rest.models.ISociaHubResource;
import com.googlecode.flickr2twitter.services.rest.models.UserModel;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class Login extends Activity {
	public static final String TAG = "SocialHub";
	public static final String SERVER_LOCATION = "http://ebaysocialhub.appspot.com/rest/user";
	
	private EditText txtUserName;
	private EditText txtPassword;
	private Button btnLogin;
	private Button btnCancel;
	private ImageButton btnOpenidGoogle;
	private TextView txtUserScreenName;
	


	/**
	 * 
	 */
	public Login() {
		super();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.login);
			
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				if (extras.containsKey(GoogleOpenIDActivity.KEY_USER_EMAIL)) {
					String userEmail = extras.getString(GoogleOpenIDActivity.KEY_USER_EMAIL);
					new GetCredentialsTask().execute(userEmail);
				}
			}
			HttpParams parameters = new BasicHttpParams();
			HttpProtocolParams.setVersion(parameters, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(parameters, HTTP.DEFAULT_CONTENT_CHARSET);
			HttpProtocolParams.setUseExpectContinue(parameters, false);
			HttpConnectionParams.setTcpNoDelay(parameters, true);
			HttpConnectionParams.setSocketBufferSize(parameters, 8192);

			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

			txtUserName=(EditText)this.findViewById(R.id.txtUname);
			txtPassword=(EditText)this.findViewById(R.id.txtPwd);
			btnLogin=(Button)this.findViewById(R.id.btnLogin);
			btnCancel=(Button)this.findViewById(R.id.btnCancel);
			btnOpenidGoogle=(ImageButton)this.findViewById(R.id.btnOpenidGoogle);
			txtUserScreenName = (TextView)this.findViewById(R.id.txtUserScreenName);

			btnLogin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new GetCredentialsTask().execute();
				}
			});
			
			btnOpenidGoogle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(Login.this, GoogleOpenIDActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}
			});
			
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
	}

	private class GetCredentialsTask extends AsyncTask<String, Void, UserModel> {
		ProgressDialog authDialog;
		 
		@Override
		protected void onPreExecute() {
			authDialog = ProgressDialog.show(Login.this, 
				getText(R.string.auth_progress_title), 
				getText(R.string.auth_progress_text), 
				true,	// indeterminate duration
				false); // not cancel-able
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected UserModel doInBackground(String... params) {
			UserModel user = null;
			try {
				ClientResource cr = new ClientResource(SERVER_LOCATION);
				ISociaHubResource resource = cr.wrap(ISociaHubResource.class);
				String userEmail = null;
				if (params != null && params.length == 1) {
					userEmail = params[0];
				}
				
				// Get the remote contact
				if (userEmail != null) {
					user = resource.openidLogin(userEmail);
				} else {
					user = resource.login(txtUserName.getText().toString()
							, txtPassword.getText().toString());
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
			return user;
		}
		
		protected void onPostExecute(UserModel user) {
			try {
				authDialog.dismiss();
				if(user != null){
					txtUserName.setText(user.getUserId());
					//txtUserScreenName.setText(user.toString());
					Toast.makeText(Login.this, "Login Successful",Toast.LENGTH_LONG).show();
					Intent i = new Intent(Login.this, UserProfileActivity.class);
					i.putExtra("user", user);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				} else{
					txtUserScreenName.setText("Not Logged In");
					Toast.makeText(Login.this, "Invalid Login",Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
		}
		
	}

}
