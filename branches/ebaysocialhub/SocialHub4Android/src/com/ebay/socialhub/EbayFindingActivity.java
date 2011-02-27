/**
 * 
 */
package com.ebay.socialhub;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ebay.sdk.sample.views.WebImageView;
import com.googlecode.flickr2twitter.impl.ebay.EbayItem;
import com.googlecode.flickr2twitter.impl.ebay.FindItemsDAO;

/**
 * @author Toby Yu(yuyang226@gmail.com)
 *
 */
public class EbayFindingActivity extends Activity {
	public static final String TAG = "SocialHub";
	
	private ListView itemsListView;
	private TextView txtKeywords;
	private Button btnSearch;

	/**
	 * 
	 */
	public EbayFindingActivity() {
		super();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ebay_finding);
		
		this.btnSearch = (Button) findViewById(R.id.cmd_search);
		this.txtKeywords = (TextView) findViewById(R.id.edit_input);
		this.itemsListView = (ListView) this.findViewById(R.id.ebayItemList);
		this.itemsListView.setTextFilterEnabled(true);

		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new EbaySearchTask().execute();
			}
		});
	}
	
	private class EbaySearchTask extends AsyncTask<Void, Void, List<EbayItem>> {
		ProgressDialog authDialog;

		@Override
		protected void onPreExecute() {
			authDialog = ProgressDialog.show(EbayFindingActivity.this, "Searching eBay",
					"Searching eBay...", true, // indeterminate
					// duration
					false); // not cancel-able
		}

		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected List<EbayItem> doInBackground(Void... arg0) {
			try {
				String keywords = txtKeywords.getText().toString();
				return new FindItemsDAO().findItemsByKeywordsFromProduction(keywords, 10);
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
			
			return Collections.emptyList();
		}

		protected void onPostExecute(List<EbayItem> items) {
			try {
				authDialog.dismiss();
				
		        EbayFindingActivity.this.itemsListView.setAdapter(
		        		new EbayItemAdapter(EbayFindingActivity.this, R.layout.row, items));
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
		}

	}
	
	private class EbayItemAdapter extends ArrayAdapter<EbayItem> {

		private List<EbayItem> items;

        public EbayItemAdapter(Context context, int textViewResourceId,
                List<EbayItem> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }

        /*
         * (non-Javadoc)
         * @see android.widget.BaseAdapter#getViewTypeCount()
         */
        @Override
        public int getViewTypeCount() {
            return items != null ? items.size() : 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.ebay_row, null);
            }
            EbayItem ebayItem = items.get(position);
            if (ebayItem != null) {
            	 TextView title = (TextView) v.findViewById(R.id.title);
                 
                 if (title != null) {
                 	title.setText(ebayItem.getTitle());                            
                 }
                 
                 WebImageView image = (WebImageView) v.findViewById(R.id.gallery_icon);
                 if (image != null){
                 	if (ebayItem.getGalleryURL() != null) {
                 		image.setImageUrl(ebayItem.getGalleryURL());
                 		image.loadImage();
                 	} else {
                 		image.setNoImageDrawable(R.drawable.placeholder);
                 	}
                 }
                 
                 // once clicked, navigate to item details page
                // v.setOnClickListener(new OnItemClickListener(item.itemId, v.getContext()));
            }
            return v;
        }
    }
	
	

}
