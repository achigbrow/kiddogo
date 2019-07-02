package com.chigbrowsoftware.kgo.model.entity;

import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.preference.PreferenceManager;
import androidx.room.TypeConverter;
import java.util.jar.Attributes.Name;

@Entity
public class UserEntity {

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


  @NonNull
  @Override
  public String toString() {
    return name;
  }
}
