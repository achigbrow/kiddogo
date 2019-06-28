package com.chigbrowsoftware.kgo.model.database;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.chigbrowsoftware.kgo.model.Activity;
import com.chigbrowsoftware.kgo.model.User;
import com.chigbrowsoftware.kgo.model.dao.ActivityDao;
import com.chigbrowsoftware.kgo.model.dao.UserDao;
import java.util.Date;

@Database(entities = {Activity.class, User.class}, version = 1)
public abstract class ActivitiesDatabase extends RoomDatabase {

  public abstract ActivityDao activityDao();

  public abstract UserDao userDao();

  private static ActivitiesDatabase INSTANCE;

  public static ActivitiesDatabase getInstance(Context context) {
    if (INSTANCE == null){
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

  private static class PopulateDbTask extends AsyncTask<Void, Void, Void> {

    private final ActivitiesDatabase db;

    PopulateDbTask(ActivitiesDatabase db) {
      this.db = db;
    }

    @Override
    protected Void doInBackground(Void... voids) {
      User user1 = new User();
      user1.setName("Noam");
      db.userDao().insert(user1);
      return null;
    }
  }

}