/**
 * 
 */
package com.ebay.socialhub;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.flickr2twitter.services.rest.models.UserModel;

/**
 * @author yayu
 *
 */
public class UserProfileActivity extends Activity {
	public static final String TAG = "SocialHub";
	
	public static final String[] SERVICES = {"eBay", "Flickr"};
	
	private TextView txtUserName;
	private TextView txtUserEmail;
	private ListView sourceServiceList;
	
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
			setContentView(R.layout.user_profile);
			
			Bundle extras = getIntent().getExtras();
			UserModel user = null;
			if (extras != null) {
				if (extras.containsKey("user")) {
					Object obj = extras.getSerializable("user");
					if (obj instanceof UserModel) {
						user = (UserModel)obj;
					}
				}
			}
			
			this.txtUserName = (TextView)this.findViewById(R.id.userScreenName);
			this.txtUserEmail = (TextView)this.findViewById(R.id.userEmail);
			
			
			this.sourceServiceList = (ListView)this.findViewById(R.id.srcServiceList);
			this.sourceServiceList.setAdapter(new ArrayAdapter<String>(
					this, R.layout.user_service_list, SERVICES));
			this.sourceServiceList.setTextFilterEnabled(true);
			this.sourceServiceList.setOnItemClickListener(new OnItemClickListener() {
			    public void onItemClick(AdapterView<?> parent, View view,
			        int position, long id) {
			     
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
	
	

}
