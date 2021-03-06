
package com.kent;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.Window;
import com.kent.adapters.FeedItemAdapter;
import com.kent.interfaces.TaskListener;
import com.kent.listeners.FeedItemClickListener;
import com.kent.models.Error;
import com.kent.models.Feed;
import com.kent.tasks.FeedTask;
import com.kent.utils.CachedData;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;

public class MainActivity extends SherlockActivity implements TaskListener {
  public ActionBar actionBar = null;
  public ListView listViewfeedList = null;
  public MenuItem refreshAction = null;
  public SubMenu menuAction = null;
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
    CachedData.set("posts", null);
    if (CachedData.get("feeds") == null) {
      loadFeeds();
    }
    else {
      populateFeedAdapter();
    }
    
    super.onStart();
  }
  
  private void showMain() {
    setContentView(R.layout.activity_main);
    
    this.listViewfeedList = (ListView) this.findViewById(R.id.feedList);
    this.feedItemAdapter = new FeedItemAdapter(this, R.layout.list_item_feed, new ArrayList<Feed>());
    this.feedItemAdapter.setNotifyOnChange(true);

    if (!this.listViewfeedList.equals(null)) {
      this.listViewfeedList.setAdapter(feedItemAdapter);
      this.listViewfeedList.setOnItemClickListener(new FeedItemClickListener());
    }
    
    this.actionBar = this.getSupportActionBar();
    this.actionBar.setTitle("KENT");
    this.actionBar.setHomeButtonEnabled(true);
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

  @SuppressWarnings("unchecked")
  private void populateFeedAdapter() {
    this.feedItemAdapter.clear();
    ArrayList<Feed> feedList = (ArrayList<Feed>) CachedData.get("feeds");
    
    for (Feed feed : feedList) {
      this.feedItemAdapter.add(feed);
    }
    
    if (refreshAction != null) {
      refreshAction.setVisible(true);
    }
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    refreshAction = menu.add("Refresh");
    refreshAction.setIcon(R.drawable.ic_action_refresh);
    refreshAction.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    refreshAction.setVisible(true);
    
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
    
    menuAction = menu.addSubMenu("Menu");
    menuAction.getItem().setIcon(R.drawable.ic_action_menu);
    menuAction.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    
    menuAction.add("Mark all as read");

    return super.onCreateOptionsMenu(menu);
  }

  @Override
  @SuppressWarnings("unchecked")
  public void onTaskCompleted(Object result) {
    ArrayList<Feed> feedList = (ArrayList<Feed>) result;
    CachedData.set("feeds", feedList);
    
    populateFeedAdapter();

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
