package com.chigbrowsoftware.kgo.controller;

import android.Manifest.permission;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import com.chigbrowsoftware.kgo.R;
import com.chigbrowsoftware.kgo.model.database.ActivitiesDatabase;
import com.chigbrowsoftware.kgo.model.entity.Activity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**Displays a complete result upon activity completion, a weekly result page when accessed
 * through dashboard, and sends a calendar event to the parent's Google calendar if the activity
 * is completed on time.
 */
public class ResultsActivity extends AppCompatActivity
    implements EasyPermissions.PermissionCallbacks {

  //TODO Set daily view different from week view.

  private static final int REQUEST_ACCOUNT_PICKER = 1000;
  private static final int REQUEST_AUTHORIZATION = 1001;
  private static final int REQUEST_GOOGLE_PLAY = 1002;
  private static final int REQUEST_GET_ACCOUNTS_PERMISSION = 1003;
  private static final String PREFERRED_ACCOUNT = "preferred_account";
  private TextView mTextMessage;
  private TextView dayOfWeek;
  private String caller;
  private String day;
  private boolean result;
  private boolean isCompleteView;
  private String succeed = "Great job!";
  private String fail = "You can get it!";
  private SharedPreferences preferences;
  private SharedPreferences.Editor editor;
  private GoogleAccountCredential credential;

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
      = new BottomNavigationView.OnNavigationItemSelectedListener() {

    /** Sets bottom navigation for Results Activity.*/
    @SuppressWarnings("SwitchStatementWithoutDefaultBranch")
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
          intent = new Intent(getApplicationContext(), ResultsActivity.class);
          startActivity(intent);
          return true;
        case R.id.navigation_settings:
          intent = new Intent(getApplicationContext(), SettingsActivity.class);
          startActivity(intent);
          return true;
      }
      return handled;
    }
  };

  /**
   * Creates Results Activity.
   */
  //TODO Fix static view
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_results);

    findViewById(R.id.navigation_dashboard).setVisibility(View.INVISIBLE);
    findViewById(R.id.navigation_settings).setVisibility(View.INVISIBLE);

    mTextMessage = (TextView) findViewById(R.id.message);
    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    preferences = PreferenceManager.getDefaultSharedPreferences(this);
    editor = preferences.edit();

    caller = getIntent().getStringExtra("caller");
    try {
      Class callerClass = Class.forName(caller);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    isCompleteView();
    setResultsViews();

    credential = GoogleAccountCredential.usingOAuth2(ResultsActivity.this.getApplicationContext(),
        Arrays.asList(new String[]{CalendarScopes.CALENDAR})).setBackOff(new ExponentialBackOff());
    if (!isGooglePlayAvailable()) {
      acquireGooglePlayServices();
    } else if (credential.getSelectedAccountName() == null) {
      chooseAccount();
    }
  }

  private boolean isCompleteView() {
    if (caller.equals("TaskFragment")) {
      isCompleteView = true;
    }
    return isCompleteView;
  }

  private String getDay() {
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE", Locale.US);
    day = simpleDateFormat.format(date);
    return day;
  }

  private void setResultsViews() {

    //TODO Use live data to do this.
    Thread t = new Thread(() -> {
      ActivitiesDatabase db = ActivitiesDatabase.getInstance(getApplication());
      Activity activity = db.activityDao().getRecentActivity();
      this.result = activity.isResult();
    });
    t.start();
    try { //TODO Handle appropriately. Do something.
      t.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    getDay();
    if (isCompleteView && !day.equals("Saturday") && !day.equals("Sunday")) {
      hideDays();

      if (result) {
        new SuccessEventsTask().execute();

        switch (day) {
          case "Monday":
            clearResults();
            editor.clear();
            editor.commit();
            completeSuccessView();
            editor.putString("mon_result", succeed);
            editor.commit();
            break;
          case "Tuesday":
            completeSuccessView();
            editor.putString("tue_result", succeed);
            editor.commit();
            setMon();
            break;
          case "Wednesday":
            completeSuccessView();
            editor.putString("wed_result", succeed);
            editor.commit();
            setMon();
            setTue();
            break;
          case "Thursday":
            completeSuccessView();
            editor.putString("thu_result", succeed);
            editor.commit();
            setMon();
            setTue();
            setWed();
            break;
          case "Friday":
            editor.remove("fri_result"); //TODO remove after testing
            editor.commit();
            completeSuccessView();
            editor.putString("fri_result", succeed);
            editor.commit();
            setMon();
            setTue();
            setWed();
            setThu();
            break;
          default:
            setMon();
            setTue();
            setWed();
            setThu();
            setFri();
        }
      } else if (!result) {

        switch (day) {
          case "Monday":
            clearResults();
            editor.clear();
            editor.commit();
            completeFailView();
            editor.putString("mon_result", fail);
            editor.commit();
            break;
          case "Tuesday":
            completeFailView();
            editor.putString("tue_result", fail);
            editor.commit();
            setMon();
            break;
          case "Wednesday":
            completeFailView();
            editor.putString("wed_result", fail);
            editor.commit();
            setMon();
            setTue();
            break;
          case "Thursday":
            completeFailView();
            editor.putString("thu_result", fail);
            editor.commit();
            setMon();
            setTue();
            setWed();
            break;
          case "Friday":
            completeFailView();
            editor.putString("fri_result", fail);
            editor.commit();
            setMon();
            setTue();
            setWed();
            setThu();
            break;
          default:
            setMon();
            setTue();
            setWed();
            setThu();
            setFri();
        }
      }
    } else if (day.equals("Saturday") || day.equals("Sunday")
        || !isCompleteView) {
      new SuccessEventsTask().execute(); //TODO remove after testing
      setMon();
      setTue();
      setWed();
      setThu();
      setFri();
    }
  }

  private void clearResults() {
    findViewById(R.id.star1).setVisibility(View.INVISIBLE);
    findViewById(R.id.star2).setVisibility(View.INVISIBLE);
    findViewById(R.id.star3).setVisibility(View.INVISIBLE);
    findViewById(R.id.star4).setVisibility(View.INVISIBLE);
    findViewById(R.id.star5).setVisibility(View.INVISIBLE);
    findViewById(R.id.x1).setVisibility(View.INVISIBLE);
    findViewById(R.id.x2).setVisibility(View.INVISIBLE);
    findViewById(R.id.x3).setVisibility(View.INVISIBLE);
    findViewById(R.id.x4).setVisibility(View.INVISIBLE);
    findViewById(R.id.x5).setVisibility(View.INVISIBLE);
  }

  private void setMon() {
    try {
      if (!preferences.getString("mon_result", null).equals(null)
          && preferences.getString("mon_result", null).equals(succeed)) {
        findViewById(R.id.star1).setVisibility(View.VISIBLE);
      } else if (!preferences.getString("mon_result", null).equals(null)
          && preferences.getString("mon_result", null).equals(fail)) {
        findViewById(R.id.x1).setVisibility(View.VISIBLE);
      } else {
        findViewById(R.id.star1).setVisibility(View.INVISIBLE);
        findViewById(R.id.x1).setVisibility(View.INVISIBLE);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setTue() {
    try {
      if (!preferences.getString("tue_result", null).equals(null)
          && preferences.getString("tue_result", null).equals(succeed)) {
        findViewById(R.id.star2).setVisibility(View.VISIBLE);
      } else if (!preferences.getString("tue_result", null).equals(null)
          && preferences.getString("tue_result", null).equals(fail)) {
        findViewById(R.id.x2).setVisibility(View.VISIBLE);
      } else {
        findViewById(R.id.star2).setVisibility(View.INVISIBLE);
        findViewById(R.id.x2).setVisibility(View.INVISIBLE);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setWed() {
    try {
      if (!preferences.getString("wed_result", null).equals(null)
          && preferences.getString("wed_result", null).equals(succeed)) {
        findViewById(R.id.star3).setVisibility(View.VISIBLE);
      } else if (!preferences.getString("wed_result", null).equals(null)
          && preferences.getString("wed_result", null).equals(fail)) {
        findViewById(R.id.x3).setVisibility(View.VISIBLE);
      } else {
        findViewById(R.id.star3).setVisibility(View.INVISIBLE);
        findViewById(R.id.x3).setVisibility(View.INVISIBLE);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setThu() {
    try {
      if (!preferences.getString("thu_result", null).equals(null)
          && preferences.getString("thu_result", null).equals(succeed)) {
        findViewById(R.id.star4).setVisibility(View.VISIBLE);
      } else if (preferences.getString("thu_result", null).equals(fail)) {
        findViewById(R.id.x4).setVisibility(View.VISIBLE);
      } else {
        findViewById(R.id.star4).setVisibility(View.INVISIBLE);
        findViewById(R.id.x4).setVisibility(View.INVISIBLE);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setFri() {
    try {
      if (!preferences.getString("fri_result", null).equals(null)
          && preferences.getString("fri_result", null).equals(succeed)) {
        findViewById(R.id.star5).setVisibility(View.VISIBLE);
      } else if (preferences.getString("fri_result", null).equals(fail)) {
        findViewById(R.id.x5).setVisibility(View.VISIBLE);
      } else {
        findViewById(R.id.star5).setVisibility(View.INVISIBLE);
        findViewById(R.id.x5).setVisibility(View.INVISIBLE);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void completeSuccessView() {
    getDay();
    dayOfWeek = findViewById(R.id.day);
    dayOfWeek.setVisibility(View.VISIBLE);
    dayOfWeek.setText(day);
    findViewById(R.id.complete_star).setVisibility(View.VISIBLE);
    TextView view = findViewById(R.id.message);
    view.setVisibility(View.VISIBLE);
    view.setText(succeed);
  }

  private void completeFailView() {
    getDay();
    dayOfWeek = findViewById(R.id.day);
    dayOfWeek.setVisibility(View.VISIBLE);
    dayOfWeek.setText(day);
    findViewById(R.id.complete_x).setVisibility(View.VISIBLE);
    TextView view = findViewById(R.id.message);
    view.setVisibility(View.VISIBLE);
    view.setText(fail);
  }

  private void hideDays() {
    findViewById(R.id.monday).setVisibility(View.INVISIBLE);
    findViewById(R.id.tuesday).setVisibility(View.INVISIBLE);
    findViewById(R.id.wednesday).setVisibility(View.INVISIBLE);
    findViewById(R.id.thursday).setVisibility(View.INVISIBLE);
    findViewById(R.id.friday).setVisibility(View.INVISIBLE);
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
        new SuccessEventsTask().execute();
      } else {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
      }
    } else {
      EasyPermissions.requestPermissions(this, "This app needs to access your google account."
          , REQUEST_GET_ACCOUNTS_PERMISSION, permission.GET_ACCOUNTS);
    }

  }

  /**
   * Provides switch statement to handle response from Google services.
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case REQUEST_GOOGLE_PLAY:
        if (resultCode != RESULT_OK) {
          Toast.makeText(this, "This app requires Google Play services.", Toast.LENGTH_LONG).show();
        } else {
          new SuccessEventsTask().execute();
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
            new SuccessEventsTask().execute();
          }
        }
        break;
      case REQUEST_AUTHORIZATION:
        if (resultCode == RESULT_OK) {
          new SuccessEventsTask().execute();
        }
        break;
    }
  }

  /**
   * Required override for implementing EasyPermissions.PermissionCallbacks.
   */
  @Override
  public void onPermissionsGranted(int requestCode, List<String> perms) {

  }

  /**
   * Required override for implementing EasyPermissions.PermissionCallbacks.
   */
  @Override
  public void onPermissionsDenied(int requestCode, List<String> perms) {

  }

  //HACK This requires that the kid select the calendar to push the event to. Fix so that parent selects.
  private class SuccessEventsTask extends AsyncTask<Void, Void, Void> {

    private Calendar service;

    SuccessEventsTask() {
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
        event.setSummary(succeed);
        service.events().insert("primary", event).execute();
      } catch (UserRecoverableAuthIOException f) {
        startActivityForResult(f.getIntent(), REQUEST_AUTHORIZATION);
      } catch (Throwable e) {
        e.printStackTrace();
        cancel(true);
      }
      return null;
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



