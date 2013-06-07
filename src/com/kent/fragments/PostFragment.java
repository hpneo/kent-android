package com.kent.fragments;

import com.kent.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.kent.models.Post;

public class PostFragment extends SherlockFragment {
  public Post post = null;
  
  public static PostFragment newInstance(Post post) {
    PostFragment postFragment = new PostFragment();
    
    Bundle args = new Bundle();
    args.putSerializable("post", post);
    postFragment.setArguments(args);
    
    return postFragment;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.post = getArguments() != null ? (Post) getArguments().get("post") : null;
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_post, container, false);
    
    TextView textViewPostTitle = (TextView) view.findViewById(R.id.textview_post_title);
    
    if (this.post == null) {
      textViewPostTitle.setText("No post");
    }
    else {
      textViewPostTitle.setText(this.post.title);
    }
    
    return view;
  }
}
