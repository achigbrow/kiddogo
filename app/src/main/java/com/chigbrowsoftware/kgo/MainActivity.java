package com.chigbrowsoftware.kgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import com.chigbrowsoftware.kgo.controller.ResultsActivity;
import com.chigbrowsoftware.kgo.controller.SettingsActivity;
import com.chigbrowsoftware.kgo.fragments.TaskFragment;
import com.chigbrowsoftware.kgo.model.database.ActivitiesDatabase;
import com.chigbrowsoftware.kgo.model.entity.ActivityEntity;
import com.chigbrowsoftware.kgo.model.entity.UserEntity;
import com.chigbrowsoftware.kgo.model.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements OnSharedPreferenceChangeListener {


  public final static int NUM_PAGES = 6;
  public static long activityTimeElapsed;
  public static androidx.viewpager.widget.ViewPager pager;
  private static int timeLimit;
  private static TextView clockDisplay;
  public long userId;
  private Button btn;
  private TextView mTextMessage;
  private SharedPreferences preferences;
  private UserEntity user;
  private Timer activityTimer;
  private Timer clockTimer;
  private String activityTimeElapsedKey;
  private String clockFormat;
  private String completeTime;
  private long activityTimerStart;
  private boolean inActivity;
  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
      = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      boolean handled = true;
      Intent intent;

      switch (item.getItemId()) {
        case R.id.navigation_play:
          intent = new Intent(getApplicationContext(), MainActivity.class);
          startActivity(intent);
          return true;
        case R.id.navigation_dashboard:
          if (!inActivity){
            intent = new Intent(getApplicationContext(), ResultsActivity.class);
            intent.putExtra("caller", "MainActivity");
            startActivity(intent);
          }
          return true;
        case R.id.navigation_settings:
          intent = new Intent(getApplicationContext(), SettingsActivity.class);
          startActivity(intent);
          return true;
      }
      return handled;
    }
  };

  public static boolean result() {
    if (activityTimeElapsed <= timeLimit * 60000) {
      return true;
    } else {
      return false;
    }
  }

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
    String userName = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        .getString("username", "default username");
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
      btn.setOnClickListener(v -> {
        btn.setVisibility(View.INVISIBLE);
        inActivity = true;
        pager = findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        findViewById(R.id.navigation_dashboard).setVisibility(View.INVISIBLE);
        initActivity();
      });
      btn.setText(user.getName());
    }
  }

  public void stopActivityTimer() {
    if (activityTimer != null) {
      activityTimer.cancel();
      activityTimer = null;
      activityTimeElapsed += System.currentTimeMillis() - activityTimerStart;
    }
  }

  public void stopClockTimer() {
    if (clockTimer != null) {
      clockTimer.cancel();
      clockTimer = null;
    }
  }

  public void activityComplete() {
    stopClockTimer();
    stopActivityTimer();
    completeTime();
    addActivity();
  }

  private LiveData<UserEntity> getUser() {
    ActivitiesDatabase db = ActivitiesDatabase.getInstance(getApplication());
    return db.userDao().getButtonLastUser();
  }

  private void initActivity() {
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

  private void updateClock() {
    timeLimit = preferences.getInt("timer", 15);
    long remaining = (timeLimit * 60000) - (System.currentTimeMillis() - activityTimerStart);
    int minutes;
    double seconds;

    if (remaining > 0) {
      minutes = (int) (remaining / 60000);
      seconds = (remaining % 60000) / 1000.0;
    } else {
      minutes = timeLimit;
      seconds = 0;
    }
    clockDisplay.setText(String.format(clockFormat, minutes, seconds));
  }

  public String completeTime() {
    long minutes = activityTimeElapsed / 60000;
    long seconds = (activityTimeElapsed % 60000) / 1000;

    StringBuilder bldr = new StringBuilder();

    bldr.append(minutes).append(" min & ")
        .append(seconds).append(" sec");
    completeTime = bldr.toString();

    return completeTime;
  }

  public void addActivity() {
    new Thread(() -> {
      ActivitiesDatabase db = ActivitiesDatabase.getInstance(getApplication());
      UserEntity user1 = db.userDao().getLastUser();
      ActivityEntity newActivity = new ActivityEntity();
      newActivity.setUser(user1.getId());
      newActivity.setTimestamp(new Date());
      newActivity.setTime(activityTimeElapsed);
      newActivity.setResult(result());
      db.activityDao().insert(newActivity);
    }).start();
  }

  private class ClockTimerTask extends TimerTask {

    @Override
    public void run() {
      runOnUiThread(() -> MainActivity.this.updateClock());
    }

  }

  private class MyPagerAdapter extends FragmentStatePagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
      super(fm);
    }


    //TODO Make the fragment end switch work.
    @Override
    public Fragment getItem(int pos) {
      switch (pos) {
        case 0:
          return TaskFragment.newInstance("Get dressed.");
        case 1:
          return TaskFragment.newInstance("Brush your teeth.");
        case 2:
          return TaskFragment.newInstance("Make your bed.");
        case 3:
          return TaskFragment.newInstance("Put your shoes on.");
        case 4:
          return TaskFragment.newInstance("Get your backpack ready.");
        case 5:
          activityComplete();
          return TaskFragment.newInstance(completeTime);
        default:
          return TaskFragment.newInstance("Good morning!");
      }
    }

    @Override
    public int getCount() {
      return NUM_PAGES;
    }
  }
}
