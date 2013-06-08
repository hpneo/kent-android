package com.kent;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.kent.adapters.PostsPagerAdapter;
import com.kent.interfaces.TaskListener;
import com.kent.models.Error;
import com.kent.models.Feed;
import com.kent.models.Post;
import com.kent.tasks.PostTask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

public class FeedActivity extends SherlockFragmentActivity implements TaskListener {
  public ActionBar actionBar = null;
  private ViewPager viewPagerFeedPosts = null;
  public MenuItem refreshAction = null;
  public SubMenu menuAction = null;
  private PostsPagerAdapter viewPagerFeedPostsAdapter = null;
  
  private Feed feed = null;
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
    
    String content_layout = "{{post_content}}";
    
    try {
      InputStream input = getAssets().open("html/post_content.html");
      int size = input.available();
      
      byte[] buffer = new byte[size];
      input.read(buffer);
      input.close();
      
      content_layout = new String(buffer);
      
    } catch (IOException e) {
    }
    
    this.viewPagerFeedPostsAdapter = new PostsPagerAdapter(getSupportFragmentManager());
    this.viewPagerFeedPostsAdapter.posts = new ArrayList<Post>();
    this.viewPagerFeedPostsAdapter.content_layout = content_layout;
    
    this.viewPagerFeedPosts.setAdapter(this.viewPagerFeedPostsAdapter);
    
    setSupportProgressBarIndeterminateVisibility(true);
    PostTask postTask = new PostTask(this);
    postTask.execute("http://kent.herokuapp.com/feeds/" + feed.id + "/posts.json?only_unread=true&auth_token=" + preferences.getString("auth_token", null));
  }
  
  public Post currentPost() {
    Post post = this.viewPagerFeedPostsAdapter.posts.get(this.viewPagerFeedPosts.getCurrentItem());
    return post;
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    refreshAction = menu.add("Refresh");
    refreshAction.setIcon(R.drawable.ic_action_refresh);
    refreshAction.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    
    MenuItem shareAction = menu.add("Share");
    shareAction.setIcon(R.drawable.ic_action_share);
    shareAction.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    shareAction.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
      
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        if (viewPagerFeedPostsAdapter.posts.size() > 0) {
          Intent sharingIntent = new Intent(Intent.ACTION_SEND);
          sharingIntent.setType("text/plain");
          sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
          
          String text = "I'm reading \"" + currentPost().title + "\": " + currentPost().link;
          
          sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
          startActivity(sharingIntent);
        }
        
        return false;
      }
    });
    
    menuAction = menu.addSubMenu("Menu");
    menuAction.getItem().setIcon(R.drawable.ic_action_menu);
    menuAction.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    
    menuAction.add("Mark all as read");
    
    return super.onCreateOptionsMenu(menu);
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
    
    refreshAction.setVisible(true);
    setSupportProgressBarIndeterminateVisibility(false);
  }

  @Override
  public void onTaskError(Object result) {
    Error error = (Error) result;
    Toast.makeText(getApplicationContext(), error.message, Toast.LENGTH_LONG).show();
    
    refreshAction.setVisible(true);
    setSupportProgressBarIndeterminateVisibility(false);
  }

  @Override
  public void onTaskCancelled(Object result) {
    Toast.makeText(getApplicationContext(), (String) result, Toast.LENGTH_LONG).show();
    
    refreshAction.setVisible(true);
    setSupportProgressBarIndeterminateVisibility(false);
  }
}
