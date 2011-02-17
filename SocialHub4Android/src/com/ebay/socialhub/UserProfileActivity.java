/**
 * 
 */
package com.ebay.socialhub;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.googlecode.flickr2twitter.services.rest.models.UserModel;
import com.googlecode.flickr2twitter.services.rest.models.UserSourceServiceConfigModel;

/**
 * @author yayu
 *
 */
public class UserProfileActivity extends Activity {
	public static final String TAG = "SocialHub";
	
	private ProgressDialog m_ProgressDialog = null;
    private List<UserSourceServiceConfigModel> m_orders = null;
    private ItemAdapter m_adapter;
    private Runnable viewOrders;
	
	public static final String[] SERVICES = {"eBay", "Flickr"};
	
	private TextView txtUserName;
	private TextView txtUserEmail;
	private ListView sourceServiceList;
	
	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			if(m_orders == null || m_orders.size() == 0){
				Bundle extras = getIntent().getExtras();
				UserModel user = null;
				if (extras != null) {
					if (extras.containsKey("user")) {
						Object obj = extras.getSerializable("user");
						if (obj instanceof UserModel) {
							user = (UserModel)obj;
							m_orders = user.getSourceServices();
						}
					}
				}
			}
			
			if(m_orders != null && m_orders.size() > 0){
				
				m_adapter.notifyDataSetChanged();
				for(int i=0;i<m_orders.size();i++)
					m_adapter.add(m_orders.get(i));
			} 
			m_ProgressDialog.dismiss();
			m_adapter.notifyDataSetChanged();
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
						this.m_orders = user.getSourceServices();
					}
				}
			}
			
			this.txtUserName = (TextView)this.findViewById(R.id.userScreenName);
			this.txtUserEmail = (TextView)this.findViewById(R.id.userEmail);
			
			this.sourceServiceList = (ListView)this.findViewById(R.id.listView);
			//this.sourceServiceList = getListView();
			this.m_adapter = new ItemAdapter(
					this, R.layout.row, user.getSourceServices());
			//setListAdapter(this.m_adapter);
			sourceServiceList.setAdapter(this.m_adapter);
			
			this.sourceServiceList.setTextFilterEnabled(true);
			/*this.sourceServiceList.setOnItemClickListener(new OnItemClickListener() {
			    public void onItemClick(AdapterView<?> parent, View view,
			        int position, long id) {
			     
			    }
			  });*/
			
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
	
	private class ItemAdapter extends ArrayAdapter<UserSourceServiceConfigModel> {
		private List<UserSourceServiceConfigModel> items;

		public ItemAdapter(Context context, int textViewResourceId,
				List<UserSourceServiceConfigModel> objects) {
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
			UserSourceServiceConfigModel o = items.get(position);
			if (o != null) {
				TextView tt = (TextView) v.findViewById(R.id.toptext);
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);
				if (tt != null) {
					tt.setText("Provider: "+o.getServiceProviderId());                            }
				if(bt != null){
					bt.setText("User Site: "+ o.getUserSiteUrl());
				}
			}
			return v;
		}
	}

}
