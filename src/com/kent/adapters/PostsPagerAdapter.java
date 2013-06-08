package com.kent.adapters;

import java.util.ArrayList;

import com.kent.fragments.PostFragment;
import com.kent.models.Post;

import android.support.v4.app.*;

public class PostsPagerAdapter extends FragmentStatePagerAdapter {
  public ArrayList<Post> posts;
  public String content_layout;
  
  public PostsPagerAdapter(FragmentManager fragmentManager) {
    super(fragmentManager);
  }

  @Override
  public Fragment getItem(int position) {
    PostFragment fragment = PostFragment.newInstance(this.posts.get(position));
    fragment.content_layout = this.content_layout;
    return fragment;
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
