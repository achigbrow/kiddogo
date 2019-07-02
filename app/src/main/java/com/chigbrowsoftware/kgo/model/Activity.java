package com.chigbrowsoftware.kgo.model;

public class Activity {

  private long userId;
  private int timeLimit;

  public Activity(long userId, int timeLimit) {
    this.userId = userId;
    this.timeLimit = timeLimit;
  }

  public long getUserId() {
    return userId;
  }

  public int getTimeLimit() {
    return timeLimit;
  }
}
