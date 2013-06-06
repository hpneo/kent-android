package com.kent.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Feed implements Serializable {
  private static final long serialVersionUID = 1L;

  public int id;
  public String title;
  public String description;
  public String url;
  public String homeURL;
  public int postsCounter;
  
  public static Feed fromJSON(JSONObject jsonItem) throws JSONException {
    Feed item = new Feed();
    
    item.id = jsonItem.getInt("id");
    item.title = jsonItem.getString("title");
    item.description = jsonItem.getString("description");
    item.url = jsonItem.getString("url");
    item.homeURL = jsonItem.getString("home_url");
    item.postsCounter = 100;// jsonItem.getInt("posts_counter");
    
    return item;
  }
  
  public static ArrayList<Feed> fromJSON(JSONArray jsonCollection) throws JSONException {
    ArrayList<Feed> collection = new ArrayList<Feed>();
    
    int count = jsonCollection.length(),
        i = 0;
    
    for(i = 0; i < count; i++) {
      Feed item = Feed.fromJSON((JSONObject) jsonCollection.get(i));
      
      collection.add(item);
    }
    
    return collection;
  }

}
