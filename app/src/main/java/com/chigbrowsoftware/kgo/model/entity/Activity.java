package com.chigbrowsoftware.kgo.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.chigbrowsoftware.kgo.model.database.TimestampConverter;
import java.util.Date;


@Entity(foreignKeys = {
    @ForeignKey(entity = UserEntity.class, parentColumns = "user_id", childColumns = "user_id",
        onDelete = ForeignKey.CASCADE)
})

/** Creates the 'table' for an Activity.*/
public class Activity {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "activity_id")
  private long id;

  @ColumnInfo(name = "user_id", index = true)
  private long user;

  @TypeConverters(TimestampConverter.class)
  private Date timestamp;

  private long time;

  private boolean result;

  /**
   * Retrieves id from table.
   */
  public long getId() {
    return id;
  }

  /**
   * Sets the id. I tried deleting this because id is set automatically, but I got an error.
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Retrieves the timestamp of the activity.
   */
  public Date getTimestamp() {
    return timestamp;
  }

  /**
   * Sets the timestamp of the activity.
   */
  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Retrieves the user associated with an activity.
   */
  public long getUser() {
    return user;
  }

  /**
   * Set the user associated with an activity.
   */
  public void setUser(long user) {
    this.user = user;
  }

  /**
   * Gets the length of time it took to complete the activity.
   */
  public long getTime() {
    return time;
  }

  /**
   * Sets the length of time it took to complete an activity.
   */
  public void setTime(long time) {
    this.time = time;
  }

  /**
   * Retrieves the boolean result (was the activity completed in time) of an activity.
   */
  public boolean isResult() {
    return result;
  }

  /**
   * Sets the boolean result (was the activity completed in time) of an activity.
   */
  public void setResult(boolean result) {
    this.result = result;
  }

//  Copyright [2019] [Alana Chigbrow]
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
}
