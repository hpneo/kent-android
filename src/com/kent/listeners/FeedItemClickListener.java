package com.kent.listeners;

import com.kent.FeedActivity;
import com.kent.models.Feed;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

public class FeedItemClickListener implements AdapterView.OnItemClickListener {

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Feed feed = (Feed) parent.getItemAtPosition(position);
    Intent intent = new Intent(parent.getContext(), FeedActivity.class);
    intent.putExtra("feed", feed);
    
    parent.getContext().startActivity(intent);
  }

}
