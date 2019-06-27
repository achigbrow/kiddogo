package com.chigbrowsoftware.kgo.model;

import android.content.res.Resources;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.preference.PreferenceManager;

@Entity
public class User {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "user_id")
  private long id;

  private String name;

  public long getId() {

    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {

    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
