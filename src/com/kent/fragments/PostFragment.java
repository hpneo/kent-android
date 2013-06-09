package com.kent.fragments;

import com.kent.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.kent.models.Post;
import com.kent.utils.CachedData;

public class PostFragment extends SherlockFragment {
  public Post post = null;
  public String content_layout = null;
  
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
    TextView textViewPostAuthor = (TextView) view.findViewById(R.id.textview_post_author);
    WebView webViewPostContent = (WebView) view.findViewById(R.id.webview_post_content);
    
    if (this.post == null) {
      textViewPostTitle.setText("No post");
    }
    else {
      textViewPostTitle.setText(this.post.title);
      textViewPostAuthor.setText(this.post.author);
      
      if (CachedData.hasString("post_content_layout")) {
        String content = CachedData.getString("post_content_layout").replace("{{post_content}}", this.post.description);
        webViewPostContent.loadDataWithBaseURL("", content, "text/html", "utf-8", "");
      }
    }
    
    return view;
  }
}
