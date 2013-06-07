package com.kent.adapters;

import java.util.ArrayList;

import com.kent.fragments.PostFragment;
import com.kent.models.Post;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PostsPagerAdapter extends FragmentPagerAdapter {
  public ArrayList<String> posts;
  
  public PostsPagerAdapter(FragmentManager fragmentManager) {
    super(fragmentManager);
  }

  @Override
  public Fragment getItem(int position) {
    String postString = this.posts.get(position);
    
    Post post = new Post();
    post.title = postString;
    
    return PostFragment.newInstance(post);
  }

  @Override
  public int getCount() {
    return posts.size();
  }
  
  @Override
  public String getPageTitle(int position) {
      return this.posts.get(position);
  }
}
