package com.chigbrowsoftware.kgo;

import android.app.Application;
import com.chigbrowsoftware.kgo.model.database.ActivitiesDatabase;
import com.chigbrowsoftware.kgo.service.GoogleSignInService;
import com.facebook.stetho.Stetho;

public class KiddoGoApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
    GoogleSignInService.setContext(this);
    new Thread(()-> ActivitiesDatabase.getInstance(this).userDao().delete()).start();

  }

}
