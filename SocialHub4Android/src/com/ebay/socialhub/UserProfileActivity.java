/**
 * 
 */
package com.ebay.socialhub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.restlet.resource.ClientResource;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.flickr2twitter.services.rest.models.GlobalApplicationConfigModel;
import com.googlecode.flickr2twitter.services.rest.models.GlobalApplicationConfigModelList;
import com.googlecode.flickr2twitter.services.rest.models.ISociaHubResource;
import com.googlecode.flickr2twitter.services.rest.models.UserModel;
import com.googlecode.flickr2twitter.services.rest.models.UserServiceConfigModel;

/**
 * @author yayu
 *
 */
public class UserProfileActivity extends Activity {
	public static final String TAG = "SocialHub";
	private static final Map<String, Integer> ICON_MAP;
	public static final String TAG_USER = "user";
	
	private static final String HEADER_SOURCE = "Authroized Source Services";
	private static final String HEADER_TARGET = "Authroized Target Services";
	
    private SectionedAdapter sourceAdapter;
	
	private TextView txtUserName;
	private TextView txtUserEmail;
	private ListView sourceServiceListView;
	
	static {
		Map<String, Integer> map = new HashMap<String, Integer>();
		/*map.put("flickr", R.drawable.flickr_32);
		map.put("youtube", R.drawable.youtube_32);
		map.put("facebook", R.drawable.facebook_32);
		map.put("twitter", R.drawable.twitter_32);
		map.put("picasa", R.drawable.picasa_32);
		map.put("ebay", R.drawable.ebay_32);
		map.put("sina", R.drawable.sina_32);*/
		map.put("flickr", R.drawable.flickr_64);
		map.put("youtube", R.drawable.youtube_64);
		map.put("facebook", R.drawable.facebook_64);
		map.put("twitter", R.drawable.twitter_64);
		map.put("picasa", R.drawable.picasa_64);
		map.put("ebay", R.drawable.ebay_64);
		map.put("sina", R.drawable.sina_64);
		ICON_MAP = Collections.unmodifiableMap(map);
	}
	
	/**
	 * 
	 */
	public UserProfileActivity() {
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			Bundle extras = getIntent().getExtras();
			UserModel user = null;
			boolean selfInit = false;
			if (extras != null) {
				if (extras.containsKey(TAG_USER)) {
					Object obj = extras.getSerializable(TAG_USER);
					if (obj instanceof UserModel) {
						user = (UserModel)obj;
					}
				} else if (extras.containsKey(OAuthActivity.KEY_USER_EMAIL)) {
					selfInit = true;
					new GetUserProfileTask().execute(extras.getString(OAuthActivity.KEY_USER_EMAIL));
				}
			}
			if (user == null && selfInit == false) {
				Intent intent = new Intent(this, Login.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				UserProfileActivity.this.startActivity(intent);
				finish();
			}
			setContentView(R.layout.main);
			
			this.txtUserName = (TextView)this.findViewById(R.id.userScreenName);
			this.txtUserEmail = (TextView)this.findViewById(R.id.userEmail);
			
			final UserModel userModel = user;
			
			final OnClickListener clickListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (v instanceof TextView) {
						Intent i = new Intent(UserProfileActivity.this, AuthorizeActivity.class);
						if (UserProfileActivity.this.getIntent().hasExtra(AuthorizeActivity.SERVICES_ID)) {
							i.putExtra(AuthorizeActivity.SERVICES_ID, 
									UserProfileActivity.this.getIntent().getExtras().getSerializable(AuthorizeActivity.SERVICES_ID));
						}
						if (userModel != null) {
							i.putExtra(OAuthActivity.KEY_USER_EMAIL, userModel.getUserId());
						}
						UserProfileActivity.this.startActivity(i);
					}
				}
			};
			
			this.sourceServiceListView = (ListView)this.findViewById(R.id.sourceServiceList);
			if (user != null) {
				this.sourceAdapter = new SectionedAdapter() {

					@Override
					protected View getHeaderView(String caption,
							int index, View convertView, ViewGroup parent) {
						TextView result = (TextView) convertView;

						if (convertView == null) {
							result = (TextView) getLayoutInflater().inflate(
									R.layout.header, null);
						}

						result.setText(caption);
						result.setOnClickListener(clickListener);
						return (result);
					}
				};

				sourceAdapter.addSection(HEADER_SOURCE, new ItemAdapter(
						this, R.layout.row, 
						new ArrayList<UserServiceConfigModel>(user.getSourceServices())));

				sourceAdapter.addSection(HEADER_TARGET, new ItemAdapter(
						this, R.layout.row, new ArrayList<UserServiceConfigModel>(user.getTargetServices())));
				sourceServiceListView.setAdapter(this.sourceAdapter);
			}
			this.sourceServiceListView.setTextFilterEnabled(true);
			

