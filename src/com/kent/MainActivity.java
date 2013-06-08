package com.kent;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.kent.adapters.FeedItemAdapter;
import com.kent.interfaces.TaskListener;
import com.kent.listeners.FeedItemClickListener;
import com.kent.models.Error;
import com.kent.models.Feed;
import com.kent.tasks.FeedTask;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;

public class MainActivity extends SherlockActivity implements TaskListener {
  public ActionBar actionBar = null;
  public ListView listViewfeedList = null;
  public MenuItem refreshAction = null;
  public SharedPreferences preferences;
  
  public FeedItemAdapter feedItemAdapter = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_PROGRESS);
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    
    super.onCreate(savedInstanceState);
    
    this.preferences = getSharedPreferences("KentPreferences", MODE_PRIVATE);
    
    if (this.preferences.contains("auth_token")) {
      showMain();
    }
    else {
      showAuth();
    }
  }
  
  @Override
  protected void onStart() {
    this.feedItemAdapter = new FeedItemAdapter(this, R.layout.list_item_feed, new ArrayList<Feed>());
    this.feedItemAdapter.setNotifyOnChange(true);
    
    if (!this.listViewfeedList.equals(null)) {
      this.listViewfeedList.setAdapter(feedItemAdapter);
      this.listViewfeedList.setOnItemClickListener(new FeedItemClickListener());
    }
    
    loadFeeds();
    super.onStart();
  }
  
  private void showMain() {
    setContentView(R.layout.activity_main);
    
    this.listViewfeedList = (ListView) this.findViewById(R.id.feedList);
    
    this.actionBar = this.getSupportActionBar();
    this.actionBar.setTitle("KENT");
    this.actionBar.setHomeButtonEnabled(true);
    // this.actionBar.setDisplayHomeAsUpEnabled(true); // Only for children activities 
    this.actionBar.setDisplayShowHomeEnabled(true);
    
    this.setSupportProgressBarIndeterminateVisibility(false);
  }
  
  private void showAuth() {
    Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    this.startActivity(intent);
    this.finish();
  }
  
  private void loadFeeds() {
    setSupportProgressBarIndeterminateVisibility(true);
    if (refreshAction != null) {
      refreshAction.setVisible(false);
    }
    
    FeedTask feedTask = new FeedTask(this);
    feedTask.execute("http://kent.herokuapp.com/feeds.json?auth_token=" + preferences.getString("auth_token", null));
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    refreshAction = menu.add("Refresh");
    refreshAction.setIcon(R.drawable.ic_action_refresh);
    refreshAction.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    
    refreshAction.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
      
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        loadFeeds();
        return false;
      }
    });
    
    MenuItem addAction = menu.add("Add");
    addAction.setIcon(R.drawable.ic_action_add);
    addAction.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    
//    SubMenu subMenu1 = menu.addSubMenu("Action Item");
//    subMenu1.add("Sample");
//    subMenu1.add("Menu");
//    subMenu1.add("Items");
//
//    MenuItem subMenu1Item = subMenu1.getItem();
//    subMenu1Item.setIcon(R.drawable.refresh);
//    subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

    return super.onCreateOptionsMenu(menu);
  }

  @Override
  @SuppressWarnings("unchecked")
  public void onTaskCompleted(Object result) {
    ArrayList<Feed> feedList = (ArrayList<Feed>) result;

    this.feedItemAdapter.clear();
    
    for (Feed feed : feedList) {
      this.feedItemAdapter.add(feed);
    }
    
    refreshAction.setVisible(true);
    setSupportProgressBarIndeterminateVisibility(false);
  }

  @Override
  public void onTaskError(Object result) {
    Error error = (Error) result;
    Toast.makeText(getApplicationContext(), error.message, Toast.LENGTH_LONG).show();
    
    refreshAction.setVisible(true);
    setSupportProgressBarIndeterminateVisibility(false);
    
    if (error.code.equals("api_error") && error.message.equals("Invalid authentication token.")) {
      showAuth();
    }
  }

  @Override
  public void onTaskCancelled(Object result) {
    Toast.makeText(getApplicationContext(), (String) result, Toast.LENGTH_LONG).show();
    
    refreshAction.setVisible(true);
    setSupportProgressBarIndeterminateVisibility(false);
  }
}
