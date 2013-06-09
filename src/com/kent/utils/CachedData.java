package com.kent.utils;

import java.util.*;

public class CachedData {
  public static HashMap<String, ArrayList<?>> data = new HashMap<String, ArrayList<?>>();
  public static HashMap<String, String> stringData = new HashMap<String, String>();
  
  public static ArrayList<?> get(String key) {
    if (has(key)) {
      return data.get(key);
    }
    else {
      return null;
    }
  }
  
  public static void set(String key, ArrayList<?> value) {
    data.put(key, value);
  }
  
  public static boolean has(String key) {
    return data.containsKey(key);
  }
  
  public static String getString(String key) {
    if (hasString(key)) {
      return stringData.get(key);
    }
    else {
      return null;
    }
  }
  
  public static void setString(String key, String value) {
    stringData.put(key, value);
  }
  
  public static boolean hasString(String key) {
    return stringData.containsKey(key);
  }
}
