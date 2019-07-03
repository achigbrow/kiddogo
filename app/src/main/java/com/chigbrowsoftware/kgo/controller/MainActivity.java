package com.chigbrowsoftware.kgo.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import com.chigbrowsoftware.kgo.R;
import com.chigbrowsoftware.kgo.model.Activity;
import com.chigbrowsoftware.kgo.model.entity.UserEntity;
import com.chigbrowsoftware.kgo.model.database.ActivitiesDatabase;
import com.chigbrowsoftware.kgo.model.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements OnSharedPreferenceChangeListener {


  private Button btn;

  private TextView mTextMessage;
  private SharedPreferences preferences;
  private int timeLimit;
  private UserEntity user;
  private long userId;
  private Activity activity;


  private static TextView clockDisplay;
  private Timer activityTimer;
  private Timer clockTimer;
  private String activityTimeElapsedKey;
  private String clockFormat;
  private long activityTimerStart;
  private long activityTimeElapsed;

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
      = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      boolean handled = true;
      Intent intent;

      switch (item.getItemId()) {
        case R.id.navigation_play:
          return true;
        case R.id.navigation_dashboard:
          mTextMessage.setText(R.string.title_dashboard);
          return true;
        case R.id.navigation_settings:
          intent = new Intent(getApplicationContext(), SettingsActivity.class);
          startActivity(intent);
          return true;
      }
      return handled;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    clockDisplay = findViewById(R.id.clock_display);
    getUser().observe(this, this::buildButton);
    mTextMessage = findViewById(R.id.message);
    preferences = PreferenceManager.getDefaultSharedPreferences(this);
    preferences.registerOnSharedPreferenceChangeListener(this);
    activityTimeElapsedKey = getString(R.string.activity_time_elapsed_key);
    clockFormat = getString(R.string.clock_format);
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    readSettings();
  }

  private void readSettings() {
    Resources res = getResources();
    String userName = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        .getString("username", "default username");
    //userName = preferences.getString("username", "username");
    timeLimit = preferences.getInt("timer", 15);
    UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    user = new UserEntity();
    user.setName(userName);
    userViewModel.addUser(user);
    userId = user.getId();
    btn.setText(user.getName());
  }


  //TODO Build the ability to create 4 buttons of different colors for up to 4 users added.
  //TODO Tie the button opened activity to the user.
  private void buildButton(UserEntity user) {
    if (user != null) {
      btn = findViewById(R.id.button);
      btn.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent pageViewer =
              new Intent(MainActivity.this, ViewPagerActivity.class);
          startActivity(pageViewer);
          initActivity();
        }
      });
      btn.setText(user.getName());
    }
  }

  private LiveData<UserEntity> getUser() {
    ActivitiesDatabase db = ActivitiesDatabase.getInstance(getApplication());
    return db.userDao().getLastUser();
  }

  private void initActivity() {
    activity = new Activity(userId, timeLimit);
    activityTimeElapsed = 0;
    activityTimerStart = System.currentTimeMillis();
    startActivityTimer();
    updateClock();
  }

  //TODO create a timeout task and tie to activity timer
  private void startActivityTimer() {
    activityTimer = new Timer();
    activityTimerStart = System.currentTimeMillis();
    clockTimer = new Timer();
    clockTimer.schedule(new ClockTimerTask(), 0, 100);
  }

  private class ClockTimerTask extends TimerTask {

    @Override
    public void run() {
      runOnUiThread(() -> MainActivity.this.updateClock());
    }

  }

    private void  updateClock() {
      timeLimit = preferences.getInt("timer", 15);
      long remaining = (timeLimit * 60000) - (System.currentTimeMillis() - activityTimerStart);
      int minutes;
      double seconds;

      if (remaining > 0) {
        minutes = (int) (remaining/60000);
        seconds = (remaining % 60000) / 1000.0;
    } else {
        minutes = timeLimit;
        seconds = 0;
      }
      clockDisplay.setText(String.format(clockFormat, minutes, seconds));
  }

}
