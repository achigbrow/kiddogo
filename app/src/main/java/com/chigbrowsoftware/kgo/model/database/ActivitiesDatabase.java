package com.chigbrowsoftware.kgo.model.database;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.chigbrowsoftware.kgo.model.dao.ActivityDao;
import com.chigbrowsoftware.kgo.model.dao.UserDao;
import com.chigbrowsoftware.kgo.model.entity.Activity;
import com.chigbrowsoftware.kgo.model.entity.UserEntity;

/**
 * Room database for app.
 */
@Database(entities = {Activity.class, UserEntity.class}, version = 1)
public abstract class ActivitiesDatabase extends RoomDatabase {

  private static ActivitiesDatabase INSTANCE;

  /**
   * Returns instance of the Activities Database.
   */
  public static ActivitiesDatabase getInstance(Context context) {
    if (INSTANCE == null) {
      INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ActivitiesDatabase.class,
          "activities_room").fallbackToDestructiveMigration().addCallback(new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
          super.onCreate(db);
          new PopulateDbTask(INSTANCE).execute();
        }
      }).build();
    }
    return INSTANCE;
  }

  /**
   * Provides access to ActivityDao.
   */
  public abstract ActivityDao activityDao();

  /**
   * Provides access to UserEntity Dao.
   */
  public abstract UserDao userDao();

  private static class PopulateDbTask extends AsyncTask<Void, Void, Void> {

    private final ActivitiesDatabase db;

    PopulateDbTask(ActivitiesDatabase db) {
      this.db = db;
    }

    /**
     * Preloads a default user to avoid null pointer issues. This "user" populates the instructions
     * in the Main Activity button when first launched.
     */
    @Override
    protected Void doInBackground(Void... voids) {
      UserEntity user1 = new UserEntity();
      user1.setName("Open Settings to change this to your kiddo's name.");
      db.userDao().insert(user1);
      return null;
    }
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