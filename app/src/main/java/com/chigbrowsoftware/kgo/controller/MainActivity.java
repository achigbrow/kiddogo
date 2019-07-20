package com.chigbrowsoftware.kgo.controller;

import android.Manifest;
import android.Manifest.permission;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import com.chigbrowsoftware.kgo.R;
import com.chigbrowsoftware.kgo.fragments.TaskFragment;
import com.chigbrowsoftware.kgo.model.database.ActivitiesDatabase;
import com.chigbrowsoftware.kgo.model.entity.ActivityEntity;
import com.chigbrowsoftware.kgo.model.entity.UserEntity;
import com.chigbrowsoftware.kgo.model.viewmodel.UserViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity
    implements OnSharedPreferenceChangeListener,  EasyPermissions.PermissionCallbacks {

  //TODO add logout


  public final static int NUM_PAGES = 6;
  private static final int REQUEST_ACCOUNT_PICKER = 1000;
  private static final int REQUEST_AUTHORIZATION = 1001;
  private static final int REQUEST_GOOGLE_PLAY = 1002;
  private static final int REQUEST_GET_ACCOUNTS_PERMISSION = 1003;
  public static final String PREFERRED_ACCOUNT = "preferred_account";
  private static long activityTimeElapsed;
  public static androidx.viewpager.widget.ViewPager pager;
  private static int timeLimit;
  private TextView clockDisplay;
  private long userId;
  private Button btn;
  private TextView mTextMessage;
  private SharedPreferences preferences;
  private Timer activityTimer;
  private Timer clockTimer;
  private String clockFormat;
  private String completeTime;
  private long activityTimerStart;
  private boolean inActivity;
  private GoogleAccountCredential credential;
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
    if (activityTimeElapsed <= (timeLimit * 60000)) {
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
    clockFormat = getString(R.string.clock_format);
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    credential = GoogleAccountCredential.usingOAuth2(MainActivity.this.getApplicationContext(),
        Arrays.asList(new String[] {CalendarScopes.CALENDAR})).setBackOff(new ExponentialBackOff());
    if (!isGooglePlayAvailable()) {
      acquireGooglePlayServices();
    } else if (credential.getSelectedAccountName() == null) {
      chooseAccount();
    } else {
      new TestEventsTask().execute();
    }
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
    UserEntity user = new UserEntity();
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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case REQUEST_GOOGLE_PLAY :
        if (resultCode != RESULT_OK) {
          Toast.makeText(this, "This app requires Google Play services.", Toast.LENGTH_LONG).show();
        } else {
          new TestEventsTask().execute();
        }
        break;
      case REQUEST_ACCOUNT_PICKER :
        if ((requestCode == RESULT_OK) && (data != null) && (data.getExtras() != null)) {
          String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
          if (accountName != null) {
            SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(PREFERRED_ACCOUNT, accountName);
            editor.apply();
            credential.setSelectedAccountName(accountName);
            new TestEventsTask().execute();
          }
        }
        break;
      case REQUEST_AUTHORIZATION :
        if (resultCode == RESULT_OK) {
          new TestEventsTask().execute();
        }
        break;
    }
  }

  @Override
  public void onPermissionsGranted(int requestCode, List<String> perms) {

  }

  @Override
  public void onPermissionsDenied(int requestCode, List<String> perms) {

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

  private boolean isGooglePlayAvailable() {
    GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
    int connectionStatusCode = availability.isGooglePlayServicesAvailable(this);
    return connectionStatusCode == ConnectionResult.SUCCESS;
  }

  private void acquireGooglePlayServices() {
    GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
    int connectionStatusCode = availability.isGooglePlayServicesAvailable(this);
    if (availability.isUserResolvableError(connectionStatusCode)) {
      Dialog dialog = availability.getErrorDialog(this, connectionStatusCode, REQUEST_GOOGLE_PLAY);
      dialog.show();
    }
  }

  //TODO update google play update

  private void calendarTest(){

  }

  @AfterPermissionGranted(REQUEST_GET_ACCOUNTS_PERMISSION)
  private void chooseAccount () {
    if (EasyPermissions.hasPermissions(this, permission.GET_ACCOUNTS)) {
      String accountName = getPreferences(Context.MODE_PRIVATE).getString(PREFERRED_ACCOUNT, null);
      if (accountName != null) {
        credential.setSelectedAccountName(accountName);
        new TestEventsTask().execute();
      } else {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
      }
    } else {
      EasyPermissions.requestPermissions(this, "This app needs to access your google account."
      , REQUEST_GET_ACCOUNTS_PERMISSION, permission.GET_ACCOUNTS);
    }

  }

  private class TestEventsTask extends AsyncTask<Void, Void, Void> {

    private Calendar service;


    public TestEventsTask() {
      HttpTransport transport = AndroidHttp.newCompatibleTransport();
      JsonFactory factory = JacksonFactory.getDefaultInstance();
//      credential = GoogleAccountCredential.usingOAuth2(MainActivity.this.getApplicationContext(),
//          Arrays.asList(new String[] {CalendarScopes.CALENDAR})).setBackOff(new ExponentialBackOff());
      service = new Calendar.Builder(transport, factory, credential)
      .setApplicationName("KiddoGo").build();
    }

    @Override
    protected Void doInBackground(Void... voids) {
      try {
        Event event = new Event();
        EventDateTime start = new EventDateTime();
        EventDateTime end = new EventDateTime();
        start.setDateTime(new DateTime(System.currentTimeMillis()));
        end.setDateTime(new DateTime(System.currentTimeMillis() + (1000 * 3600)));
        event.setStart(start);
        event.setEnd(end);
        event.setSummary("Test");
        service.events().insert("primary", event).execute();
      } catch (Throwable e) {
        e.printStackTrace();
        cancel(true);
      }
      return null;
    }
  }
}
