package com.chigbrowsoftware.kgo;

import android.app.Application;
import com.chigbrowsoftware.kgo.model.User;
import com.chigbrowsoftware.kgo.model.database.ActivitiesDatabase;
import com.facebook.stetho.Stetho;

public class ApplicationKiddoGo extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
    new Thread ( ()-> {
      User user = new User();
     user.setName("Noam");
      ActivitiesDatabase.getInstance(this).getUserDao().insert(user);
    }).start();
  }
}
