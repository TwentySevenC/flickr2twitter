/**
 * 
 */
package com.ebay.socialhub;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.googlecode.flickr2twitter.services.rest.models.UserModel;
import com.googlecode.flickr2twitter.services.rest.models.UserServiceConfigModel;
import com.googlecode.flickr2twitter.services.rest.models.UserSourceServiceConfigModel;
import com.googlecode.flickr2twitter.services.rest.models.UserTargetServiceConfigModel;

/**
 * @author yayu
 *
 */
public class UserProfileActivity extends Activity {
	public static final String TAG = "SocialHub";
	
	private ProgressDialog m_ProgressDialog = null;
    private List<UserSourceServiceConfigModel> sourceServiceList = null;
    private List<UserTargetServiceConfigModel> targetServiceList = null;
    private ItemAdapter sourceAdapter;
    private ItemAdapter targetAdapter;
    private Runnable viewOrders;
	
	private TextView txtUserName;
	private TextView txtUserEmail;
	private ListView sourceServiceListView;
	private ListView targetServiceListView;
	
	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			if(sourceServiceList != null && sourceServiceList.size() > 0){
				sourceAdapter.notifyDataSetChanged();
				for(int i=0;i<sourceServiceList.size();i++)
					sourceAdapter.add(sourceServiceList.get(i));
			}
			
			if(targetServiceList != null && targetServiceList.size() > 0){
				targetAdapter.notifyDataSetChanged();
				for(int i=0;i<targetServiceList.size();i++)
					targetAdapter.add(targetServiceList.get(i));
			}
			
			m_ProgressDialog.dismiss();
			sourceAdapter.notifyDataSetChanged();
			targetAdapter.notifyDataSetChanged();
		}
	};
	
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
			setContentView(R.layout.main);
			
			Bundle extras = getIntent().getExtras();
			UserModel user = null;
			if (extras != null) {
				if (extras.containsKey("user")) {
					Object obj = extras.getSerializable("user");
					if (obj instanceof UserModel) {
						user = (UserModel)obj;
						this.sourceServiceList = user.getSourceServices();
						this.targetServiceList = user.getTargetServices();
					}
				}
			}
			
			this.txtUserName = (TextView)this.findViewById(R.id.userScreenName);
			this.txtUserEmail = (TextView)this.findViewById(R.id.userEmail);
			
			this.sourceServiceListView = (ListView)this.findViewById(R.id.sourceServiceList);
			this.sourceAdapter = new ItemAdapter(
					this, R.layout.row, new ArrayList<UserServiceConfigModel>());
			sourceServiceListView.setAdapter(this.sourceAdapter);
			this.sourceServiceListView.setTextFilterEnabled(true);

			this.sourceServiceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			    public void onItemClick(AdapterView<?> parent, View view,
			        int position, long id) {
			    	UserServiceConfigModel serviceModel = sourceAdapter.items.get(position);
					if (serviceModel != null && serviceModel.getUserSiteUrl() != null) {
						UserProfileActivity.this.startActivity(
								new Intent(Intent.ACTION_VIEW, Uri.parse(serviceModel.getUserSiteUrl())));
					}
			    }
			    
			  });
			
			this.targetServiceListView = (ListView)this.findViewById(R.id.targetServiceList);
			this.targetAdapter = new ItemAdapter(
					this, R.layout.row, new ArrayList<UserServiceConfigModel>());
			targetServiceListView.setAdapter(this.targetAdapter);
			this.targetServiceListView.setTextFilterEnabled(true);
			
			if (user != null) {
				txtUserName.setText(user.getScreenName());
				txtUserEmail.setText(user.getUserId());
			}
			
			viewOrders = new Runnable(){
	            @Override
	            public void run() {
	            	runOnUiThread(returnRes);
	            }
	        };
			Thread thread =  new Thread(null, viewOrders, "MagentoBackground");
			thread.start();
			m_ProgressDialog = ProgressDialog.show(UserProfileActivity.this,    
					"Please wait...", "Retrieving data ...", true);
		
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
				int iconId = -1;
				if ("flickr".equalsIgnoreCase(providerId)) {
					iconId = R.drawable.flickr_32;
				} else if ("youtube".equalsIgnoreCase(providerId)) {
					iconId = R.drawable.youtube_32;
				} else if ("facebook".equalsIgnoreCase(providerId)) {
					iconId = R.drawable.facebook_32;
				} else if ("twitter".equalsIgnoreCase(providerId)) {
					iconId = R.drawable.twitter_32;
				} else if ("picasa".equalsIgnoreCase(providerId)) {
					iconId = R.drawable.picasa_32;
				} else if ("ebay".equalsIgnoreCase(providerId)) {
					iconId = R.drawable.ebay_32;
				} else if ("sina".equalsIgnoreCase(providerId)) {
					iconId = R.drawable.sina_32;
				}
				
				if (iconId >= 0) {
					image.setImageResource(iconId);
				}
				
				TextView tt = (TextView) v.findViewById(R.id.toptext);
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);
				if (tt != null) {
					tt.setText("Provider: "+serviceModel.getServiceProviderId());                            }
				if(bt != null){
					bt.setText("User ID: "+ serviceModel.getServiceUserName());
				}
			}
			return v;
		}
	}

}
