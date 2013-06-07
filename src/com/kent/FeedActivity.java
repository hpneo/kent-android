package com.kent;

import java.util.ArrayList;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.kent.adapters.PostsPagerAdapter;
import com.kent.models.Feed;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;

public class FeedActivity extends SherlockFragmentActivity {
  public ActionBar actionBar = null;
  private Feed feed = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feed);
    feed = (Feed) getIntent().getExtras().get("feed");
    
    this.actionBar = this.getSupportActionBar();
    this.actionBar.setTitle(feed.title);
    this.actionBar.setHomeButtonEnabled(true);
    this.actionBar.setDisplayHomeAsUpEnabled(true);
    this.actionBar.setDisplayShowHomeEnabled(true);
    
    ViewPager viewPagerFeedPosts = (ViewPager) this.findViewById(R.id.viewPagerFeedPosts);
    
    PostsPagerAdapter viewPagerFeedPostsAdapter = new PostsPagerAdapter(getSupportFragmentManager());
    viewPagerFeedPostsAdapter.posts = new ArrayList<String>();
    viewPagerFeedPostsAdapter.posts.add("Post #1");
    viewPagerFeedPostsAdapter.posts.add("Post #2");
    viewPagerFeedPostsAdapter.posts.add("Post #3");
    viewPagerFeedPostsAdapter.posts.add("Post #4");
    viewPagerFeedPostsAdapter.posts.add("Post #5");
    
    viewPagerFeedPosts.setAdapter(viewPagerFeedPostsAdapter);
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
}
