package com.kent.interfaces;

public interface TaskListener {
  void onTaskCompleted(Object result);
  void onTaskError(Object result);
  void onTaskCancelled(Object result);
}
