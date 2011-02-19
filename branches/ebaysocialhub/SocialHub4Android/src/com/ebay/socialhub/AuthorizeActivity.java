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
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.flickr2twitter.services.rest.models.GlobalApplicationConfigModel;
import com.googlecode.flickr2twitter.services.rest.models.GlobalApplicationConfigModelList;
import com.googlecode.flickr2twitter.services.rest.models.GlobalSourceApplicationServiceModel;
import com.googlecode.flickr2twitter.services.rest.models.GlobalTargetApplicationServiceModel;
import com.googlecode.flickr2twitter.services.rest.models.ISociaHubResource;

/**
 * @author yayu
 *
 */
public class AuthorizeActivity extends Activity {
	public static final String TAG = "SocialHub";
	private static final Map<String, Integer> ICON_MAP;
	
    private SectionedAdapter servicesAdapter;
	
	private ListView authorizeServiceListView;
	
	static {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("flickr", R.drawable.flickr_32);
		map.put("youtube", R.drawable.youtube_32);
		map.put("facebook", R.drawable.facebook_32);
		map.put("twitter", R.drawable.twitter_32);
		map.put("picasa", R.drawable.picasa_32);
		map.put("ebay", R.drawable.ebay_32);
		map.put("sina", R.drawable.sina_32);
		
		ICON_MAP = Collections.unmodifiableMap(map);
	}
	
	/**
	 * 
	 */
	public AuthorizeActivity() {
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.authorize);
			
