/**
 * 
 */
package com.ebay.socialhub;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
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

import com.googlecode.flickr2twitter.services.rest.models.IUserResource;
import com.googlecode.flickr2twitter.services.rest.models.UserModel;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class Login extends Activity {
	public static final String TAG = "SocialHub";
	public static final String SERVER_LOCATION = "http://ebaysocialhub.appspot.com/rest/user";
	private static final int REQUEST_CODE = 10;
	
	private EditText txtUserName;
	private EditText txtPassword;
	private Button btnLogin;
	private Button btnCancel;
	private ImageButton btnOpenidGoogle;
	private TextView txtUserScreenName;
	
	
	private HttpClient mClient;
	


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
			
			HttpParams parameters = new BasicHttpParams();
			HttpProtocolParams.setVersion(parameters, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(parameters, HTTP.DEFAULT_CONTENT_CHARSET);
			HttpProtocolParams.setUseExpectContinue(parameters, false);
			HttpConnectionParams.setTcpNoDelay(parameters, true);
			HttpConnectionParams.setSocketBufferSize(parameters, 8192);

			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			ClientConnectionManager tsccm = new ThreadSafeClientConnManager(parameters, schReg);
			mClient = new DefaultHttpClient(tsccm, parameters);

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
					startActivityForResult(i, REQUEST_CODE);
				}
			});
			
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}

		
	}
	
	
	
	@Override
	protected void onResume() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (extras.containsKey(GoogleOpenIDActivity.KEY_USER_EMAIL)) {
				String userEmail = extras.getString(GoogleOpenIDActivity.KEY_USER_EMAIL);
				new GetCredentialsTask().execute(userEmail);
			}
		}
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			if (data.hasExtra(GoogleOpenIDActivity.KEY_USER_EMAIL)) {
				String userEmail = data.getExtras().getString(GoogleOpenIDActivity.KEY_USER_EMAIL);
				new GetCredentialsTask().execute(userEmail);
			}
		}
	}

	
	/* (non-Javadoc)
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		super.finish();
		mClient.getConnectionManager().shutdown();
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
				IUserResource resource = cr.wrap(IUserResource.class);
				String userEmail = null;
				if (params != null && params.length == 1) {
					userEmail = params[0];
				}
				if (userEmail != null) {
					//txtUserName.setText(userEmail);
					user = resource.openidLogin(userEmail);
				} else {
					// Get the remote contact
					user = resource.login(txtUserName.getText().toString()
							, txtPassword.getText().toString());
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
			return user;
		}
		
		protected void onPostExecute(UserModel user) {
			authDialog.dismiss();
			if(user != null){
				txtUserName.setText(user.getUserId());
				txtUserScreenName.setText(user.toString());
				Toast.makeText(Login.this, "Login Successful",Toast.LENGTH_LONG).show();
			} else{
				txtUserScreenName.setText("Not Logged In");
				Toast.makeText(Login.this, "Invalid Login",Toast.LENGTH_LONG).show();
			}
		}
		
	}

}
