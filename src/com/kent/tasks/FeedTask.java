package com.kent.tasks;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.kent.models.Feed;
import com.kent.utils.RestClient.RequestMethod;
import com.kent.utils.RestClient;
import com.kent.MainActivity;

import android.os.AsyncTask;

public class FeedTask extends AsyncTask<String, Void, String> {
  private MainActivity activityCaller;
  
  public FeedTask(MainActivity activityCaller) {
    this.activityCaller = activityCaller;
  }

  @Override
  protected String doInBackground(String... urls) {
    try {
      return loadFromNetwork(urls[0]);
    } catch (Exception e) {
      return "An error occurred while trying connecting to the server";
    }
  }
  
  @Override
  protected void onPostExecute(String result) {
    super.onPostExecute(result);
    
    try {
      ArrayList<Feed> feedList = Feed.fromJSON(new JSONArray(result));
      
      activityCaller.populateFeedList(feedList);
    } catch (JSONException e) {
      System.out.println(e.getMessage());
    }
  }
  
  private String loadFromNetwork(String url) throws Exception {
    RestClient client = new RestClient(url);
    client.Execute(RequestMethod.GET);
    
    return client.getResponse();
  }

}
