package com.chigbrowsoftware.kgo.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.sql.Timestamp;

@Entity(foreignKeys = {
    @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user",
        onDelete = ForeignKey.CASCADE)
    })

public class Activity {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "activity_id")
  private long id;

  @ColumnInfo(name = "user_id", index = true)
  private long  user;

  private Timestamp timestamp;

  private long time;

  private boolean result;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  public long getUser() {
    return user;
  }

  public void setUser(long user) {
    this.user = user;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public boolean isResult() {
    return result;
  }

  public void setResult(boolean result) {
    this.result = result;
  }
}