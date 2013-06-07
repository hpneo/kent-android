package com.kent;

import java.util.ArrayList;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.kent.adapters.PostsPagerAdapter;
import com.kent.interfaces.TaskListener;
import com.kent.models.Error;
import com.kent.models.Feed;
import com.kent.models.Post;
import com.kent.tasks.PostTask;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

public class FeedActivity extends SherlockFragmentActivity implements TaskListener {
  public ActionBar actionBar = null;
  private Feed feed = null;
  private ViewPager viewPagerFeedPosts = null;
  private PostsPagerAdapter viewPagerFeedPostsAdapter = null;
  public SharedPreferences preferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feed);
    
    this.preferences = getSharedPreferences("KentPreferences", MODE_PRIVATE);
    
    feed = (Feed) getIntent().getExtras().get("feed");
    
    this.actionBar = this.getSupportActionBar();
    this.actionBar.setTitle(feed.title);
    this.actionBar.setHomeButtonEnabled(true);
    this.actionBar.setDisplayHomeAsUpEnabled(true);
    this.actionBar.setDisplayShowHomeEnabled(true);
    
    this.viewPagerFeedPosts = (ViewPager) this.findViewById(R.id.viewPagerFeedPosts);
    
    this.viewPagerFeedPostsAdapter = new PostsPagerAdapter(getSupportFragmentManager());
    this.viewPagerFeedPostsAdapter.posts = new ArrayList<Post>();
    
    this.viewPagerFeedPosts.setAdapter(this.viewPagerFeedPostsAdapter);
    
    setSupportProgressBarIndeterminateVisibility(true);
    PostTask postTask = new PostTask(this);
    postTask.execute("http://kent.herokuapp.com/feeds/" + feed.id + "/posts.json?only_unread=true&auth_token=" + preferences.getString("auth_token", null));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case android.R.id.home:
      NavUtils.navigateUpFromSameTask(this);
      break;
    }
    
    return true;
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public void onTaskCompleted(Object result) {
    this.viewPagerFeedPostsAdapter.posts = (ArrayList<Post>) result;
    this.viewPagerFeedPostsAdapter.notifyDataSetChanged();
    
    // refreshAction.setVisible(true);
    setSupportProgressBarIndeterminateVisibility(false);
  }

  @Override
  public void onTaskError(Object result) {
    Error error = (Error) result;
    Toast.makeText(getApplicationContext(), error.message, Toast.LENGTH_LONG).show();
    
    // refreshAction.setVisible(true);
    setSupportProgressBarIndeterminateVisibility(false);
  }

  @Override
  public void onTaskCancelled(Object result) {
    Toast.makeText(getApplicationContext(), (String) result, Toast.LENGTH_LONG).show();
    
    // refreshAction.setVisible(true);
    setSupportProgressBarIndeterminateVisibility(false);
  }
}
