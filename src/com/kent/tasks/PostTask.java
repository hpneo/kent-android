package com.kent.tasks;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kent.interfaces.TaskListener;
import com.kent.models.Error;
import com.kent.models.Post;
import com.kent.utils.RestClient;
import com.kent.utils.RestClient.RequestMethod;

import android.os.AsyncTask;

public class PostTask extends AsyncTask<String, Void, String> {
  private TaskListener activityCaller;
  
  public PostTask(TaskListener activityCaller) {
    this.activityCaller = activityCaller;
  }

  @Override
  protected String doInBackground(String... urls) {
    try {
      return loadFromNetwork(urls[0]);
    } catch (Exception e) {
      activityCaller.onTaskError(new Error("network_error", "An error occured while connecting to the server"));
      return null;
    }
  }
  
  @Override
  protected void onCancelled() {
    activityCaller.onTaskCancelled("Task cancelled");
  }
  
  @Override
  protected void onPostExecute(String result) {
    super.onPostExecute(result);
    
    try {
      if (!result.equals(null)) {
        ArrayList<Post> postList = Post.fromJSON(new JSONArray(result));
        
        activityCaller.onTaskCompleted(postList);
      }
    } catch (JSONException e) {
      try {
        JSONObject message = new JSONObject(result);
        
        if (message.has("error")) {
          activityCaller.onTaskError(new Error("api_error", message.getString("error")));
        }
      } catch (JSONException e1) {
        activityCaller.onTaskError(new Error("json_error", "An error occured while fetching the data"));
      }
    }
  }
  
  private String loadFromNetwork(String url) throws Exception {
    RestClient client = new RestClient(url);
    client.Execute(RequestMethod.GET);
    
    return client.getResponse();
  }

}
