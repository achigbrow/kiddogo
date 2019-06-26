package com.chigbrowsoftware.kgo.model.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.chigbrowsoftware.kgo.model.Activity;
import com.chigbrowsoftware.kgo.model.User;
import com.chigbrowsoftware.kgo.model.dao.ActivityDao;
import com.chigbrowsoftware.kgo.model.dao.UserDao;
import java.util.Date;

@Database(entities = {Activity.class, User.class}, version = 1)
public abstract class ActivitiesDatabase extends RoomDatabase {

  public abstract ActivityDao getActivityDao();

  public abstract UserDao getUserDao();

  private static ActivitiesDatabase INSTANCE;

  public static ActivitiesDatabase getInstance(Context context) {
    if (INSTANCE == null){
      INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ActivitiesDatabase.class,
          "activities_room").build();
    }
    return INSTANCE;
  }

}