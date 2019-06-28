package com.chigbrowsoftware.kgo;

import android.app.Application;
import com.chigbrowsoftware.kgo.model.database.ActivitiesDatabase;
import com.facebook.stetho.Stetho;

public class KiddoGoApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
    new Thread(()-> ActivitiesDatabase.getInstance(this).userDao().delete()).start();

  }

}
