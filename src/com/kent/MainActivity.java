package com.kent;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.kent.models.Feed;
import com.kent.tasks.FeedTask;

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;

public class MainActivity extends SherlockActivity {
  public ActionBar actionBar = null;
  public MenuItem refreshAction = null;
  public SharedPreferences preferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_PROGRESS);
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    
    super.onCreate(savedInstanceState);
    
    preferences = getSharedPreferences("KentPreferences", MODE_PRIVATE);
    
    if (preferences.contains("auth_token")) {
      setContentView(R.layout.activity_main);
      
      this.actionBar = this.getSupportActionBar();
      this.actionBar.setTitle("KENT");
      this.actionBar.setHomeButtonEnabled(true);
      // this.actionBar.setDisplayHomeAsUpEnabled(true); // Only for children activities 
      this.actionBar.setDisplayShowHomeEnabled(true);
      
      this.setSupportProgressBarIndeterminateVisibility(false);
    }
    else {
      Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      this.startActivity(intent);
      this.finish();
    }
  }
  
  public void populateFeedList(ArrayList<Feed> feedList) {
    refreshAction.setVisible(true);
    setSupportProgressBarIndeterminateVisibility(false);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    refreshAction = menu.add("Refresh");
    refreshAction.setIcon(R.drawable.ic_action_refresh);
    refreshAction.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    
    refreshAction.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
      
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        setSupportProgressBarIndeterminateVisibility(true);
        item.setVisible(false);
        
        FeedTask feedTask = new FeedTask(MainActivity.this);
        feedTask.execute("http://kent.herokuapp.com/feeds.json?auth_token=" + preferences.getString("auth_token", null));
        
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
}