			this.sourceServiceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			    public void onItemClick(AdapterView<?> parent, View view,
			        int position, long id) {
			    	Object obj = sourceAdapter.getItem(position);
			    	Toast.makeText(UserProfileActivity.this, String.valueOf(obj), Toast.LENGTH_SHORT).show();
			    	/*UserServiceConfigModel serviceModel = sourceAdapter.items.get(position);
					if (serviceModel != null && serviceModel.getUserSiteUrl() != null) {
						UserProfileActivity.this.startActivity(
								new Intent(Intent.ACTION_VIEW, Uri.parse(serviceModel.getUserSiteUrl())));
					}*/
			    }
			    
			  });
			
			if (user != null) {
				txtUserName.setText(user.getScreenName());
				txtUserEmail.setText(user.getUserId());
			}
		
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
	}

	private class ItemAdapter extends ArrayAdapter<UserServiceConfigModel> {
		private List<UserServiceConfigModel> items;

		public ItemAdapter(Context context, int textViewResourceId,
				List<UserServiceConfigModel> objects) {
			super(context, textViewResourceId, objects);
			this.items = objects;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row, null);
			}
			UserServiceConfigModel serviceModel = items.get(position);
			if (serviceModel != null) {
				ImageView image = (ImageView) v.findViewById(R.id.serviceIcon);
				String providerId = serviceModel.getServiceProviderId();
				
				if (providerId != null && ICON_MAP.containsKey(providerId.toLowerCase(Locale.US))) {
					image.setImageResource(ICON_MAP.get(providerId.toLowerCase(Locale.US)));
				}
				
				TextView tt = (TextView) v.findViewById(R.id.toptext);
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);
				
				String providerName = serviceModel.getServiceProviderId();
				if (UserProfileActivity.this.getIntent().hasExtra(AuthorizeActivity.SERVICES_ID)) {
					GlobalApplicationConfigModelList models = 
						(GlobalApplicationConfigModelList)UserProfileActivity.this.getIntent().getExtras().getSerializable(AuthorizeActivity.SERVICES_ID);
					for (GlobalApplicationConfigModel model : models.getGlobalAppConfigModels()) {
						if (providerName.equalsIgnoreCase(model.getProviderId())) {
							providerName = model.getAppName();
						}
					}
				}
				
				if (tt != null) {
					tt.setText(providerName);  
				}
				if(bt != null){
					bt.setText(serviceModel.getServiceUserName());
				}
			}
			return v;
		}
	}
	
	private class GetUserProfileTask extends AsyncTask<String, Void, Data> {
		ProgressDialog authDialog;
		 
		@Override
		protected void onPreExecute() {
			authDialog = ProgressDialog.show(UserProfileActivity.this, 
				getText(R.string.auth_progress_title), 
				getText(R.string.auth_progress_text), 
				true,	// indeterminate duration
				false); // not cancel-able
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Data doInBackground(String... params) {
			Data data = null;
			try {
				ClientResource cr = new ClientResource(Login.SERVER_LOCATION);
				ISociaHubResource resource = cr.wrap(ISociaHubResource.class);
				String userEmail = null;
				if (params != null && params.length == 1) {
					userEmail = params[0];
				}
				
				// Get the remote contact
				if (userEmail != null) {
					UserModel user = resource.openidLogin(userEmail);
					
					data = new Data(user, resource.getSupportedServiceProviders());
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
			return data;
		}
		
		protected void onPostExecute(Data data) {
			try {
				authDialog.dismiss();
				if(data != null){
					Toast.makeText(UserProfileActivity.this, "Login Successful",Toast.LENGTH_LONG).show();
					Intent intent = getIntent();
					intent.putExtra(TAG_USER, data.user);
					intent.putExtra(AuthorizeActivity.SERVICES_ID, data.serviceProviders);
					finish();
					startActivity(intent);
				} else{
					Intent intent = new Intent(UserProfileActivity.this, Login.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					UserProfileActivity.this.startActivity(intent);
					finish();
					Toast.makeText(UserProfileActivity.this, "Invalid Login",Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
		}
		
	}
	
	private class Data {
		private UserModel user;
		private GlobalApplicationConfigModelList serviceProviders;
		/**
		 * @param user
		 * @param serviceProviders
		 */
		public Data(UserModel user,
				GlobalApplicationConfigModelList serviceProviders) {
			super();
			this.user = user;
			this.serviceProviders = serviceProviders;
		}
		
		
	}

}

