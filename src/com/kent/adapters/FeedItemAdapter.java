package com.kent.adapters;

import java.util.ArrayList;

import com.kent.R;
import com.kent.models.Feed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FeedItemAdapter extends ArrayAdapter<Feed> {
  private ArrayList<Feed> feeds;
  
  public FeedItemAdapter(Context context, int textViewResourceId, ArrayList<Feed> collection) {
    super(context, textViewResourceId, collection);
    this.feeds = collection;
  }
  
  @Override
  public View getView(int position, View view, ViewGroup parent) {
    Feed feed = this.feeds.get(position);
    
    if(view == null) {
      LayoutInflater view_inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = view_inflater.inflate(R.layout.list_item_feed, null);
    }
    
    TextView feedName = (TextView) view.findViewById(R.id.list_item_feed_name);
    TextView feedPostsCounter = (TextView) view.findViewById(R.id.list_item_feed_posts_counter);
    
    if (!feed.equals(null)) {
      feedName.setText(feed.title);
      feedPostsCounter.setText(feed.postsCounter + "");
    }
    
    return view;
  }
}
