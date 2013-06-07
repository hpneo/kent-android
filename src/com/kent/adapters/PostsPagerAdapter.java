package com.kent.adapters;

import java.util.ArrayList;

import com.kent.fragments.PostFragment;
import com.kent.models.Post;

import android.support.v4.app.*;

public class PostsPagerAdapter extends FragmentStatePagerAdapter {
  public ArrayList<Post> posts;
  
  public PostsPagerAdapter(FragmentManager fragmentManager) {
    super(fragmentManager);
  }

  @Override
  public Fragment getItem(int position) {
    return PostFragment.newInstance(this.posts.get(position));
  }

  @Override
  public int getCount() {
    return posts.size();
  }
  
  @Override
  public CharSequence getPageTitle(int position) {
      return this.posts.get(position).title;
  }
}
