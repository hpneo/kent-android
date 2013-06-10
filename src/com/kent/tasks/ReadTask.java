package com.kent.tasks;

import com.kent.utils.RestClient;
import com.kent.utils.RestClient.RequestMethod;

import android.os.AsyncTask;

public class ReadTask extends AsyncTask<String, Void, Integer> {

  @Override
  protected Integer doInBackground(String... urls) {
    try {
      RestClient client = new RestClient(urls[0]);
      client.Execute(RequestMethod.POST);
      
      System.out.println("ReadTask : " + client.getResponse());
      
      return client.getResponseCode();
    } catch (Exception e) {
      return null;
    }
  }

}
