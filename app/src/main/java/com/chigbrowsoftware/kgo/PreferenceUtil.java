package com.chigbrowsoftware.kgo;

import android.content.SharedPreferences;
import android.content.res.Resources;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import com.chigbrowsoftware.kgo.model.entity.UserEntity;

public class PreferenceUtil extends AppCompatActivity {

  private SharedPreferences preferences;
  private static int timeLimit;
  private UserEntity user;


  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    readSettings();
  }

  public void readSettings() {
    Resources res = getResources();
    String userName = PreferenceManager.getDefaultSharedPreferences(this)
        .getString("username", "default username");
    timeLimit = preferences.getInt("timer", 15);
  }

  public int getTimeLimit() {
    Resources res = getResources();
    timeLimit = preferences.getInt("timer", 15);
    return timeLimit;
  }

  public SharedPreferences getPreferences() {
    return preferences;
  }

  public void setPreferences(SharedPreferences preferences) {
    this.preferences = preferences;
  }


}