			new GetAvailableServicesTask().execute();
			final OnLongClickListener longClickListener = new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					if (v instanceof TextView) {
						TextView txtV = (TextView)v;
						Toast.makeText(AuthorizeActivity.this, txtV.getText(),
								Toast.LENGTH_SHORT).show();
						return true;
					}
					return false;
				}
			};
			this.servicesAdapter = new SectionedAdapter() {

				@Override
				protected View getHeaderView(String caption,
						int index, View convertView, ViewGroup parent) {
					TextView result = (TextView) convertView;

					if (convertView == null) {
						result = (TextView) getLayoutInflater().inflate(
								R.layout.header, null);
					}

					result.setText(caption);
					result.setOnLongClickListener(longClickListener);
					return (result);
				}
			};
			
			/*String authPagePath = null;
			String configPagePath = null;
			String imagePath = null;
			String sourceAppApiKey = null;
			String sourceAppSecret = null;
			String targetAppConsumerId = null;
			String targetAppConsumerSecret = null;*/
			
			List<GlobalApplicationConfigModel> sources = new ArrayList<GlobalApplicationConfigModel>();
			/*sources.add(new GlobalSourceApplicationServiceModel("ebay", "eBay", "	The world's leading e-commerce site", authPagePath, configPagePath, imagePath, sourceAppApiKey, sourceAppSecret));
			sources.add(new GlobalSourceApplicationServiceModel("flickr", "Flickr", "The world's leading online photo storage service ", authPagePath, configPagePath, imagePath, sourceAppApiKey, sourceAppSecret));
			sources.add(new GlobalSourceApplicationServiceModel("picasa", "Picasa Web Album", "The Google's online photo storage service", authPagePath, configPagePath, imagePath, sourceAppApiKey, sourceAppSecret));
			sources.add(new GlobalSourceApplicationServiceModel("youtube", "Youtube", "The Google's online video-sharing service", authPagePath, configPagePath, imagePath, sourceAppApiKey, sourceAppSecret));*/
			List<GlobalApplicationConfigModel> targets = new ArrayList<GlobalApplicationConfigModel>();
			/*targets.add(new GlobalTargetApplicationServiceModel("facebook", "Facebook", "Facebook Status service", authPagePath, configPagePath, imagePath, targetAppConsumerId, targetAppConsumerSecret));
			targets.add(new GlobalTargetApplicationServiceModel("twitter", "Twitter", "The world's leading online micro-blog service", authPagePath, configPagePath, imagePath, targetAppConsumerId, targetAppConsumerSecret));
			targets.add(new GlobalTargetApplicationServiceModel("Sina", "Sina MicroBlog", "The MaLeGeBi's leading online micro-blog service", authPagePath, configPagePath, imagePath, targetAppConsumerId, targetAppConsumerSecret));*/
			
			/*servicesAdapter.addSection("Source Services", new ItemAdapter(
					this, R.layout.row, 
					sources));

			servicesAdapter.addSection("Target Services", new ItemAdapter(
					this, R.layout.row, targets));*/
			
			this.authorizeServiceListView = (ListView)this.findViewById(R.id.authorizeServiceList);
			//authorizeServiceListView.setAdapter(this.servicesAdapter);
			this.authorizeServiceListView.setTextFilterEnabled(true);
			

			this.authorizeServiceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			    public void onItemClick(AdapterView<?> parent, View view,
			        int position, long id) {
			    	Object obj = servicesAdapter.getItem(position);
			    	Toast.makeText(AuthorizeActivity.this, String.valueOf(obj), Toast.LENGTH_SHORT).show();
			    	/*UserServiceConfigModel serviceModel = servicesAdapter.items.get(position);
					if (serviceModel != null && serviceModel.getUserSiteUrl() != null) {
						UserProfileActivity.this.startActivity(
								new Intent(Intent.ACTION_VIEW, Uri.parse(serviceModel.getUserSiteUrl())));
					}*/
			    }
			    
			  });
		
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
	}
	
	private class ItemAdapter extends ArrayAdapter<GlobalApplicationConfigModel> {
		private List<GlobalApplicationConfigModel> items;

		public ItemAdapter(Context context, int textViewResourceId,
				List<GlobalApplicationConfigModel> objects) {
			super(context, textViewResourceId, objects);
			this.items = objects;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.authorize_row, null);
			}
			GlobalApplicationConfigModel serviceModel = items.get(position);
			if (serviceModel != null) {
				ImageView image = (ImageView) v.findViewById(R.id.authorizeServiceIcon);
				String providerId = serviceModel.getProviderId();
				
				if (providerId != null && ICON_MAP.containsKey(providerId.toLowerCase(Locale.US))) {
					image.setImageResource(ICON_MAP.get(providerId.toLowerCase(Locale.US)));
				}
				
				TextView tt = (TextView) v.findViewById(R.id.txtAuthAppName);
				TextView bt = (TextView) v.findViewById(R.id.txtAuthDescription);
				if (tt != null) {
					tt.setText(serviceModel.getAppName());                            }
				if(bt != null){
					bt.setText(serviceModel.getDescription());
				}
			}
			return v;
		}
	}
	
	private class GetAvailableServicesTask extends AsyncTask<Void, Void, List<GlobalApplicationConfigModel>> {
		ProgressDialog authDialog;
		 
		@Override
		protected void onPreExecute() {
			authDialog = ProgressDialog.show(AuthorizeActivity.this, 
				"Contacting Server", 
				"Retrieving supported services from server...", 
				true,	// indeterminate duration
				false); // not cancel-able
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected List<GlobalApplicationConfigModel> doInBackground(Void... arg0) {
			try {
				ClientResource cr = new ClientResource(Login.SERVER_LOCATION);
				ISociaHubResource resource = cr.wrap(ISociaHubResource.class);
				GlobalApplicationConfigModelList models = resource.getSupportedServiceProviders();
				if (models != null) {
					return models.getGlobalAppConfigModels();
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
			return Collections.emptyList();
		}
		
		
		protected void onPostExecute(List<GlobalApplicationConfigModel> models) {
			try {
				authDialog.dismiss();
				if(models != null && models.isEmpty() == false){
					List<GlobalApplicationConfigModel> sources = 
						new ArrayList<GlobalApplicationConfigModel>();
					List<GlobalApplicationConfigModel> targets = 
						new ArrayList<GlobalApplicationConfigModel>();
					
					for (GlobalApplicationConfigModel model : models) {
						if (model instanceof GlobalSourceApplicationServiceModel) {
							sources.add(model);
						} else {
							targets.add(model);
						}
					}
					AuthorizeActivity.this.servicesAdapter.addSection("Source Services", new ItemAdapter(
							AuthorizeActivity.this, R.layout.row, 
							sources));

					AuthorizeActivity.this.servicesAdapter.addSection("Target Services", new ItemAdapter(
							AuthorizeActivity.this, R.layout.row, targets));
					authorizeServiceListView.setAdapter(AuthorizeActivity.this.servicesAdapter);
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
		}

	}

}

