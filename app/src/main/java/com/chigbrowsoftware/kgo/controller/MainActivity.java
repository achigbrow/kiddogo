package com.chigbrowsoftware.kgo.controller;

import android.Manifest.permission;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
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
import com.chigbrowsoftware.kgo.fragment.TaskFragment;
import com.chigbrowsoftware.kgo.model.database.ActivitiesDatabase;
import com.chigbrowsoftware.kgo.model.entity.Activity;
import com.chigbrowsoftware.kgo.model.entity.UserEntity;
import com.chigbrowsoftware.kgo.model.viewmodel.UserViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Builds the 'home screen', a play button, and a timer. Confirms Google Play availability
 * and account.
 */
public class MainActivity extends AppCompatActivity
    implements OnSharedPreferenceChangeListener, EasyPermissions.PermissionCallbacks {

  //TODO add logout

  public final static int NUM_PAGES = 6;
  private static final String PREFERRED_ACCOUNT = "preferred_account";
  private static final int REQUEST_ACCOUNT_PICKER = 1000;
  private static final int REQUEST_AUTHORIZATION = 1001;
  private static final int REQUEST_GOOGLE_PLAY = 1002;
  private static final int REQUEST_GET_ACCOUNTS_PERMISSION = 1003;
  public static androidx.viewpager.widget.ViewPager pager;
  private static long activityTimeElapsed;
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
  private boolean activityComplete = false;
  private GoogleAccountCredential credential;

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
      = new BottomNavigationView.OnNavigationItemSelectedListener() {

    /** Sets bottom navigation for Main Activity.*/
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
          if (!inActivity) {
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

  private static boolean result() {
    return activityTimeElapsed <= (timeLimit * 60000);
  }

  /**Creates Main Activity*/
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
        Arrays.asList(new String[]{CalendarScopes.CALENDAR})).setBackOff(new ExponentialBackOff());
    if (!isGooglePlayAvailable()) {
      acquireGooglePlayServices();
    } else if (credential.getSelectedAccountName() == null) {
      chooseAccount();
    }
  }

  /**Checks for changes to Shared Preferences and makes appropriate updates.*/
  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    readSettings();
  }

  private void readSettings() {
    String username = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        .getString("username", "default username");
    timeLimit = preferences.getInt("timer", 15);
    UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    if (!username.equals("default username")) {
      UserEntity user = new UserEntity();
      user.setName(username);
      userViewModel.addUser(user);
      userId = user.getId();
      btn.setText(user.getName());
    }
  }

  //TODO Build the ability to create 4 buttons of different colors for up to 4 users added.
  //TODO Tie the button opened activity to the user.
  //TODO Maybe? Figure out how to handle back press when user is in fragment activity and navigation is hidden.
  private void buildButton(UserEntity user) {
    if (user != null && activityComplete == false) {
      btn = findViewById(R.id.button);
      btn.setOnClickListener(v -> {
        btn.setVisibility(View.INVISIBLE);
        inActivity = true;
        pager = findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        findViewById(R.id.navigation_play).setVisibility(View.INVISIBLE);
        findViewById(R.id.navigation_dashboard).setVisibility(View.INVISIBLE);
        findViewById(R.id.navigation_settings).setVisibility(View.INVISIBLE);
        initActivity();
        activityComplete = true;
      });
      btn.setText(user.getName());
    }
  }

  private void stopActivityTimer() {
    if (activityTimer != null) {
      activityTimer.cancel();
      activityTimer = null;
      activityTimeElapsed += System.currentTimeMillis() - activityTimerStart;
    }
  }

  private void stopClockTimer() {
    if (clockTimer != null) {
      clockTimer.cancel();
      clockTimer = null;
    }
  }

  private void activityComplete() {
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

  private class ClockTimerTask extends TimerTask {

    @Override
    public void run() {
      runOnUiThread(() -> MainActivity.this.updateClock());
    }

  }

  //I don't know why it says that the return value of the method is never used. It is used in final fragment.
  private String completeTime() {
    long minutes = activityTimeElapsed / 60000;
    long seconds = (activityTimeElapsed % 60000) / 1000;

    StringBuilder bldr = new StringBuilder();

    bldr.append(minutes).append(" min & ")
        .append(seconds).append(" sec");
    completeTime = bldr.toString();

    return completeTime;
  }

  private void addActivity() {
    new Thread(() -> {
      ActivitiesDatabase db = ActivitiesDatabase.getInstance(getApplication());
      UserEntity user1 = db.userDao().getLastUser();
      Activity newActivity = new Activity();
      newActivity.setUser(user1.getId());
      newActivity.setTimestamp(new Date());
      newActivity.setTime(activityTimeElapsed);
      newActivity.setResult(result());
      db.activityDao().insert(newActivity);
    }).start();
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

  @AfterPermissionGranted(REQUEST_GET_ACCOUNTS_PERMISSION)
  private void chooseAccount() {
    if (EasyPermissions.hasPermissions(this, permission.GET_ACCOUNTS)) {
      String accountName = getPreferences(Context.MODE_PRIVATE).getString(PREFERRED_ACCOUNT, null);
      if (accountName != null) {
        credential.setSelectedAccountName(accountName);
      } else {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
      }
    } else {
      EasyPermissions.requestPermissions(this, "This app needs to access your google account."
          , REQUEST_GET_ACCOUNTS_PERMISSION, permission.GET_ACCOUNTS);
    }

  }

  //TODO update google play update

  /** Provides switch statement to handle response from Google services. */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case REQUEST_GOOGLE_PLAY:
        if (resultCode != RESULT_OK) {
          Toast.makeText(this, "This app requires Google Play services.", Toast.LENGTH_LONG).show();
        }
        break;
      case REQUEST_ACCOUNT_PICKER:
        if ((resultCode == RESULT_OK) && (data != null) && (data.getExtras() != null)) {
          String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
          if (!accountName.equals(null)) {
            SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(PREFERRED_ACCOUNT, accountName);
            editor.apply();
            credential.setSelectedAccountName(accountName);
          }
        }
        break;
      case REQUEST_AUTHORIZATION:
        if (resultCode == RESULT_OK) {
          //TODO What goes here?
        }
        break;
    }
  }

  /** Required override for implementing EasyPermissions.PermissionCallbacks. */
  @Override
  public void onPermissionsGranted(int requestCode, List<String> perms) {

  }

  /** Required override for implementing EasyPermissions.PermissionCallbacks. */
  @Override
  public void onPermissionsDenied(int requestCode, List<String> perms) {

  }

  private class MyPagerAdapter extends FragmentStatePagerAdapter {

    MyPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    //TODO Add a text to speech element to read each step.
    @Override
    public Fragment getItem(int pos) {
      switch (pos) {
        case 0:
          return TaskFragment.newInstance(getString(R.string.step1));
        case 1:
          return TaskFragment.newInstance(getString(R.string.step2));
        case 2:
          return TaskFragment.newInstance(getString(R.string.step3));
        case 3:
          return TaskFragment.newInstance(getString(R.string.step4));
        case 4:
          return TaskFragment.newInstance(getString(R.string.step5));
        case 5:
          activityComplete();
          return TaskFragment.newInstance(completeTime);
        default:
          return TaskFragment.newInstance(getString(R.string.default_msg));
      }
    }

    /** Override required to use Fragment Manager. */
    @Override
    public int getCount() {
      return NUM_PAGES;
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
