package com.kent.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Post implements Serializable {
  private static final long serialVersionUID = 1L;

  public int id;
  public String title;
  public String author;
  public String description;
  public String link;
  public boolean read;
  public long publishedAt;
  
  public static Post fromJSON(JSONObject jsonItem) throws JSONException {
    Post item = new Post();
    
    item.id = jsonItem.getInt("id");
    item.title = jsonItem.getString("title");
    item.author = jsonItem.getString("author");
    item.description = jsonItem.getString("description");
    item.link = jsonItem.getString("link");
    item.read = jsonItem.getBoolean("read");
    item.publishedAt = jsonItem.getLong("published_at_timestamp");
    
    return item;
  }
  
  public static ArrayList<Post> fromJSON(JSONArray jsonCollection) throws JSONException {
    ArrayList<Post> collection = new ArrayList<Post>();
    
    int count = jsonCollection.length(),
        i = 0;
    
    for(i = 0; i < count; i++) {
      Post item = Post.fromJSON((JSONObject) jsonCollection.get(i));
      
      collection.add(item);
    }
    
    return collection;
  }
}
